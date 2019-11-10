/*    */ package com.chungke.provider018081.common.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StackTraceUtils
/*    */ {
/*    */   public static String getStackTrace() {
/*  9 */     StringBuilder sb = new StringBuilder();
/* 10 */     StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
/* 11 */     for (int i = 2; i < stackTraceElements.length; i++) {
/* 12 */       sb.append(stackTraceElements[i].toString()).append("\n");
/*    */     }
/* 14 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Asus\Desktop\to8to\juc\commons-1.3.15.jar!\com\to8to\commo\\util\StackTraceUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.1
 */