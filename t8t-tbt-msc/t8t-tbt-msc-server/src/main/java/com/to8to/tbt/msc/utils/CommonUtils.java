package com.to8to.tbt.msc.utils;

import com.google.common.base.Strings;

import com.alibaba.fastjson.JSONObject;

import com.to8to.tbt.msc.configuration.MsgCenterConfiguration;
import com.to8to.tbt.msc.constant.CityIdConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.common.base.Joiner;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Collection;

import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

/**
 * @author juntao.guo
 */
@Slf4j
public class CommonUtils {
    public static final int REPORT_SUCC_TYPE = 1;

    public static final int REPORT_FAIL_TYPE = 1;

    public static final String[] REPORT_SUCC_KEYS = {"", "biz_msgc_mwyx_tmy_succ",
            "biz_msgc_mwyx_to8to_succ", "biz_msgc_mwsc_tmy_succ", "biz_msgc_mwsc_to8to_succ",
            "biz_msgc_mwyzm_tmy_succ", "biz_msgc_mwyzm_to8to_succ"};

    public static final String[] REPORT_FAIL_KEYS = {"", "biz_msgc_mwyx_tmy_fail",
            "biz_msgc_mwyx_to8to_fail", "biz_msgc_mwsc_tmy_fail", "biz_msgc_mwsc_to8to_fail",
            "biz_msgc_mwyzm_tmy_fail", "biz_msgc_mwyzm_to8to_fail"};

    private static final String CORPID = "wx640535834e2c4cb8";

    private static final String CORPSECRET = "PbXz5d0izL6YQFt-5GTSdpR2QhK_MQpxKQmBPsPz6ck9JY9Crkk28fFqGyUQLVAc";

    private static final String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

    private static final String SEND_MSG_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";

    private static final String WEIXIN_CREATE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=";

    private static final String LIST_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=";

    private static String access_token = null;

    private CommonUtils() {
    }

    /**
     * 获取access_token
     *
     * @return
     * @Description
     * @author wyp.wan
     */
    public static String getAccessToken() {
        String res = null;

        int count = 0;

        while (res == null && count < 3) {
            res = execGet(String.format(ACCESS_TOKEN_URL, CORPID, CORPSECRET), null);

            count++;
        }

        if (null == res || res.isEmpty()) {
            return null;
        }

        //access_token = "XlxWspZut1zdwdT4wXvUc03YJYdfBXBuxZ0p97I1pCG60G6yXLSyq0Xf4u1CPpbKP7ppt4nDBKPX3wh_A";
        return JSONObject.parseObject(res).getString("access_token");
    }

    /**
     * post请求微信服务
     *
     * @param requrl
     * @param params
     * @return
     * @Description
     * @author wyp.wan
     */
    public static String weixinPost(String requrl, JSONObject params) {
        if (null == access_token) {
            access_token = getAccessToken();
        }

        if (null == access_token || access_token.isEmpty()) {
            return null;
        }

        String result = post(requrl + access_token, params);

        if (null == result || result.isEmpty()) {
            return null;
        } else if (JSONObject.parseObject(result).getIntValue("errcode") == 40014) {
            access_token = getAccessToken();

            if (null == access_token || access_token.isEmpty()) {
                return null;
            }

            result = weixinPost(requrl + access_token, params);

            if (null == result || result.isEmpty()) {
                return null;
            } else {
                return result;
            }
        } else {
            return result;
        }

    }

    public static String post(String urlstr, JSONObject params) {
        String result = null;

        String json = JSONObject.toJSONString(params);

        URL url = null;

        try {
            url = new URL(urlstr);

            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("POST");

            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            http.setDoOutput(true);

            http.setDoInput(true);

            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒

            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒

            http.connect();

            OutputStream os = http.getOutputStream();

            os.write(json.getBytes("UTF-8"));// 传入参数

            InputStream is = http.getInputStream();

            int size = is.available();

            byte[] jsonBytes = new byte[size];

            is.read(jsonBytes);

            result = new String(jsonBytes, "UTF-8");

            System.out.println("请求返回结果:" + result);

            os.flush();

            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String execGet(String urlStr, JSONObject params) {
        String res = null;

        StringBuffer sb = new StringBuffer();

        if (null != params) {
            for (String key : params.keySet()) {
                sb.append("&");
                sb.append(key + "=" + params.get(key));
            }
        }


        try {
            HttpGet httpget = new HttpGet(urlStr + sb.toString());

            org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

            HttpEntity entity = httpclient.execute(httpget).getEntity();

            if (entity != null && entity.getContentLength() != -1) {
                res = EntityUtils.toString(entity);
            }

            httpclient.getConnectionManager().shutdown();

        } catch (Exception e) {
            log.error("执行get请求出错 {}", e);
        }
        return res;
    }

    public static String md5(String input) {

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
                'E', 'F'};
        try {
            byte[] btInput = input.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.error("错误信息 {}", e);
            return null;
        }
    }

    public static int getDayTimePoint(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(DAY_OF_YEAR, days);
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * 获取当前时间，单位：秒
     */
    public static int getCurrentSecond() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * Collection转字符串，默认以逗号分割。简单的对象的集合可以使用该方法。
     */
    public static <T> String collect2Str(Collection<T> collect) {
        return collect2Str(collect, ",");
    }

    /**
     * Collection转字符串。简单的对象的集合可以使用该方法。
     *
     * @param collect   集合
     * @param separator 分隔符
     */
    public static <T> String collect2Str(Collection<T> collect, String separator) {
        Joiner joiner = Joiner.on(separator == null ? "," : separator);
        return joiner.join(collect);
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return codePoint == 0x0 || codePoint == 0x9 || codePoint == 0xA || codePoint == 0xD || codePoint >= 0x20 && codePoint <= 0xD7FF || codePoint >= 0xE000 && codePoint <= 0xFFFD;
    }

    /**
     * 过滤非utf8编码的文字，该方法已经压测，每秒近4千万
     *
     * @param text 可能带非utf8数据的字符串
     * @return 过滤掉非utf8的字符串
     */
    public static String filterEmoji(String text) {
        if (Strings.isNullOrEmpty(text)) {
            return text;
        }
        StringBuilder buf = null;
        int len = text.length();
        for (int i = 0; i < len; i++) {
            char codePoint = text.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(text.length());
                }
                buf.append(codePoint);
            }
        }
        if (buf == null) {
            return "";
        } else {
            if (buf.length() == len) {
                return text;
            } else {
                return buf.toString();
            }
        }
    }

    /**
     * 获取短信发送通道
     *
     * @param channel
     * @param ywId
     * @param ywType
     * @param zid
     * @param cityid
     * @param ispass
     * @return
     */
    public static int convertChannel(int channel, int ywId, int ywType, int zid, int cityid, int ispass) {
        channel = channel > -2 && channel < 7 ? channel : 0;

        if (channel > 0) {
            return channel;
        }

        int channelTag = convertChannelTag(ywId, ywType, zid, cityid, ispass);

        if (channelTag == 1 && channel == 0) {
            return MsgCenterConfiguration.MWYX_TO8TO;
        } else if (channelTag == 1 && channel == -1) {
            return MsgCenterConfiguration.MWSC_TO8TO;
        } else if (channelTag == 2 && channel == 0) {
            return MsgCenterConfiguration.MWYX_TMY;
        } else {
            return MsgCenterConfiguration.MWSC_TMY;
        }
    }

    /**
     * 获取短信通道
     *
     * @param ywId
     * @param ywType
     * @return 1土巴兔  2图满意
     * @Description
     * @author wyp.wan
     */
    public static int convertChannelTag(int ywId, int ywType, int zid, int cityidReq, int ispassReq) {
        //工模选择土巴兔通道
        if (ywType == 2 || ywId < 1) {
            return 1;
        }
        //业务id和zid全为空
        else if (ywId < 1 && zid < 1) {
            return 1;
        } else if (zid > 0) {
            //JSONObject bidInfo = DpitemService.getBidInfoByZid(zid);
            JSONObject bidInfo = null;

            if (bidInfo == null) {
                return 1;
            }

            String ocity = bidInfo.getString("ocity");

            if (StringUtils.isEmpty(ocity) || CityIdConstant.LUODI_CITYIDS_STR.contains(ocity)) {
                return 1;
            } else {
                return 2;
            }
        }

        //JSONObject item = DpitemService.getItemByYid(ywId);
        JSONObject item = null;

        if (item == null) {
            return 1;
        }

        String ptag = item.getString("ptag");
        int cityid = cityidReq > 0 ? cityidReq : item.getIntValue("cityid");
        int ispass = ispassReq > 0 ? ispassReq : item.getIntValue("ispass");
        if (!StringUtils.isEmpty(ptag) && ptag.startsWith("1001")) {
            return 2;
        } else if (ispass == 9) {
            return 1;
        } else if (CityIdConstant.LUODI_CITYIDS.contains(cityid)) {
            return 1;
        } else {
            return 2;
        }
    }
}
