package com.to8to.tbt.msc.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author juntao.guo
 */
@RunWith(value = SpringRunner.class)
public class HelperTest {

    @Test
    public void getEnv(){
        String env = System.getenv("ENV");
        System.out.println(env);
    }

    @Test
    public void chatAt(){
        String phone = "15824909673";
        char selector = phone.charAt(phone.length() - 1);
        System.out.println(selector);
    }
}
