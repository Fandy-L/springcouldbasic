///*     */ package com.chungke.provider018081.common.util;
///*     */
///*     */ import java.util.List;
///*     */ import java.util.stream.Collectors;
///*     */ import org.apache.catalina.mapper.Mapper;
///*     */ import org.springframework.beans.BeansException;
///*     */ import org.springframework.context.ApplicationContext;
///*     */ import org.springframework.context.ApplicationContextAware;
//import org.springframework.data.mapping.MappingException;
//
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */ public class DozerUtils
///*     */   implements ApplicationContextAware
///*     */ {
///*     */   private static Mapper mapper;
///*     */
///*  31 */   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {  mapper = (Mapper)applicationContext.getBean(Mapper.class); }
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */   public static <T> T map(Object source, Class<T> destinationClass) throws MappingException {
///*  45 */     if (source == null)
///*     */     {
///*  47 */       return null;
///*     */     }
///*     */
///*  50 */     return (T)mapper.map(source, destinationClass);
///*     */   }
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*  62 */   public static void map(Object source, Object destination) throws MappingException { mapper.map(source, destination); }
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */   public static <T> T map(Object source, Class<T> destinationClass, String mapId) throws MappingException {
///*  77 */     if (source == null)
///*     */     {
///*  79 */       return null;
///*     */     }
///*     */
///*  82 */     return (T)mapper.map(source, destinationClass, mapId);
///*     */   }
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*  95 */   public static void map(Object source, Object destination, String mapId) throws MappingException { mapper.map(source, destination, mapId); }
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///* 109 */   public static <T> List<T> mapList(List<?> sources, Class<T> destinationClass) throws MappingException { return (List<T>)sources.stream().map(source -> mapper.map(source, paramClass)).collect(Collectors.toList()); }
///*     */ }
//
//
///* Location:              C:\Users\Asus\Desktop\to8to\juc\commons-1.3.15.jar!\com\to8to\commo\\util\DozerUtils.class
// * Java compiler version: 8 (52.0)
// * JD-Core Version:       1.1.1
// */