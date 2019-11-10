///*    */ package com.chungke.provider018081.common.util;
///*    */ import java.net.InetAddress;
///*    */ import java.net.NetworkInterface;
///*    */ import java.net.URI;
///*    */ import java.net.URISyntaxException;
///*    */ import java.util.Collection;
///*    */ import java.util.Collections;
///*    */ import java.util.Enumeration;
///*    */ import java.util.Set;
///*    */
///*    */ public class IpAddressUtils {
///* 12 */   private static final Logger log = LoggerFactory.getLogger(IpAddressUtils.class);
///*    */
///*    */
///*    */   public static String getExternalIp() {
///* 16 */     Collection<String> ips = localAddress();
///* 17 */     for (String s : ips) {
///* 18 */       if (s.contains(".") && !"127.0.0.1".equals(s)) {
///* 19 */         return s;
///*    */       }
///*    */     }
///* 22 */     throw new RuntimeException("获取对外暴露地址失败");
///*    */   }
///*    */
///*    */   public static String hostnameToIp(String hostname) {
///*    */     try {
///* 27 */       return InetAddress.getByName(hostname).getHostAddress();
///* 28 */     } catch (UnknownHostException e) {
///* 29 */       log.warn("解析[{}]地址失败，请检查是否配置dns或者hosts", hostname);
///*    */
///* 31 */       return null;
///*    */     }
///*    */   }
///*    */
///*    */
///*    */
///*    */   public static Collection<String> localAddress() {
///*    */     try {
///* 39 */       Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
///* 40 */       Set<String> localAddr = new HashSet<>();
///* 41 */       while (enumeration.hasMoreElements()) {
///* 42 */         Enumeration<InetAddress> address = ((NetworkInterface)enumeration.nextElement()).getInetAddresses();
///* 43 */         while (address.hasMoreElements()) {
///* 44 */           localAddr.add(((InetAddress)address.nextElement()).getHostAddress());
///*    */         }
///*    */       }
///* 47 */       return Collections.unmodifiableCollection(localAddr);
///* 48 */     } catch (Exception e) {
///* 49 */       log.error("", e);
///* 50 */       throw new IllegalStateException("获取本地地址失败", e);
///*    */     }
///*    */   }
///*    */
///*    */   public static String hostFromUrl(String url) {
///*    */     URI uri;
///*    */     try {
///* 57 */       uri = new URI(url);
///* 58 */     } catch (URISyntaxException e) {
///* 59 */       throw new IllegalArgumentException(e);
///*    */     }
///* 61 */     return uri.getHost();
///*    */   }
///*    */ }
//
//
///* Location:              C:\Users\Asus\Desktop\to8to\juc\commons-1.3.15.jar!\com\to8to\commo\\util\IpAddressUtils.class
// * Java compiler version: 8 (52.0)
// * JD-Core Version:       1.1.1
// */