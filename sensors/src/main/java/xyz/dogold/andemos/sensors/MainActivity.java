package xyz.dogold.andemos.sensors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import xyz.dogold.andemos.common.utils.ActivityUtils;
import xyz.dogold.andemos.sensors.gyro.GyroActivity;

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

    @OnClick({R.id.btnGyroscope})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGyroscope:
                ActivityUtils.startActivity(this, GyroActivity.class);
                break;
        }
    }
}
