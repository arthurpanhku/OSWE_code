/*    */ package com.offsec.oswe.soapbx.util;
/*    */ 
/*    */ import com.offsec.oswe.soapbx.users.User;
/*    */ import java.security.MessageDigest;
/*    */ import java.util.Base64;
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.SecretKey;
/*    */ import javax.crypto.spec.SecretKeySpec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EncryptUtil
/*    */ {
/*    */   public static String encryptToken(User user, String token, String salt) {
/*    */     try {
/* 19 */       SecretKey slosilo = getKeyForUser(user, salt);
/* 20 */       Cipher cipher = getCipher();
/* 21 */       cipher.init(1, slosilo);
/*    */       
/* 23 */       byte[] enc = cipher.doFinal(token.getBytes("UTF-8"));
/* 24 */       String value = Base64.getUrlEncoder().encodeToString(enc);
/* 25 */       return value;
/* 26 */     } catch (Exception e) {
/* 27 */       e.printStackTrace();
/* 28 */       return "";
/*    */     } 
/*    */   }
/*    */   
/*    */   public static String decryptToken(User user, String token, String salt) {
/*    */     try {
/* 34 */       SecretKey slosilo = getKeyForUser(user, salt);
/* 35 */       Cipher cipher = getCipher();
/* 36 */       cipher.init(2, slosilo);
/*    */       
/* 38 */       byte[] enc = Base64.getUrlDecoder().decode(token.getBytes("UTF-8"));
/* 39 */       byte[] plain = cipher.doFinal(enc);
/* 40 */       return new String(plain, "UTF-8");
/* 41 */     } catch (Exception e) {
/* 42 */       return "";
/*    */     } 
/*    */   }
/*    */   
/*    */   private static SecretKey getKeyForUser(User user, String salt) throws Exception {
/* 47 */     MessageDigest md = MessageDigest.getInstance("SHA-512");
/* 48 */     String keytext = salt + user.getEmail();
/* 49 */     byte[] keyArray = new byte[24];
/* 50 */     System.arraycopy(md.digest(keytext.getBytes("UTF-8")), 0, keyArray, 0, 24);
/* 51 */     return new SecretKeySpec(keyArray, "DESede");
/*    */   }
/*    */   
/*    */   private static Cipher getCipher() throws Exception {
/* 55 */     return Cipher.getInstance("DESede/ECB/PKCS5Padding");
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/util/EncryptUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */