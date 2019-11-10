/*    */ package com.chungke.provider018081.common.util;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RuntimePropertiesUtils
/*    */ {
/* 17 */   private static final Logger log = LoggerFactory.getLogger(RuntimePropertiesUtils.class);
/*    */   private static final String SPRING_CONTEXT_HOLDER = "com.to8to.sc.spring.SpringContextHolder";
/*    */   private static final String ENV_CLZ = "org.springframework.core.env.Environment";
/*    */   private static final String METHOD_NAME = "getBean";
/*    */   private static final String SPRING_PROP_METHOD_NAME = "getProperty";
/*    */   private static Method springPropMethod;
/*    */   private static Object env;
/*    */   
/*    */   static  {
/*    */     try {
/* 27 */       Class<?> clz = Class.forName("com.to8to.sc.spring.SpringContextHolder");
/* 28 */       Method method = clz.getMethod("getBean", new Class[] { Class.class });
/* 29 */       env = method.invoke(null, new Object[] { Class.forName("org.springframework.core.env.Environment") });
/* 30 */       springPropMethod = env.getClass().getMethod("getProperty", new Class[] { String.class, String.class });
/* 31 */     } catch (Exception e) {
/* 32 */       log.warn("获取spring environment失败，", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 37 */   public static int getInt(String key, int defaultV) { return Integer.valueOf(getProp(key, String.valueOf(defaultV))).intValue(); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static String getString(String key, String defaultV) { return getProp(key, defaultV); }
/*    */ 
/*    */   
/*    */   private static String getProp(String key, String defaultV) {
/*    */     try {
/* 46 */       return (springPropMethod == null) ? defaultV : (String)springPropMethod.invoke(env, new Object[] { key, defaultV });
/* 47 */     } catch (Exception e) {
/* 48 */       log.warn("从spring环境变量获取属性失败，key：{}，defaultV：{}，", new Object[] { key, defaultV, e });
/* 49 */       return defaultV;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Asus\Desktop\to8to\juc\commons-1.3.15.jar!\com\to8to\commo\\util\RuntimePropertiesUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.1
 */