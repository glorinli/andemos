package xyz.dogold.andemos.av.textureview;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import xyz.dogold.andemos.av.R;
import xyz.dogold.andemos.common.utils.FileUtils;

/**
 * TextureViewActivity
 * Created by glorin on 27/11/2017.
 */

@RuntimePermissions
public class TextureViewActivity extends AppCompatActivity {
    private static final String TAG = "TextureViewActivity";

    @BindView(R.id.ttvTextureView)
    TextureView ttvTextureView;

    private Unbinder mUnbinder;

    private SurfaceTexture mSurfaceTexture;

    private Bitmap mSurfaceBitmap;

    private int mTextureViewInitWidth, mTextureViewInitHeight;

    private Rect mBitmapRect;

    private Rect mSurfaceRect;

    private static final String BITMAP_SAVE_PATH = FileUtils.APP_EXTERNAL_STORAGE_DIR + File.separator + "av/textureview_bitmap.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_textureview);

        mUnbinder = ButterKnife.bind(this);

        mBitmapRect = new Rect();
        mSurfaceRect = new Rect();

        ttvTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mSurfaceTexture = surface;

                drawTextureView();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                drawTextureView();
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                mSurfaceTexture = null;
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//                drawTextureView();
            }
        });

        ttvTextureView.post(new Runnable() {
            @Override
            public void run() {
                mTextureViewInitWidth = ttvTextureView.getWidth();
                mTextureViewInitHeight = ttvTextureView.getHeight();
                drawTextureView();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();
    }

    @OnClick({R.id.btnGetBitmap, R.id.btnDoubleBufferSize, R.id.btnDoubleViewSize,
            R.id.btnSetMatrix, R.id.btnViewBitmap})
    void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnGetBitmap:
                TextureViewActivityPermissionsDispatcher.getAndSaveTextureViewBitmapWithPermissionCheck(this);
                break;
            case R.id.btnDoubleBufferSize:
                ttvTextureView.getSurfaceTexture().setDefaultBufferSize(ttvTextureView.getWidth() * 2, ttvTextureView.getHeight() * 2);

                drawTextureView();
                break;
            case R.id.btnDoubleViewSize:
                ttvTextureView.getLayoutParams().width = mTextureViewInitWidth * 2;
                ttvTextureView.getLayoutParams().height = mTextureViewInitHeight * 2;
                ttvTextureView.requestLayout();

                drawTextureView();
                break;
            case R.id.btnSetMatrix:
                Matrix matrix = new Matrix();

                matrix.postScale((float) mTextureViewInitWidth / ttvTextureView.getWidth(),
                        (float) mTextureViewInitHeight / ttvTextureView.getHeight());

                ttvTextureView.setTransform(matrix);

                drawTextureView();
                break;
            case R.id.btnViewBitmap:
                break;
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void getAndSaveTextureViewBitmap() {
        final Bitmap bitmap = ttvTextureView.getBitmap();

        if (bitmap != null && !bitmap.isRecycled()) {
            OutputStream os = null;
            try {
                File f = new File(BITMAP_SAVE_PATH);

                final File parentFile = f.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }

                os = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                Toast.makeText(this, "Get bitmap, size is: " + bitmap.getWidth() + ", " + bitmap.getHeight(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error save bitmap: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                if (os != null) {
                    try {
                        os.flush();
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void drawTextureView() {
        if (mSurfaceTexture == null) return;

        final Canvas canvas = ttvTextureView.lockCanvas();

        canvas.drawColor(Color.BLACK);

        if (mSurfaceBitmap == null) {
            mSurfaceBitmap = BitmapFactory.decodeResource(getResources(), R.raw.starry);
        }


        Log.d(TAG, "mSurfaceBitmap = " + mSurfaceBitmap + ", canvas size: " + canvas.getWidth() + ", " + canvas.getHeight());

        try {
            if (mSurfaceBitmap != null && !mSurfaceBitmap.isRecycled()) {
                mBitmapRect.set(0, 0, mSurfaceBitmap.getWidth(), mSurfaceBitmap.getHeight());
                mSurfaceRect.set(0, 0, ttvTextureView.getWidth(), ttvTextureView.getHeight());
                canvas.drawBitmap(mSurfaceBitmap, mBitmapRect, mSurfaceRect, null);
            }
        } finally {
            ttvTextureView.unlockCanvasAndPost(canvas);
        }
    }
}
