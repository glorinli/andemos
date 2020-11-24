package com.dogold.andemos.opengles;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dogold.andemos.opengles.egl.SurfaceViewActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnGlSurfaceView).setOnClickListener(this);
        findViewById(R.id.btnSurfaceView).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGlSurfaceView:
                startActivity(new Intent(this, GLSurfaceViewActivity.class));
                break;
            case R.id.btnSurfaceView:
                startActivity(new Intent(this, SurfaceViewActivity.class));
        }
    }
}
