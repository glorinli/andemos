package xyz.dogold.andemos.sharing;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import xyz.dogold.andemos.common.utils.FileUtils;
import xyz.dogold.andemos.common.utils.ToastUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String IMAGE_PATH = Environment.getExternalStorageDirectory() + "/sharingdemo/image.jpg";
    private Unbinder mButterKnifeUnbinder;

    @BindView(R.id.rbAllPlatform)
    RadioButton rbAllPlatform;

    @BindView(R.id.rbFacebook)
    RadioButton rbFacebook;

    @BindView(R.id.rbTwitter)
    RadioButton rbTwitter;

    @BindView(R.id.rbInstagram)
    RadioButton rbInstagram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButterKnifeUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mButterKnifeUnbinder != null) {
            mButterKnifeUnbinder.unbind();
        }
    }

    @OnClick({R.id.btnShareText, R.id.btnShareImage, R.id.btnShareVideo, R.id.btnShareLink, R.id.btnShareImageExplicit, R.id.btnShareVideoExplicit})
    void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnShareText:
                shareText("Hello android #Test", getTargetPackage());
                break;
            case R.id.btnShareImage:
                shareImage(IMAGE_PATH, getTargetPackage());
                break;
            case R.id.btnShareVideo:
                shareVideo(Environment.getExternalStorageDirectory() + "/sharingdemo/video.mp4", getTargetPackage());
                break;
            case R.id.btnShareLink:
                shareLink("Link", "https://www.baidu.com/", getTargetPackage());
                break;
            case R.id.btnShareImageExplicit:
                shareImageWithComponent(IMAGE_PATH, getTargetComponentName());
                break;
        }
    }

    private String getTargetPackage() {
        if (rbAllPlatform.isChecked()) {
            return null;
        } else if (rbFacebook.isChecked()) {
            return "com.facebook.katana";
        } else if (rbTwitter.isChecked()) {
            return "com.twitter.android";
        } else if (rbInstagram.isChecked()) {
            return "com.instagram.android";
        } else {
            return null;
        }
    }

    private ComponentName getTargetComponentName() {
        if (rbAllPlatform.isChecked()) {
            return null;
        } else if (rbFacebook.isChecked()) {
            return new ComponentName("com.facebook.katana", "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias");
        } else if (rbTwitter.isChecked()) {
            return new ComponentName("com.twitter.android", "com.twitter.composer.ComposerShareActivity");
        } else if (rbInstagram.isChecked()) {
            return new ComponentName("com.instagram.android", "com.instagram.android.activity.ShareHandlerActivity");
        } else {
            return null;
        }
    }

    private void shareText(String text, String targetPackage) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);

        if (TextUtils.isEmpty(targetPackage)) {
            startActivity(Intent.createChooser(intent, "Share text"));
        } else {
            if (checkAppInstall(targetPackage)) {
                intent.setPackage(targetPackage);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    ToastUtil.showToast(this, "Fail to share: " + e.getMessage());
                }
            } else {
                ToastUtil.showToast(this, targetPackage + " not installed");
            }
        }
    }

    private void shareLink(String title, String link, String targetPackage) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, "Link: " + link);

        if (TextUtils.isEmpty(targetPackage)) {
            startActivity(Intent.createChooser(intent, "Share text"));
        } else {
            if (checkAppInstall(targetPackage)) {
                intent.setPackage(targetPackage);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    ToastUtil.showToast(this, "Fail to share: " + e.getMessage());
                }
            } else {
                ToastUtil.showToast(this, targetPackage + " not installed");
            }
        }
    }

    private void shareImage(String imagePath, String targetPackage) {
        if (!FileUtils.isFileExists(imagePath)) {
            ToastUtil.showToast(this, "Target image does not exist: " + imagePath);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri fileUri = getFileUri(imagePath);
        Log.d(TAG, "shareImage, uri: " + fileUri);

        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);

        if (TextUtils.isEmpty(targetPackage)) {
            startActivity(Intent.createChooser(intent, "Share text"));
        } else {
            if (checkAppInstall(targetPackage)) {
                intent.setPackage(targetPackage);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    ToastUtil.showToast(this, "Fail to share: " + e.getMessage());
                }
            } else {
                ToastUtil.showToast(this, targetPackage + " not installed");
            }
        }
    }

    private void shareImageWithComponent(String imagePath, ComponentName componentName) {
        if (!FileUtils.isFileExists(imagePath)) {
            ToastUtil.showToast(this, "Target image does not exist: " + imagePath);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri fileUri = getFileUri(imagePath);
        Log.d(TAG, "shareImage, uri: " + fileUri);

        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);

        if (componentName == null) {
            startActivity(Intent.createChooser(intent, "Share text"));
        } else {
            if (checkAppInstall(componentName.getPackageName())) {
                intent.setComponent(componentName);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    ToastUtil.showToast(this, "Fail to share: " + e.getMessage());
                }
            } else {
                ToastUtil.showToast(this, componentName.getPackageName() + " not installed");
            }
        }
    }

    private Uri getFileUri(String filePath) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(new File(filePath));
        } else {
            return FileProvider.getUriForFile(this, "xyz.dogold.andemos.sharing.fileprovider", new File(filePath));
        }
    }

    private void shareVideo(String videoPath, String targetPackage) {
        if (!FileUtils.isFileExists(videoPath)) {
            ToastUtil.showToast(this, "Target video does not exist: " + videoPath);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_STREAM, getFileUri(videoPath));

        if (TextUtils.isEmpty(targetPackage)) {
            startActivity(Intent.createChooser(intent, "Share text"));
        } else {
            if (checkAppInstall(targetPackage)) {
                intent.setPackage(targetPackage);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    ToastUtil.showToast(this, "Fail to share: " + e.getMessage());
                }
            } else {
                ToastUtil.showToast(this, targetPackage + " not installed");
            }
        }
    }

    private boolean checkAppInstall(String pkg) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
}
