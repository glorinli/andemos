package xyz.dogold.andemos.sensors.gyro;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import xyz.dogold.andemos.sensors.R;

public class GyroActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Unbinder mUnbinder;

    @BindView(R.id.tvGyroData)
    TextView tvGyroData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyro);

        mUnbinder = ButterKnife.bind(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float angleSpeedX = (float) Math.toDegrees(sensorEvent.values[0]);
            float angleSpeedY = (float) Math.toDegrees(sensorEvent.values[1]);
            float angleSpeedZ = (float) Math.toDegrees(sensorEvent.values[2]);

            tvGyroData.setText(String.format("Gyro data:\nX: %1$f\nY: %2$f\nZ: %3$f", angleSpeedX, angleSpeedY, angleSpeedZ));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
