package li.glorin.andemos.ijkplayerdemo;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final int REQUEST_GET_VIDEOS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnSelectVideo).setOnClickListener(this);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void chooseVideo() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
            startActivityForResult(intent, REQUEST_GET_VIDEOS);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No gallery apps found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSelectVideo) {
            MainActivityPermissionsDispatcher.chooseVideoWithPermissionCheck(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GET_VIDEOS) {
            if (resultCode == Activity.RESULT_OK) {
                final Uri uri = data.getData();

                if (uri != null) {
                    PlayerActivity.start(this, uri);
                }
            }
        }
    }
}
