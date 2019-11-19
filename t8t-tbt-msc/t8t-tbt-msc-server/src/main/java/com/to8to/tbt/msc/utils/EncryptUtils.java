package com.to8to.tbt.msc.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author juntao.guo
 */
@Slf4j
public class EncryptUtils {

    private static final String KEY0 = "JASONYU()*&<MNCXZPKL";
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static byte[] keyBytes = KEY0.getBytes(CHARSET);

    /**
     * 简单异或运算加密
     * @param enc
     * @return
     */
    public static String simpleEncode(String enc) {
        byte[] b = enc.getBytes();
        int size = b.length;
        for (int i = 0; i < b.length; i++) {
            for (byte keyByte : keyBytes) {
                b[i] = (byte) (b[i] ^ keyByte);
            }
        }
        return new String(b);
    }

    /**
     * 简单异或运算解密
     * @param dec
     * @return
     */
    public static String simpleDecode(String dec) {
        byte[] e = dec.getBytes(CHARSET);
        int size = e.length;
        byte[] dee = e;
        for (int i = 0; i < size; i++) {
            for (byte keyByte : keyBytes) {
                e[i] = (byte) (dee[i] ^ keyByte);
            }
        }
        return new String(e);
    }

    /**
     * 通用加密方法
     * @param str
     * @param type
     * @return
     */
    public static String encrypt(String str, String type) {
        MessageDigest md;
        String strDes;
        byte[] bt = str.getBytes();

        try {
            md = MessageDigest.getInstance(type);
            md.update(bt);
            strDes = byte2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            log.warn("加密失败！str:{},type:{},e:{}", str, type, e);
            return null;
        }
        return strDes;
    }

    /**
     * Base64加密
     * @param content
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encryptBase64(String content) throws UnsupportedEncodingException {
        String strBase64Msg = Base64.getEncoder().encodeToString(content.getBytes("GBK"));
        return strBase64Msg;
    }

    private static String byte2Hex(byte[] bts) {
        String des = "";
        String tmp;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0XFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
