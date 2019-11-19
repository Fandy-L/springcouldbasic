package com.to8to.tbt.msc.utils;

import com.to8to.sc.compatible.RPCException;
import com.to8to.sc.compatible.Status;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * @author edmund.yu
 */

public class Preconditions {

    public static String checkStringNotEmpty(String str, Status status){
        if (StringUtils.isEmpty(str)){
            throw new RPCException(status);
        }else {
            return str;
        }
    }

    public static String checkStringNotEmpty(String str){
        return checkStringNotEmpty(str, MyExceptionStatus.PARAMS_CONTAINS_NULL);
    }

    public static Collection checkCollectionSize(Collection collection, Status status, int minSize){
        checkNotNull(collection, MyExceptionStatus.PARAMS_CONTAINS_NULL);
        if (collection.size() < minSize){
            throw new RPCException(status);
        }else {
            return collection;
        }
    }

    public static Collection checkCollectionSizeGte(Collection collection, int minSize){
        return checkCollectionSize(collection, MyExceptionStatus.PARAMS_LENGTH_INVALID, minSize);
    }

    public static Object checkNotNull(Object o, Status status){
        if (o == null){
            throw new RPCException(status);
        }else {
            return o;
        }
    }

    public static void checkArgument(boolean expression, MyExceptionStatus status, String errorMessage) {
        if (!expression) {
            if (status != null) {
                throw new RPCException(status, errorMessage);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void checkArgument(boolean expression, MyExceptionStatus status) {
        if (!expression) {
            if (status != null) {
                throw new RPCException(status);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}
