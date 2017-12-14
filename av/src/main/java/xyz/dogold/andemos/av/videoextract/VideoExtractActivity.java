package xyz.dogold.andemos.av.videoextract;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import xyz.dogold.andemos.av.R;
import xyz.dogold.andemos.common.utils.FileUtils;
import xyz.dogold.andemos.common.utils.ToastUtil;

/**
 * VideoExtractActivity
 * Created by glorin on 14/12/2017.
 */

public class VideoExtractActivity extends AppCompatActivity {
    private static final int REQUEST_SELECT_VIDEO = 1888;
    private Unbinder mUnbinder;

    @BindView(R.id.tvVideoPath)
    TextView tvVideoPath;
    @BindView(R.id.tvVideoInfo)
    TextView tvVideoInfo;

    private String mVideoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_extract);

        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @OnClick({R.id.btnSelectVideo, R.id.btnExtract})
    void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnSelectVideo:
                selectVideo();
                break;
            case R.id.btnExtract:
                if (!TextUtils.isEmpty(mVideoPath)) {
                    final long startTime = SystemClock.elapsedRealtime();
                    final List<VideoFrameInfo> videoFrameInfo = VideoExtractHelper.getVideoFrameInfo(mVideoPath);

                    if (videoFrameInfo != null) {
                        final int frameCount = videoFrameInfo.size();
                        tvVideoInfo.setText("FrameCount: " + frameCount);
                        final long timeCostMs = SystemClock.elapsedRealtime() - startTime;
                        tvVideoInfo.append("\nTime cost: " + timeCostMs + "ms");
                        tvVideoInfo.append("\nTime per frame: " + timeCostMs * 1f / frameCount + "ms");
                    } else {
                        tvVideoInfo.setText("Cannot get video frames");
                    }
                } else {
                    ToastUtil.showToast(this, "Please select video");
                }
                break;
        }
    }

    private void selectVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_SELECT_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_VIDEO && resultCode == Activity.RESULT_OK) {
            final Uri uri = data.getData();

            if (uri != null) {
                if ("file".equalsIgnoreCase(uri.getScheme())) {
                    mVideoPath = uri.getPath();
                } else {
                    mVideoPath = FileUtils.getRealPathFromURI(this, uri);
                }

                tvVideoPath.setText(mVideoPath);
            }
        }
    }
}
