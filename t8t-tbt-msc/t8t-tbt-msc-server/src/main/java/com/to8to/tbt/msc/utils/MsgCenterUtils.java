package com.to8to.tbt.msc.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author juntao.guo
 */
@Slf4j
public class MsgCenterUtils {

    public static Pattern p1 = Pattern.compile("#([^#>;}]+)#");

    /**
     * 处理科学计数法
     *
     * @param num
     * @return
     */
    public static String getLongNum(String num) {
        if (StringUtils.isEmpty(num)) {
            return "0";
        }
        BigDecimal bd = new BigDecimal(num);
        return bd.toPlainString();
    }

    /**
     * 获取当时时间戳（10位）
     */
    public static long getTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * MD5加密
     *
     * @param input
     * @return
     */
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32
            // chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static String doGet(String url, String queryString, String charset, boolean pretty) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        try {
            if (StringUtils.isNotBlank(queryString)) {
                // 对get请求参数做了http请求默认编码，好像没有任何问题，汉字编码后，就成为%式样的字符串
                method.setQueryString(URIUtil.encodeQuery(queryString));
            }
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        method.getResponseBodyAsStream(), charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty) {
                        response.append(line).append(System.getProperty("line.separator"));
                    } else {
                        response.append(line);
                    }
                }
                reader.close();
            }
        } catch (URIException e) {
            log.error("执行HTTP Get请求时，编码查询字符串“{}, 发生异常:{}", queryString, e);
        } catch (IOException e) {
            log.error("执行HTTP Get请求url:{}时，发生异常！{}", url, e);
        } finally {
            method.releaseConnection();
        }
        return response.toString();
    }

    public static String doPost(String url, Map<String, String> params) {
        String response = null;
        HttpClient client = new HttpClient();
        HttpMethod method = new PostMethod(url);
        // 设置Http Post数据
        if (params != null) {
            HttpMethodParams p = new HttpMethodParams();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                p.setParameter(entry.getKey(), entry.getValue());
            }
            method.setParams(p);
        }
        try {
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                response = method.getResponseBodyAsString();
            }
        } catch (IOException e) {
            log.error("执行HTTP Post请求" + url + "时，发生异常！", e);
        } finally {
            method.releaseConnection();
        }

        return response;
    }

    @SuppressWarnings("unused")
    private static String prepareParam(Map<String, Object> paramMap) {
        StringBuffer sb = new StringBuffer();
        if (paramMap.isEmpty()) {
            return "";
        } else {
            for (String key : paramMap.keySet()) {
                String value = (String) paramMap.get(key);
                if (sb.length() < 1) {
                    sb.append(key).append("=").append(value);
                } else {
                    sb.append("&").append(key).append("=").append(value);
                }
            }
            return sb.toString();
        }
    }

    public static String postForm(String url, JSONObject json) {

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();

        for (String s : json.keySet()) {
            formParams.add(new BasicNameValuePair(s, json.getString(s)));
        }

        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(url);

        UrlEncodedFormEntity urlEncodedFormEntity;

        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
            HttpResponse httpResponse = null;
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String content = EntityUtils.toString(httpEntity, "UTF-8");
                return content;
            }
        } catch (ClientProtocolException e) {
            log.error("postForm exception e:{}", e);
        } catch (UnsupportedEncodingException e) {
            log.error("postForm exception e:{}", e);
        } catch (IOException e) {
            log.error("postForm exception e:{}", e);
        } finally {
            // 关闭连接，释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return null;

    }

    /**
     * 替换参数
     *
     * @param list
     * @param s
     * @return
     */
    public static String replaceParam(List<String> list, String s) {

        List<String> arglist = new LinkedList<String>();


        Matcher m1 = p1.matcher(s);

        while (m1.find()) {
            String old = m1.group();
            arglist.add(old);
        }

        if (list == null || arglist == null || arglist.isEmpty()) {
            return s;
        }

        if (arglist.size() != list.size()) {
            log.warn("参数个数不匹配! 参数名列表:{}, 参数列表:{}", arglist, list);
        }

        int size = Math.min(arglist.size(), list.size());

        for (int j = 0; j < size; ++j) {
            s = s.replace(arglist.get(j), list.get(j));
        }
        return s;
    }
}
