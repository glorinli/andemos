package com.dogold.andemos.opengles;

import android.opengl.GLES20;
import android.util.Log;

public class GLESUtils {
    public static int loadShader(int shaderType, String source) {
        // Create a new shader
        int shader = GLES20.glCreateShader(shaderType);

        // If creates success
        if (shader != 0) {
            // Load souce
            GLES20.glShaderSource(shader, source);

            // Compile shader
            GLES20.glCompileShader(shader);

            // Get compile result
            int[] compiled = new int[1];

            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

            if (compiled[0] == 0) {
                // Compile failed, exit
                Log.e("ES20_ERROR", "Could not compile shader " + shaderType + ":");
                Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));

                GLES20.glDeleteShader(shader);

                shader = 0;
            }
        }

        return shader;
    }

    public static int createProgram(String vertextSrc, String fragmentSrc) {
        int vShader = loadShader(GLES20.GL_VERTEX_SHADER, vertextSrc);

        if (vShader == 0) {
            return 0;
        }

        int fShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSrc);

        if (fShader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();

        if (program != 0) {
            // Attach shaders
            GLES20.glAttachShader(program, vShader);
            GLES20.glAttachShader(program, fShader);

            // Link program
            GLES20.glLinkProgram(program);

            // Check link status
            int[] linkStatus = new int[1];

            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);

            if (linkStatus[0] != GLES20.GL_TRUE) {
                // Link failed, delete program
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));

                GLES20.glDeleteProgram(program);

                program = 0;
            }
        }

        return program;
    }
}
