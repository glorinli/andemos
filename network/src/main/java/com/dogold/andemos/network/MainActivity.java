package com.dogold.andemos.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.dogold.andemos.network.util.NetworkManagerImpl;
import com.dogold.andemos.network.util.NetworkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import xyz.dogold.andemos.common.utils.ToastUtil;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnCheckVpn)
    Button btnCheckVpn;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        new NetworkManagerImpl(this);
    }

    @OnClick(R.id.btnCheckVpn)
    public void checkVpnClicked() {
        ToastUtil.showToast(this, "VPN activated: " + NetworkUtil.isVpnActivated());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();
    }
}
