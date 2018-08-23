package com.dogold.andemos.network.util;

/**
 * An interface manages network states
 * Created by glorin on 01/11/2016.
 */

public interface NetworkManager {
    int TYPE_NONE = -1;

    boolean isNetworkConnected();
    boolean isMobileDataConnected();
    boolean isWifiConnected();
    int getConnectedNetworkType();
    void addNetworkListener(NetworkStateListener l);
    void removeNetworkListener(NetworkStateListener l);

    interface NetworkStateListener {
        void onNetworkStateChanged(boolean wifi, boolean mobile, int type);
    }
}
