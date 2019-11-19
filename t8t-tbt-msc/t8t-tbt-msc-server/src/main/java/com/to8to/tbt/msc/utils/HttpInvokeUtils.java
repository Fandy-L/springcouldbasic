package com.to8to.tbt.msc.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author juntao.guo
 */
@Slf4j
public class HttpInvokeUtils {
    public static String post(String urlstr, JSONObject params) throws Exception {
        String result = null;

        String json = JSONObject.toJSONString(params);


        URL url = new URL(urlstr);

        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        http.setRequestMethod("POST");

        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        http.setDoOutput(true);

        http.setDoInput(true);

        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");

        System.setProperty("sun.net.client.defaultReadTimeout", "30000");

        http.connect();

        OutputStream os = http.getOutputStream();

        os.write(json.getBytes("UTF-8"));// 传入参数

        InputStream is = http.getInputStream();

        int size = is.available();

        byte[] jsonBytes = new byte[size];

        is.read(jsonBytes);

        result = new String(jsonBytes, "UTF-8");

        log.info("Post请求参数:{},返回结果:{}", params.toJSONString(), result);

        os.flush();

        os.close();

        return result;
    }

    public static String get(JSONObject params, String urlstr) throws IOException {
        String result = "";
        StringBuffer buffer = new StringBuffer();
        for (String param : params.keySet()) {
            buffer.append("&");
            buffer.append(param);
            buffer.append("=");
            if ("multixmt".equals(param)) {
                JSONObject multixmt = params.getJSONObject("multixmt");
                buffer.append((URLEncoder.encode(multixmt.getString("base"), "UTF-8")));
                buffer.append((URLEncoder.encode("|", "UTF-8")));
                buffer.append(multixmt.getString("phone"));
                buffer.append((URLEncoder.encode("|", "UTF-8")));
                buffer.append((URLEncoder.encode(multixmt.getString("content"), "UTF-8")));
                buffer.append((URLEncoder.encode("|||||||||1", "UTF-8")));
            } else {
                buffer.append((URLEncoder.encode(params.getString(param), "UTF-8")));
            }
        }
        String paramStr = buffer.toString().replaceFirst("&", "?");
        urlstr = urlstr + paramStr;
        HttpGet httpget = new HttpGet(urlstr);
        httpget.setHeader("Connection", "Keep-Alive");
        HttpClient httpclient = new DefaultHttpClient();
        HttpEntity entity = httpclient.execute(httpget).getEntity();
        if (entity != null && entity.getContentLength() != -1) {
            result = EntityUtils.toString(entity);
        }
        return result;

    }
}
