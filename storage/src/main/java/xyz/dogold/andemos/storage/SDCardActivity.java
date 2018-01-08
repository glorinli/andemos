package xyz.dogold.andemos.storage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.FileNotFoundException;
import java.io.OutputStream;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import xyz.dogold.andemos.common.utils.ToastUtil;
import xyz.dogold.andemos.storage.sdcard.SDCardHelper;

/**
 * SDCardActivity
 * Created by glorin on 08/01/2018.
 */

public class SDCardActivity extends AppCompatActivity {
    private static final String TAG = "SDCardActivity";
    private static final int REQUEST_SELECT_SD_CARD = 111;

    Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sd_card);

        mUnbinder = ButterKnife.bind(this);

        SDCardHelper.init(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_SD_CARD && resultCode == Activity.RESULT_OK) {
            SDCardHelper.handleSelectSDCardPathResult(this, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @OnClick({R.id.btnCheckSD, R.id.btnGetSDPath, R.id.btnSelectSDCard, R.id.btnDownloadFileToSDCard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCheckSD:
                break;
            case R.id.btnGetSDPath:
                break;
            case R.id.btnSelectSDCard:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(intent, REQUEST_SELECT_SD_CARD);
                break;
            case R.id.btnDownloadFileToSDCard:
                downloadFileToSDCard();
                break;
        }
    }

    private void downloadFileToSDCard() {
        String url = "https://www.baidu.com/img/bd_logo1.png";

        try {
            Uri dest = SDCardHelper.createFileOnSDCard(this, "bd_logo1.png");

            Ion.with(this)
                    .load(url)
                    .write(getContentResolver().openOutputStream(dest))
                    .setCallback(new FutureCallback<OutputStream>() {
                        @Override
                        public void onCompleted(Exception e, OutputStream result) {
                            if (e != null)
                                Log.e(TAG, "Error download file", e);
                            else if (!isDestroyed())
                                ToastUtil.showToast(SDCardActivity.this, "Download completed");

                            if (result != null) {
                                try {
                                    result.flush();
                                    result.close();
                                } catch (Exception e1) {
                                    Log.e(TAG, "Error close outputstream", e1);
                                }
                            }
                        }
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
