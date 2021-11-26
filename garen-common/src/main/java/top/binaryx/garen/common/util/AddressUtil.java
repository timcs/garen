package top.binaryx.garen.common.util;

import top.binaryx.garen.common.enums.MessageEnum;
import top.binaryx.garen.common.exception.GarenException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * TODO
 *
 * @author weihongtian
 * @version v0.1 2019-09-17 11:23 weihongtian Exp $
 */
public class AddressUtil {

    private static volatile String cachedIpAddress;

    public static String getLocalHost() throws Exception {
        if (null != cachedIpAddress) {
            return cachedIpAddress;
        }

        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();

        while (netInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = netInterfaces.nextElement();
            Enumeration<InetAddress> ipAddresses = netInterface.getInetAddresses();
            while (ipAddresses.hasMoreElements()) {
                InetAddress ipAddress = ipAddresses.nextElement();
                if (isLocalIpAddress(ipAddress)) {
                    cachedIpAddress = ipAddress.getHostAddress();
                    return cachedIpAddress;
                }
            }
        }
        throw new GarenException(MessageEnum.GET_IP_FAIL);
    }

    private static boolean isPublicIpAddress(final InetAddress ipAddress) {
        return !ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !isV6IpAddress(ipAddress);
    }

    private static boolean isLocalIpAddress(final InetAddress ipAddress) {
        return ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !isV6IpAddress(ipAddress);
    }

    private static boolean isV6IpAddress(final InetAddress ipAddress) {
        return ipAddress.getHostAddress().contains(":");
    }
}
