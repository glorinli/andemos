package com.dogold.andemos.opengles.egl;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import com.dogold.andemos.opengles.GLESUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGL;

public class GLRenderer extends HandlerThread {
    private static final String TAG = "GLRenderer";

    private EGLConfig mEGLConfig;
    private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;

    private int mGLProgram;
    private int mGLAttributePosition;
    private int mGLUniformColor;

    public GLRenderer() {
        super(TAG);
    }

    private void createGL() {
        // Get default display
        mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);

        // Init
        int[] version = new int[2];
        if (!EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1)) {
            throw new RuntimeException("EGL init error: " + EGL14.eglGetError());
        }

        // Get framtbuffer type and capcity
        int[] configAttributes = {
                EGL14.EGL_BUFFER_SIZE, 32,
                EGL14.EGL_ALPHA_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL14.EGL_SURFACE_TYPE, EGL14.EGL_WINDOW_BIT,
                EGL14.EGL_NONE
        };

        // Select config
        int[] numConfigs = new int[1];
        EGLConfig[] configs = new EGLConfig[1];

        if (!EGL14.eglChooseConfig(mEGLDisplay, configAttributes, 0, configs,
                0, configs.length, numConfigs, 0)) {
            throw new RuntimeException("Error select config: " + EGL14.eglGetError());
        }
        mEGLConfig = configs[0];

        // Create GL Context
        int[] contextAttributes = {
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL14.EGL_NONE
        };
        mEGLContext = EGL14.eglCreateContext(mEGLDisplay, mEGLConfig, EGL14.EGL_NO_CONTEXT,
                contextAttributes, 0);
        if (mEGLContext == EGL14.EGL_NO_CONTEXT) {
            throw new RuntimeException("Error create context: " + EGL14.eglGetError());
        }
    }

    private void destroyGL() {
        EGL14.eglDestroyContext(mEGLDisplay, mEGLContext);
        mEGLContext = EGL14.EGL_NO_CONTEXT;
        mEGLDisplay = EGL14.EGL_NO_DISPLAY;
    }

    @Override
    public synchronized void start() {
        super.start();

        new Handler(getLooper()).post(new Runnable() {
            @Override
            public void run() {
                createGL();
            }
        });
    }

    public void release() {
        new Handler(getLooper()).post(new Runnable() {
            @Override
            public void run() {
                destroyGL();

                quitSafely();
            }
        });
    }

    private FloatBuffer getVertices() {
        float[] vertices = new float[]{
                0f, 0.5f,
                -0.5f, -0.5f,
                0.5f, -0.5f
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);

        vbb.order(ByteOrder.nativeOrder());

        FloatBuffer buffer = vbb.asFloatBuffer();
        buffer.put(vertices);
        buffer.position(0);

        return buffer;
    }

    public void render(Surface surface, int width, int height) {
        final int[] surfaceAttributes = {EGL14.EGL_NONE};

        EGLSurface eglSurface = EGL14.eglCreateWindowSurface(mEGLDisplay,
                mEGLConfig, surface, surfaceAttributes, 0);
        EGL14.eglMakeCurrent(mEGLDisplay, eglSurface, eglSurface, mEGLContext);

        // Create program
        mGLProgram = GLESUtils.createProgram(mVertextSrc, mFragmentSrc);
        mGLAttributePosition = GLES20.glGetAttribLocation(mGLProgram, "vPosition");
        mGLUniformColor = GLES20.glGetUniformLocation(mGLProgram, "uColor");

        // Set clear color
        GLES20.glClearColor(1f, 0f, 0f, 1f);

        // Set view port
        GLES20.glViewport(0, 0, width, height);

        // Get vertext buffer
        FloatBuffer vertices = getVertices();

        // Clear window
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // Use program
        GLES20.glUseProgram(mGLProgram);

        // Set vertices
        GLES20.glVertexAttribPointer(mGLAttributePosition, 2, GLES20.GL_FLOAT,
                false, 0, vertices);

        // Enable attribute
        GLES20.glEnableVertexAttribArray(mGLAttributePosition);

        // Set uniform color
        GLES20.glUniform4f(mGLUniformColor, 0f, 1f, 0f, 1f);    // RGBA

        // Draw
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3);

        // Swap buffer
        EGL14.eglSwapBuffers(mEGLDisplay, eglSurface);

        // Destroy
        EGL14.eglDestroySurface(mEGLDisplay, eglSurface);
    }


    private String mVertextSrc =
            "attribute vec2 vPosition;\n" +
                    "void main() {\n" +
                    "    gl_Position = vec4(vPosition, 0, 1);\n" +
                    "}";

    private String mFragmentSrc =
            "precision mediump float;\n" +
                    "uniform vec4 uColor;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = uColor;\n" +
                    "}";
}
