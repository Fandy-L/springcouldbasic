package com.to8to.tbt.msc.utils;

import com.to8to.tbt.msc.enumeration.BooleanEnum;

/**
 * @author pajero.quan
 */
public class BoolUtils {
    public static Byte toByte(Boolean boolValue) {
        if (boolValue == null) {
            return BooleanEnum.FALSE.getValue();
        }
        return boolValue ? BooleanEnum.TRUE.getValue() : BooleanEnum.FALSE.getValue();
    }

    public static boolean toBoolean(Byte value) {
        return BooleanEnum.TRUE.getValue().equals(value);
    }
}
