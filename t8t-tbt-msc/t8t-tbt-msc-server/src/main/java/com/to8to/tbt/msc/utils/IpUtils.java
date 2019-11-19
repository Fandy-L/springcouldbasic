package com.to8to.tbt.msc.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @author pajero.quan
 */
@Slf4j
public class IpUtils {

    private static String hostIp = null;
    private static final String LOCALHOST = "127.0.0.1";

    public static String getHostIP() {
        if (hostIp != null) {
            return hostIp;
        }
        Enumeration<NetworkInterface> allNetInterfaces = null;
        String resultIP = null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            log.warn("获取本机ip", e);
        }
        InetAddress address;
        while (Objects.requireNonNull(allNetInterfaces).hasMoreElements()) {
            NetworkInterface netInterface = allNetInterfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            //取最后一个地址
            while (addresses.hasMoreElements()) {
                address = addresses.nextElement();
                if (address instanceof Inet4Address) {
                    String ip = address.getHostAddress();
                    if (!ip.equals(LOCALHOST)) {
                        resultIP = ip;
                    }
                }
            }
        }
        hostIp = resultIP;
        return hostIp;
    }
}
