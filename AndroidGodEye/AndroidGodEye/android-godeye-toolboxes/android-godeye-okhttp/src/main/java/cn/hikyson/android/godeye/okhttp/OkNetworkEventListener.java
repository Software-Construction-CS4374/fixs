package cn.hikyson.android.godeye.okhttp;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import cn.hikyson.godeye.core.GodEyeHelper;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfo;
import cn.hikyson.godeye.core.internal.modules.network.NetworkTime;
import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * network is success or not , network time for connection\send\receive...
 */
class OkNetworkEventListener extends EventListener {
    private NetworkInfo<HttpContent> mNetworkInfo;
    private long mCallStartTimeMillis;
    private long mDnsStartTimeMillis;
    private long mConnectionStartTimeMillis;
    private long mRequestHeadersStartTimeMillis;
    private long mRequestBodyStartTimeMillis;
    private long mResponseHeadersStartTimeMillis;
    private long mResponseBodyStartTimeMillis;
    private HttpContentTimeMapping mHttpContentTimeMapping;

    OkNetworkEventListener(final HttpContentTimeMapping httpContentTimeMapping) {
        this.mHttpContentTimeMapping = httpContentTimeMapping;
        this.mNetworkInfo = new NetworkInfo<>();
        this.mNetworkInfo.networkTime = new NetworkTime();
        this.mNetworkInfo.extraInfo = new HashMap<>();
    }

    @Override
    public void callStart(final Call call) {
        super.callStart(call);
        mCallStartTimeMillis = System.currentTimeMillis();
        this.mNetworkInfo.summary = call.request().method() + " " + call.request().url();
    }

    @Override
    public void dnsStart(final Call call,final String domainName) {
        super.dnsStart(call, domainName);
        mDnsStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void dnsEnd(final Call call,final String domainName,final List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        this.mNetworkInfo.networkTime.networkTimeMillisMap.put("DnsTime", System.currentTimeMillis() - mDnsStartTimeMillis);
    }

    @Override
    public void connectStart(final Call call,final InetSocketAddress inetSocketAddress,final Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        mConnectionStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void connectEnd(final Call call,final InetSocketAddress inetSocketAddress,final Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        this.mNetworkInfo.networkTime.networkTimeMillisMap.put("ConnectTime", System.currentTimeMillis() - mConnectionStartTimeMillis);
    }

    @Override
    public void connectFailed(final Call call,final InetSocketAddress inetSocketAddress,final Proxy proxy,final Protocol protocol,
                              IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        this.mNetworkInfo.networkTime.networkTimeMillisMap.put("ConnectTime", System.currentTimeMillis() - mConnectionStartTimeMillis);
    }

    @Override
    public void connectionAcquired(final Call call,final Connection connection) {
        super.connectionAcquired(call, connection);
        Handshake handshake = connection.handshake();
        String cipherSuite = "";
        String tlsVersion = "";
        if (handshake != null) {
            cipherSuite = handshake.cipherSuite().javaName();
            tlsVersion = handshake.tlsVersion().javaName();
        }
        Socket socket = connection.socket();
        int localPort = socket.getLocalPort();
        int remotePort = socket.getPort();
        String localIp = "";
        String remoteIp = "";
        InetAddress localAddress = socket.getLocalAddress();
        if (localAddress != null) {
            localIp = localAddress.getHostAddress();
        }
        InetAddress remoteAddress = socket.getInetAddress();
        if (remoteAddress != null) {
            remoteIp = remoteAddress.getHostAddress();
        }
        mNetworkInfo.extraInfo.put("cipherSuite", cipherSuite);
        mNetworkInfo.extraInfo.put("tlsVersion", tlsVersion);
        mNetworkInfo.extraInfo.put("localIp", localIp);
        mNetworkInfo.extraInfo.put("localPort", localPort);
        mNetworkInfo.extraInfo.put("remoteIp", remoteIp);
        mNetworkInfo.extraInfo.put("remotePort", remotePort);
    }

    @Override
    public void requestHeadersStart(final Call call) {
        super.requestHeadersStart(call);
        mRequestHeadersStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void requestHeadersEnd(final Call call,final Request request) {
        super.requestHeadersEnd(call, request);
        this.mNetworkInfo.networkTime.networkTimeMillisMap.put("RequestHeadersTime", System.currentTimeMillis() - mRequestHeadersStartTimeMillis);
    }

    @Override
    public void requestBodyStart(final Call call) {
        super.requestBodyStart(call);
        mRequestBodyStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void requestBodyEnd(final Call call,final long byteCount) {
        super.requestBodyEnd(call, byteCount);
        this.mNetworkInfo.networkTime.networkTimeMillisMap.put("RequestBodyTime", System.currentTimeMillis() - mRequestBodyStartTimeMillis);
    }

    @Override
    public void responseHeadersStart(final Call call) {
        super.responseHeadersStart(call);
        mResponseHeadersStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void responseHeadersEnd(final Call call,final Response response) {
        super.responseHeadersEnd(call, response);
        this.mNetworkInfo.networkTime.networkTimeMillisMap.put("ResponseHeadersTime", System.currentTimeMillis() - mResponseHeadersStartTimeMillis);
    }

    @Override
    public void responseBodyStart(final Call call) {
        super.responseBodyStart(call);
        mResponseBodyStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void responseBodyEnd(final Call call,final long byteCount) {
        super.responseBodyEnd(call, byteCount);
        this.mNetworkInfo.networkTime.networkTimeMillisMap.put("ResponseBodyTime", System.currentTimeMillis() - mResponseBodyStartTimeMillis);
    }

    @Override
    public void callEnd(final Call call) {
        super.callEnd(call);
        this.mNetworkInfo.networkTime.totalTimeMillis = System.currentTimeMillis() - mCallStartTimeMillis;
        mNetworkInfo.networkContent = mHttpContentTimeMapping.removeAndGetRecord(call);
        if (mNetworkInfo.networkContent != null) {
            if (mNetworkInfo.networkContent.httpResponse != null) {
                mNetworkInfo.isSuccessful = isSuccessful(mNetworkInfo.networkContent.httpResponse.code);
            }
            if (mNetworkInfo.networkContent.httpResponse != null) {
                mNetworkInfo.message = mNetworkInfo.networkContent.httpResponse.message;
            }
        }
        try {
            GodEyeHelper.onNetworkEnd(mNetworkInfo);
        } catch (UninstallException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callFailed(final Call call, IOException ioe) {
        super.callFailed(call, ioe);
        this.mNetworkInfo.networkTime.totalTimeMillis = System.currentTimeMillis() - mCallStartTimeMillis;
        mNetworkInfo.isSuccessful = false;
        mNetworkInfo.message = String.valueOf(ioe);
        mNetworkInfo.networkContent = mHttpContentTimeMapping.removeAndGetRecord(call);
        try {
            GodEyeHelper.onNetworkEnd(mNetworkInfo);
        } catch (UninstallException e) {
            e.printStackTrace();
        }
    }

    private static boolean isSuccessful(final int code) {
        return code >= 200 && code < 300;
    }
}
