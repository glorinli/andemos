package xyz.dogold.andemos.storage;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import xyz.dogold.andemos.common.utils.ActivityUtils;

public class MainActivity extends AppCompatActivity {
    private Unbinder mUnbinder;

    @BindView(R.id.tvStorageInfo)
    TextView tvStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUnbinder = ButterKnife.bind(this);

        showStorageInfos();
    }

    private void showStorageInfos() {
        StringBuilder sb = new StringBuilder("Storage info:\n\n");

        sb.append("getFilesDir: ").append(getFilesDir()).append("\n\n");

        sb.append("getCacheDir: ").append(getCacheDir()).append("\n\n");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sb.append("getDataDir: ").append(getDataDir()).append("\n\n");
        }

        sb.append("getExternalFilesDir(\"haha\"): ").append(getExternalFilesDir("haha")).append("\n\n");

        sb.append("Environment.getExternalStorageDirectory: ").append(Environment.getExternalStorageDirectory()).append("\n\n");

        sb.append("getExternalFilesDirManually: ").append(Environment.getExternalStorageDirectory() +
                "/Android/data/" + getPackageName() + "/files").append("\n\n");

        tvStorage.setText(sb);
    }

    @OnClick({R.id.btnSDCardActivity})
    void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnSDCardActivity:
                ActivityUtils.startActivity(this, SDCardActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
