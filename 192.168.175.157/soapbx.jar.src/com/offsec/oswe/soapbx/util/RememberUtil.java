/*    */ package com.offsec.oswe.soapbx.util;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ public class RememberUtil
/*    */ {
/*  8 */   private static HashMap<RememberMeToken, Long> tokenCache = new HashMap<>();
/*  9 */   private static Long timeout = Long.valueOf(3600L);
/*    */ 
/*    */   
/*    */   public static void addTokenToCache(RememberMeToken token) {
/* 13 */     Long ttl = Long.valueOf(System.currentTimeMillis() / 1000L);
/* 14 */     tokenCache.put(token, ttl);
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isTokenValid(RememberMeToken token) {
/* 19 */     boolean isValid = false;
/* 20 */     if (tokenCache.containsKey(token)) {
/* 21 */       Long iat = tokenCache.get(token);
/* 22 */       if (System.currentTimeMillis() / 1000L - iat.longValue() < timeout.longValue()) {
/* 23 */         isValid = true;
/*    */         
/* 25 */         addTokenToCache(token);
/*    */       } else {
/* 27 */         tokenCache.remove(token);
/*    */       } 
/*    */     } 
/*    */     
/* 31 */     return isValid;
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/util/RememberUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */