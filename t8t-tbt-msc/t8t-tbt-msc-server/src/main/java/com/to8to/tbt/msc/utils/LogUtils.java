package com.to8to.tbt.msc.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author juntao.guo
 */
@Slf4j
public class LogUtils {

    public static final String PACKAGE_NAME = "com.to8to.tbt.msc";

    /**
     * 生成Market
     *
     * @return
     */
    public static String buildMarket() {
        String market = "undefined";
        try {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            if (stackTraceElements.length >= 1) {
                market = "";
                for (StackTraceElement stackTraceElement : stackTraceElements) {
                    boolean check = stackTraceElement.getClassName().startsWith(PACKAGE_NAME) && stackTraceElement.getClassName().indexOf("LogUtils") < 0 && stackTraceElement.getClassName().indexOf(PACKAGE_NAME + ".controller") < 0;
                    if (check) {
                        String methodName = stackTraceElement.getMethodName();
                        String limiter = "$";
                        int startLimiter = methodName.indexOf(limiter) + 1;
                        if (startLimiter >= 1) {
                            int endLimiter = methodName.indexOf(limiter, startLimiter);
                            if (endLimiter >= 0) {
                                methodName = methodName.substring(startLimiter, endLimiter);
                            } else {
                                methodName = methodName.substring(startLimiter);
                            }
                        }
                        if ("null".equals(methodName)) {
                            continue;
                        }
                        String fileName = stackTraceElement.getFileName().split("\\.")[0];
                        market = market.length() > 0 ? "=>>" + market : "";
                        market = String.format("%s:%s", fileName, methodName) + market;
                    }
                }
                market = StringUtils.isEmpty(market) ? market : market.substring(market.indexOf(":") + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return market;
    }

    /**
     * 打印ERROR日志
     *
     * @param template
     * @param args
     */
    public static void error(String template, Object... args) {
        template = buildTemplate(template);
        log.error(template, args);
    }

    /**
     * 打印WARN日志
     *
     * @param template
     * @param args
     */
    public static void warn(String template, Object... args) {
        template = buildTemplate(template);
        log.warn(template, args);
    }

    /**
     * 打印INFO日志
     *
     * @param template
     * @param args
     */
    public static void info(String template, Object... args) {
        template = buildTemplate(template);
        log.info(template, args);
    }

    /**
     * 打印DEBUG日志
     *
     * @param template
     * @param args
     */
    public static void debug(String template, Object... args) {
        template = buildTemplate(template);
        log.debug(template, args);
    }

    /**
     * 打印异常日志
     *
     * @param template
     * @param args
     */
    public static void exception(String template, Object... args) {
        template = buildTemplate(template + " exception");
        log.debug(template, args);
    }

    /**
     * 打印异常日志
     *
     * @param args
     */
    public static void exception(Object... args) {
        String template = buildTemplate("exception");
        log.debug(template, args);
    }

    /**
     * 生成日志模板
     *
     * @param template
     * @return
     */
    public static String buildTemplate(String template) {
        template = buildMarket() + "\n" + template + " ";
        return template.replace(" ", ":{}\n");
    }

    /**
     * 生成日志模板
     *
     * @return
     */
    public static String buildTemplate() {
        return buildMarket() + "\n";
    }

    /**
     * 生成异常日志的模板
     *
     * @param template
     * @return
     */
    public static String buildExceptionTemplate(String template) {
        template = buildTemplate(template + " exception");
        return template;
    }

    /**
     * 生成异常日志的模板
     *
     * @return
     */
    public static String buildExceptionTemplate() {
        String template = buildTemplate("exception");
        return template;
    }
}
