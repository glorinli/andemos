package com.dogold.andemos.opengles;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLSurfaceViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glsurface_view);

        GLSurfaceView glSurfaceView = findViewById(R.id.glsvSurface);

        glSurfaceView.setEGLContextClientVersion(2);

        glSurfaceView.setRenderer(new MyRenderer());

        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class MyRenderer implements GLSurfaceView.Renderer {
        private int mProgram;
        private int mVPosition;
        private int mUColor;

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

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            // Init here
            mProgram = GLESUtils.createProgram(mVertextSrc, mFragmentSrc);

            mVPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
            mUColor = GLES20.glGetUniformLocation(mProgram, "uColor");

            // RGBA
            GLES20.glClearColor(1f, 0f, 0f, 1f);
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            if(mProgram == 0) return;

            FloatBuffer vertices = getVertices();

            // Clear display
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            // Use program
            GLES20.glUseProgram(mProgram);

            // Set vertices
            GLES20.glVertexAttribPointer(mVPosition, 2, GLES20.GL_FLOAT, false, 0, vertices);

            GLES20.glEnableVertexAttribArray(mVPosition);

            // Set color, RGBA
            GLES20.glUniform4f(mUColor, 0f, 1f, 0f, 1f);

            // Draw arrays
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3);
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
    }
}
