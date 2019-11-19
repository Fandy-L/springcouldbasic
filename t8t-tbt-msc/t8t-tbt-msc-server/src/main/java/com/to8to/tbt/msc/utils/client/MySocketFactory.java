package com.to8to.tbt.msc.utils.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;

/**
 * @author juntao.guo
 */
@Slf4j
public class MySocketFactory implements SchemeSocketFactory{
    private Socket socket;
    private int max = 60000;
    private int min = 50000;

    public MySocketFactory() {
    }

    @Override
    public Socket createSocket(HttpParams params) {
        return this.createSocket();
    }

    public Socket createSocket() {
        this.socket = new Socket();
        int port = 0;
        int count = 0;
        int t = 0;

        while(t < 1000) {
            int delta = this.max - this.min;
            port = RandomUtils.nextInt(100000) % delta + this.min;

            Logger var10000;
            try {
                this.socket.bind(new InetSocketAddress("0.0.0.0", port));
                var10000 = log;
                ++count;
                var10000.debug("尝试了{}次,地址绑定成功", count);
                break;
            } catch (Exception var6) {
                log.debug("createSocket exception e:{}", var6);
                var10000 = log;
                ++count;
                var10000.debug("尝试了{}次,地址绑定失败", count);
                ++t;
            }
        }

        return this.socket;
    }

    @Override
    public Socket connectSocket(Socket socket, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params) throws IOException, ConnectTimeoutException {
        if (remoteAddress == null) {
            throw new IllegalArgumentException("Remote address may not be null");
        } else if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            Socket sock = socket;
            if (socket == null) {
                sock = this.createSocket();
            }

            if (localAddress != null) {
                sock.setReuseAddress(HttpConnectionParams.getSoReuseaddr(params));
                sock.bind(localAddress);
            }

            int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
            int soTimeout = HttpConnectionParams.getSoTimeout(params);

            try {
                sock.setSoTimeout(soTimeout);
                sock.connect(remoteAddress, connTimeout);
                return sock;
            } catch (SocketTimeoutException var9) {
                throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
            }
        }
    }

    @Override
    public final boolean isSecure(Socket sock) throws IllegalArgumentException {
        if (sock == null) {
            throw new IllegalArgumentException("Socket may not be null.");
        } else if (sock.isClosed()) {
            throw new IllegalArgumentException("Socket is closed.");
        } else {
            return false;
        }
    }

    public Socket getSocket() {
        return this.socket;
    }
}
