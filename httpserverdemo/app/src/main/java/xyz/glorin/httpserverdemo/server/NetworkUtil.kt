package xyz.glorin.httpserverdemo.server

import android.util.Log
import java.lang.Exception
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface

object NetworkUtil {
    private const val TAG = "NetworkUtil"
    fun getIpV4Address(): String? {
        return getInetAddress(Inet4Address::class.java)?.firstOrNull()?.hostAddress
    }

    fun getIpV6Address(): String? {
        getInetAddress(Inet6Address::class.java)?.firstOrNull { !it.isLinkLocalAddress && !it.isSiteLocalAddress }
            ?.hostAddress?.let {
                val delim = it.indexOf('%')
                return if (delim < 0) it else it.substring(0, delim)
            }

        return null
    }

    private fun getInetAddress(clazz: Class<out InetAddress>): List<InetAddress>? {
        val result = mutableListOf<InetAddress>()
        try {
            NetworkInterface.getNetworkInterfaces().iterator().forEach { networkInterface ->
                networkInterface.inetAddresses.iterator().forEach {
                    if (!it.isLoopbackAddress) {
                        if (it::class.java == clazz) {
                            result.add(it)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting address of ${clazz.simpleName}", e)
        }

        return result
    }
}