package com.to8to.tbt.msc.utils;

import com.to8to.sc.component.redis.operation.AbstractDefaultOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author juntao.guo
 */
public class RedisUtils {

    @Autowired
    public static AbstractDefaultOperation<String> abstractDefaultOperation;


}
