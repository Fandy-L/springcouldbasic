package com.to8to.tbt.msc.utils;

import com.google.common.base.Strings;

/**
 * @author juntao.guo
 */
public class TextUtils {

    /**
     * 分割字符串为整形数组
     * @param text
     * @param regex
     * @return
     */
    public static int[] splitToInt(String text, String regex){
        String[] textList = text.split(regex);
        int[] values = new int[textList.length];
        for (int i = 0;i < textList.length;i++){
            values[i] = Integer.parseInt(textList[i]);
        }
        return values;
    }
}
