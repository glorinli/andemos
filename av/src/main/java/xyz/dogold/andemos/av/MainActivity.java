package xyz.dogold.andemos.av;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import xyz.dogold.andemos.av.textureview.TextureViewActivity;
import xyz.dogold.andemos.av.videoextract.VideoExtractActivity;

public class MainActivity extends AppCompatActivity {
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();
    }

    @OnClick({R.id.btnTextureView, R.id.btnVideoExtract})
    void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnTextureView:
                startActivity(new Intent(this, TextureViewActivity.class));
                break;
            case R.id.btnVideoExtract:
                startActivity(new Intent(this, VideoExtractActivity.class));
                break;
        }
    }
}
