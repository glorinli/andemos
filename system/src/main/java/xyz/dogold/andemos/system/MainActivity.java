package xyz.dogold.andemos.system;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import xyz.dogold.andemos.common.reflect.Reflect;
import xyz.dogold.andemos.common.utils.ToastUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
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

    @OnClick({R.id.btnGetThetheringStatus, R.id.btnGetWifiStatus})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnGetThetheringStatus:
                getThetheringStatus();
                break;
            case R.id.btnGetWifiStatus:
                getWifiStatus();
                break;
        }
    }

    private void getWifiStatus() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        WifiInfo connectionInfo = wifiManager.getConnectionInfo();

        if (connectionInfo != null) {
            ToastUtil.showToast(this, "Wifi ssid: " + connectionInfo.getSSID());
        } else {
            ToastUtil.showToast(this, "Cannot get wifi ssid");
        }
    }

    private void getThetheringStatus() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        try {
            boolean isWifiApEnabled = Reflect.on(wifiManager).call("isWifiApEnabled").get();

            ToastUtil.showToast(this, "Wifi AP enabled: " + isWifiApEnabled);
        } catch (Exception e) {
            ToastUtil.showToast(this, "Error get wifi ap status: " + e.getMessage());

            Log.e(TAG, "Error get thether status", e);
        }
    }
}
