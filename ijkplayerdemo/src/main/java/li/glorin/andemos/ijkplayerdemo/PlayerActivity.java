package li.glorin.andemos.ijkplayerdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "PlayerActivity";

    private IjkMediaPlayer mIjkMediaPlayer;
    private IjkPlayerListener mIjkListener = new IjkPlayerListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            Log.d(TAG, "onPrepared");
        }
    };

    private TextureView mTextureView;
    private Button btnPlay;

    public static void start(@NonNull Context starter, @NonNull Uri uri) {
        Intent intent = new Intent(starter, PlayerActivity.class);
        intent.setData(uri);

        if (!(starter instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        starter.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Uri uri = getIntent().getData();

        if (uri == null) {
            throw new IllegalArgumentException("Should provide uri");
        }

        setContentView(R.layout.activity_player);

        btnPlay = findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(this);

        mTextureView = findViewById(R.id.textureView);

        mTextureView.setSurfaceTextureListener(this);

        RadioGroup rgSpeed = findViewById(R.id.rgSpeed);
        rgSpeed.setOnCheckedChangeListener(this);

        // Create player
        mIjkMediaPlayer = new IjkMediaPlayer();

        mIjkMediaPlayer.setOnPreparedListener(mIjkListener);
        mIjkMediaPlayer.setOnInfoListener(mIjkListener);
        mIjkMediaPlayer.setOnSeekCompleteListener(mIjkListener);
        mIjkMediaPlayer.setOnBufferingUpdateListener(mIjkListener);
        mIjkMediaPlayer.setOnErrorListener(mIjkListener);

        try {
            mIjkMediaPlayer.setDataSource(this, uri);
        } catch (IOException e) {
            Log.e(TAG, "Error set data source", e);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mIjkMediaPlayer.release();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "setSurface");
        mIjkMediaPlayer.setSurface(new Surface(surface));

        mIjkMediaPlayer.prepareAsync();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Noops
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Noops
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPlay) {
            togglePlay();
        }
    }

    private void togglePlay() {
        if (mIjkMediaPlayer.isPlaying()) {
            mIjkMediaPlayer.pause();
        } else {
            if (mIjkMediaPlayer.isPlayable()) {
                mIjkMediaPlayer.start();
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.rgSpeed) {
            setPlaybackSpeed(getCheckedSpeed(checkedId));
        }
    }

    private void setPlaybackSpeed(float speed) {
        if (mIjkMediaPlayer != null) {
            mIjkMediaPlayer.setSpeed(speed);
        }
    }

    private float getCheckedSpeed(int checkedId) {
        switch (checkedId) {
            case R.id.rbtn1_4:
                return 1f / 4;
            case R.id.rbtn1:
                return 1f;
            case R.id.rbtn2:
                return 2f;
            case R.id.rbtn4:
                return 4f;
            case R.id.rbtn8:
                return 8f;
            default:
                return 1f;
        }
    }
}
