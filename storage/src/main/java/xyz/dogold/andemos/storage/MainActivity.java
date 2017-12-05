package xyz.dogold.andemos.storage;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvStorage = findViewById(R.id.tvStorageInfo);

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
}
