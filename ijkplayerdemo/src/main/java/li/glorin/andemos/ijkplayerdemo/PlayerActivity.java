package li.glorin.andemos.ijkplayerdemo;

import android.graphics.SurfaceTexture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Button;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btnPlay = findViewById(R.id.btnPlay);

        mTextureView = findViewById(R.id.textureView);

        mTextureView.setSurfaceTextureListener(this);


        // Create player
        mIjkMediaPlayer = new IjkMediaPlayer();

        mIjkMediaPlayer.setOnPreparedListener(mIjkListener);
        mIjkMediaPlayer.setOnInfoListener(mIjkListener);
        mIjkMediaPlayer.setOnSeekCompleteListener(mIjkListener);
        mIjkMediaPlayer.setOnBufferingUpdateListener(mIjkListener);
        mIjkMediaPlayer.setOnErrorListener(mIjkListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mIjkMediaPlayer.release();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mIjkMediaPlayer.setSurface(new Surface(surface));
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
}
