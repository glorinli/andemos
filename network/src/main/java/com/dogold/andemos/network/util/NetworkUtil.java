package com.dogold.andemos.network.util;

import android.util.Log;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtil {
    private static final String TAG = "NetworkUtil";

    public static boolean isVpnActivated() {
        List<String> networkList = new ArrayList<>();
        try {
            final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            if (networkInterfaces != null)
                for (NetworkInterface networkInterface : Collections.list(networkInterfaces)) {
                    if (networkInterface.isUp())
                        networkList.add(networkInterface.getName());
                }
        } catch (Exception ex) {
            Log.e(TAG, "isVpnUsing Network List didn't received", ex);
        }

        Log.d(TAG, "networkList: " + networkList);

        return networkList.contains("tun0") || networkList.contains("ppp0");
    }
}
