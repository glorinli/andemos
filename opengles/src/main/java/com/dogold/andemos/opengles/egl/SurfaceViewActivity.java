package com.dogold.andemos.opengles.egl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dogold.andemos.opengles.R;

public class SurfaceViewActivity extends AppCompatActivity {
    private GLRenderer mGLRenderer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_surface_view);

        mGLRenderer = new GLRenderer();
        mGLRenderer.start();

        SurfaceView sv = findViewById(R.id.svSurface);

        sv.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mGLRenderer.render(holder.getSurface(), width, height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mGLRenderer.release();
        mGLRenderer = null;
    }
}
