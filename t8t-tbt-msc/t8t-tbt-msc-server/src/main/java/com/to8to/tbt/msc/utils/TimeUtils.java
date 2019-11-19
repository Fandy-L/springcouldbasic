package com.to8to.tbt.msc.utils;

import com.to8to.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;
import static java.util.Calendar.SECOND;

/**
 * @author yason.li
 */
@Slf4j
public class TimeUtils {

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static int getCurrentTimestamp() {
        Long timestamp = System.currentTimeMillis() / 1000;
        return timestamp.intValue();
    }

    /**
     * 获取当前时间戳-长整型
     *
     * @return
     */
    public static long getCurrentTimestampLong() {
        Long timestamp = System.currentTimeMillis() / 1000;
        return timestamp.longValue();
    }

    /**
     * 格式化日期
     *
     * @param timestamp
     * @return
     */
    public static String timestampToDate(int timestamp) {
        if (timestamp > 0) {
            int startTime = new Long(DateUtils.getDayStartTime()).intValue();
            if (timestamp > startTime) {
                return DateUtils.format(new Date((timestamp * 1000L)), "HH:mm");
            } else {
                return DateUtils.format(new Date((timestamp * 1000L)), "MM-dd");
            }
        }
        return "";
    }

    /**
     * 生成自然周的起始时间
     *
     * @return
     */
    public static int generateNatureWeekStartTime() {
        return getDayTimePoint(-7);
    }

    /**
     * 计算未来或过去某天的时间
     *
     * @param days
     * @return
     */
    public static int getDayTimePoint(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(DAY_OF_YEAR, days);
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * 得到本周周一0点时间戳
     *
     * @return
     */
    public static int getTimesWeekMorning() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return (int) (calendar.getTimeInMillis() / 1000);
    }

    /**
     * 本周日24点时间
     *
     * @return
     */
    public static int getTimesWeekNight() {
        return getTimesWeekMorning() + 7 * 86400;
    }


    /**
     * 得到当天0点时间戳
     *
     * @return
     */
    public static int getTimesDayMorning() {
        return getDayTimePoint(0);
    }

    /**
     * 当天24点时间戳
     *
     * @return
     */
    public static int getTimesDayNight() {
        return getTimesDayMorning() + 86400;
    }
}
