package com.to8to.tbt.msc.utils;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author juntao.guo
 */
@Component
@Slf4j
public class HttpUtils {
    /**
     * 读取超时时间
     */
    @Value("${socket.time.out:500}")
    private int soTimeout;

    /**
     * 连接超时时间
     */
    @Value("${connection.time.out:2000}")
    private int connectionTimeout;

    /**
     * 连接请求超时时间
     */
    private int connectionRequestTimeout = 10000;

    public String doGet(String url) throws IOException {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result;
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout)
                    .setConnectionRequestTimeout(connectionRequestTimeout)
                    .setSocketTimeout(soTimeout)
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);

            if (response.getStatusLine().getStatusCode() == 200) {
                return result;
            } else {
                log.error("HttpGetException http-get,request={}，状态码:{},内容：{}", url, response.getStatusLine().getStatusCode(), result);
                return null;
            }
        } catch (Exception e) {
            log.warn("HttpGetException http-get执行时出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
            throw e;
        } finally {
            // 关闭资源
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("HttpGetException http-get关闭response出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
                    throw e;
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("HttpGetException http-get关闭httpClient出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
                    throw e;
                }
            }
        }
    }

    public String doPost(String url, Map<String, Object> paramMap) throws IOException {
        CloseableHttpClient httpClient;
        CloseableHttpResponse httpResponse = null;
        String result;
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(soTimeout)
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        // 封装post请求参数
        if (null != paramMap && paramMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 通过map集成entrySet方法获取entity
            Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
            // 循环遍历，获取迭代器
            Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> mapEntry = iterator.next();
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }

            // 为httpPost设置封装好的请求参数
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (Exception e) {
            log.error("HttpPostException http-post执行时出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
            throw e;
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    log.error("HttpPostException http-post关闭httpResponse出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
                    throw e;
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("HttpPostException http-post关闭httpClient出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
                    throw e;
                }
            }
        }
        return result;
    }
}
