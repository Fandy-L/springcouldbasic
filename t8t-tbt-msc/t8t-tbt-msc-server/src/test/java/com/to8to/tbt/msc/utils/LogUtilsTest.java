package com.to8to.tbt.msc.utils;

import com.to8to.tbt.msc.dto.TemplateMsgDTO;
import org.junit.Test;

/**
 * @author juntao.guo
 */
public class LogUtilsTest {

    @Test
    public void info(){
        LogUtils.exception(new TemplateMsgDTO());
    }
}
