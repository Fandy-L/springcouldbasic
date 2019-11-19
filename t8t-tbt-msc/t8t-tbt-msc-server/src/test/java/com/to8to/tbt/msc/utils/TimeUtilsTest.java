package com.to8to.tbt.msc.utils;

import com.to8to.tbt.msc.BaseApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author juntao.guo
 */
@RunWith(value = SpringRunner.class)
public class TimeUtilsTest extends BaseApplication {

    @Test
    public void generateNatureWeekStartTime() {
        int timestamp = TimeUtils.generateNatureWeekStartTime();
        System.out.println(timestamp);
    }

    @Test
    public void getTimesWeekMorning() {
        int timestamp = TimeUtils.getTimesWeekMorning();
        System.out.println(timestamp);
    }
}
