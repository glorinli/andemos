package com.dogold.andemos.network;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.dogold.andemos.network.util.NetworkManager;
import com.dogold.andemos.network.util.NetworkManagerImpl;
import com.dogold.andemos.network.util.NetworkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import xyz.dogold.andemos.common.utils.ToastUtil;

public class MainActivity extends AppCompatActivity implements NetworkManager.NetworkStateListener {
    private static final String TAG = "MainActivity";

    @BindView(R.id.btnCheckVpn)
    Button btnCheckVpn;
    @BindView(R.id.tvIpAddress)
    TextView tvIpAddress;

    private Unbinder mUnbinder;

    private NetworkManager mNetworkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        mNetworkManager = new NetworkManagerImpl(this);

        mNetworkManager.addNetworkListener(this);

        updateIpAddresses();
    }

    private void updateIpAddresses() {
        tvIpAddress.setText("IpAddress:\n" + NetworkUtil.getIPAddresses());
    }

    @OnClick(R.id.btnCheckVpn)
    public void checkVpnClicked() {
        ToastUtil.showToast(this, "VPN activated: " + NetworkUtil.isVpnActivated());
    }

    @OnClick(R.id.refreshIpAddress)
    public void refreshIpAddressesClicked() {
        updateIpAddresses();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();

        mNetworkManager.removeNetworkListener(this);
    }

    @Override
    public void onNetworkStateChanged(boolean wifi, boolean mobile, int type) {
        Log.d(TAG, "onNetworkStateChanged, vpn: " + NetworkUtil.isVpnActivated());
    }
}
