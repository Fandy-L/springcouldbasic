package com.to8to.tbt.msc.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author juntao.guo
 */
@Slf4j
public class ReflectUtils {
    /**
     * 属性拷贝
     * @param t
     * @param e
     * @param <T>
     * @param <E>
     */
    public static <T, E> E attrCopy(T t, E e) {
        Method[] tms = t.getClass().getDeclaredMethods();
        Method[] tes = e.getClass().getDeclaredMethods();
        for (Method m1 : tms) {
            if (m1.getName().startsWith("get")) {
                String mNameSubfix = m1.getName().substring(3);
                String forName = "set" + mNameSubfix;
                for (Method m2 : tes) {
                    if (m2.getName().equals(forName)) {
                        // 如果类型一致，或者m2的参数类型是m1的返回类型的父类或接口
                        boolean canContinue = m2.getParameterTypes()[0].isAssignableFrom(m1.getReturnType()) ||
                                ("java.lang.Integer".equals(m2.getParameterTypes()[0].getName()) && "int".equals(m1.getReturnType().getName())) ||
                                ("int".equals(m2.getParameterTypes()[0].getName()) && "java.lang.Integer".equals(m1.getReturnType().getName())) ||
                                ("long".equals(m2.getParameterTypes()[0].getName()) && "java.lang.Long".equals(m1.getReturnType().getName())) ||
                                ("java.lang.Long".equals(m2.getParameterTypes()[0].getName()) && "long".equals(m1.getReturnType().getName()));
                        if (canContinue) {
                            try {
                                if (m1.invoke(t) != null){
                                    m2.invoke(e, m1.invoke(t));
                                }
                                break;
                            } catch (Exception exception) {
                                log.warn("attrCopy fail sourceName:{} targetName:{} source:{} target:{} exception:{}", forName, m2.getName(), t, e, exception);
                            }
                        }
                    }
                }
            }
        }
        return e;
    }

    /**
     * 对象转换
     *
     * @param target
     * @param object
     * @param <T>
     * @return
     */
    public static <T> T convert(T target, Object object){
        try {
            String content = JSONObject.toJSONString(object);
            JSONObject source = JSONObject.parseObject(content);
            target = (T) JSONObject.toJavaObject(source, target.getClass());
        }catch (Exception e){
            log.warn("ReflectUtils fail target:{} source:{} e:{}", target, object, e);
        }
        return target;
    }
}
