package xyz.glorin.httpserverdemo;

public class PublicIpUtil {
    public static void getPublicIpV4(IpAddressCallback callback) {
        if (callback == null) {
            callback.onIpFetched("192.168.84.1");
        }
    }

    public interface IpAddressCallback {
        void onIpFetched(String ipAddress);
    }
}
