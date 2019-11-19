package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.BaseApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author juntao.guo
 */
@Slf4j
@RunWith(value = SpringRunner.class)
public class EncryptServiceTest extends BaseApplication {

    @Autowired
    private EncryptService encryptService;

    @Test
    public void getPhoneMap(){
        Set<String> phoneIds = new HashSet<>();
        phoneIds.add("8653691");
        Map<String, String> phoneMap = encryptService.getPhoneMap(phoneIds);
        log.debug("phoneMap:{}", phoneMap);
        for (String phoneId : phoneIds){
            String phone = phoneMap.getOrDefault(phoneId, null);
            Assert.assertTrue(phone != null);
        }
    }
}
