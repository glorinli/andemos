package com.dogold.andemos.network.util;

import android.util.Log;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
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

    public static String getIPAddresses() {
        try {
            StringBuilder sb = new StringBuilder();
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();

                if (!networkInterface.getInetAddresses().hasMoreElements()) {
                    continue;
                }

                sb.append("+ NetworkInterface: ").append(networkInterface.getName()).append("\n");

                for (Enumeration<InetAddress> addr = networkInterface.getInetAddresses();
                     addr.hasMoreElements(); ) {
                    InetAddress inetAddress = addr.nextElement();

                    sb.append("    ")
                            .append(getType(inetAddress))
                            .append(": ")
                            .append(inetAddress.getHostAddress())
                            .append(inetAddress instanceof Inet4Address ? "(V4)" : "(V6)")
                            .append("\n");
                }

                sb.append("\n");
            }

            return sb.toString();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getType(InetAddress inetAddress) {
        if (inetAddress.isLoopbackAddress()) {
            return "LoopbackAddress";
        } else if (inetAddress.isAnyLocalAddress()) {
            return "AnyLocalAddress";
        } else if (inetAddress.isLinkLocalAddress()) {
            return "LinkLocalAddress";
        } else if (inetAddress.isMulticastAddress()) {
            return "MultiCast";
        } else if (inetAddress.isSiteLocalAddress()) {
            return "SiteLocal";
        }
        return "Other";
    }
}
