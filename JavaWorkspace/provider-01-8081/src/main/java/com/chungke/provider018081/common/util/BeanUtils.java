/*    */ package com.chungke.provider018081.common.util;
/*    */ 
/*    */ import com.fasterxml.jackson.core.type.TypeReference;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.databind.ObjectWriter;
///*    */ import com.to8to.common.http.TypeReference;
/*    */ import java.io.IOException;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BeanUtils
/*    */ {
/* 15 */   private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);
/*    */   
/* 17 */   private static final ObjectMapper OM = new ObjectMapper();
/* 18 */   private static final ObjectWriter OW = OM.writer().withDefaultPrettyPrinter();
/*    */   
/*    */   public static String deepPrint(Object o) {
/*    */     try {
/* 22 */       return OW.writeValueAsString(o);
/* 23 */     } catch (Exception e) {
/* 24 */       log.error("格式化数据失败，", e);
/* 25 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static <T> T parser(byte[] in, Class<T> clz) {
/*    */     try {
/* 31 */       return (T)OM.readValue(in, clz);
/* 32 */     } catch (IOException e) {
/* 33 */       log.error("格式化数据失败，", e);
/* 34 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static <T> T parser(byte[] in, TypeReference typeReference) {
/*    */     try {
/* 40 */       return (T)OM.readValue(in, (TypeReference)typeReference);
/* 41 */     } catch (IOException e) {
/* 42 */       log.error("格式化数据失败，", e);
/* 43 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Asus\Desktop\to8to\juc\commons-1.3.15.jar!\com\to8to\commo\\util\BeanUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.1
 */