package com.dogold.andemos.network.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * NetworkManagerImpl
 * Created by glorin on 01/11/2016.
 */

public class NetworkManagerImpl implements NetworkManager {
    private static final String TAG = "NetworkManagerImpl";

    private ConnectivityManager mCm;
    private Context mApplicationContext;
    private boolean mMobileConnected;
    private boolean mWifiConnected;
    private int mCurrentNetworkType;
    private BroadcastReceiver mNetworkStateReceiver;
    private boolean mStatesInitilized;

    private List<WeakReference<NetworkStateListener>> mStateListeners;

    public NetworkManagerImpl(Context context) {
        Log.d(TAG, "New instance");

        mApplicationContext = context.getApplicationContext();
        mCm = (ConnectivityManager) mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        mNetworkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                    updateNetworkStatus();
                    mStatesInitilized = true;
                }
            }
        };

        mStatesInitilized = false;

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mApplicationContext.registerReceiver(mNetworkStateReceiver, filter);
    }

    @Override
    public boolean isNetworkConnected() {
//        return mMobileConnected || mWifiConnected;
        NetworkInfo activeNetworkInfo = mCm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean isMobileDataConnected() {
        return mMobileConnected;
    }

    @Override
    public boolean isWifiConnected() {
        return mWifiConnected;
    }

    @Override
    public int getConnectedNetworkType() {
        return mCurrentNetworkType;
    }

    @Override
    public void addNetworkListener(NetworkStateListener l) {
        if (mStateListeners == null) {
            mStateListeners = new ArrayList<>();
        }

        Iterator<WeakReference<NetworkStateListener>> iterator = mStateListeners.iterator();

        boolean exists = false;

        if (iterator.hasNext()) {
            // Remove item if it is unavailable
            WeakReference<NetworkStateListener> weakReference = iterator.next();
            if (weakReference.get() == null) {
                iterator.remove();
            } else {
                if (weakReference.get() == l) {
                    exists = true;
                }
            }
        }

        if (!exists) {
            mStateListeners.add(new WeakReference<>(l));

            // If we have got data, publish it to the new added listener
            if (mStatesInitilized)
                l.onNetworkStateChanged(mWifiConnected, mMobileConnected, mCurrentNetworkType);
        }
    }

    @Override
    public void removeNetworkListener(NetworkStateListener l) {
        if (mStateListeners != null) {
            Iterator<WeakReference<NetworkStateListener>> iterator = mStateListeners.iterator();
            if (iterator.hasNext()) {
                // Remove item if it is unavailable
                WeakReference<NetworkStateListener> weakReference = iterator.next();
                if (weakReference.get() == l) {
                    iterator.remove();
                }
            }
        }
    }

    private void updateNetworkStatus() {
        updateWifiAndMobileConnectStatus();

        NetworkInfo connected = mCm.getActiveNetworkInfo();
        mCurrentNetworkType = connected != null && connected.isConnected() ? connected.getType() : TYPE_NONE;

        Log.d(TAG, String.format("updateNetworkStatus, wifi: %s, mobile %s, type: %d",
                mWifiConnected, mMobileConnected, mCurrentNetworkType));

        if (mStateListeners != null) {
            for (WeakReference<NetworkStateListener> w : mStateListeners) {
                NetworkStateListener networkStateListener = w.get();
                if (networkStateListener != null) {
                    networkStateListener.onNetworkStateChanged(mWifiConnected, mMobileConnected, mCurrentNetworkType);
                }
            }
        }
    }

    private void updateWifiAndMobileConnectStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = mCm.getAllNetworks();

            mWifiConnected = mMobileConnected = false;

            if (networks != null) {
                for (Network n : networks) {
                    NetworkInfo i = mCm.getNetworkInfo(n);
                    resolveNetworkInfo(i);
                }
            }
        } else {
            NetworkInfo[] allNetworkInfo = mCm.getAllNetworkInfo();

            for (NetworkInfo ni : allNetworkInfo) {
                resolveNetworkInfo(ni);
            }
        }
    }

    private void resolveNetworkInfo(NetworkInfo i) {
        // Wifi
        if (i != null && i.getType() == ConnectivityManager.TYPE_WIFI && i.isConnected()) {
            mWifiConnected = true;
        }

        // Mobile
        if (i != null && i.getType() == ConnectivityManager.TYPE_MOBILE && i.isConnected()) {
            mMobileConnected = true;
        }
    }
}
