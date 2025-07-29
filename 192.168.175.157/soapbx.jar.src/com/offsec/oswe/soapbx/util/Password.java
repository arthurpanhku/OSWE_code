/*    */ package com.offsec.oswe.soapbx.util;
/*    */ 
/*    */ import java.security.MessageDigest;
/*    */ import java.security.SecureRandom;
/*    */ import java.util.Arrays;
/*    */ import java.util.Base64;
/*    */ 
/*    */ 
/*    */ public class Password
/*    */ {
/*    */   public static String generatePassword(int length) {
/* 12 */     SecureRandom random = new SecureRandom();
/* 13 */     StringBuilder sb = new StringBuilder();
/*    */     
/* 15 */     for (int i = 0; i < length; i++) {
/* 16 */       sb.append(TokenUtil.CHARSET.charAt(random.nextInt(TokenUtil.CHARSET.length())));
/*    */     }
/*    */     
/* 19 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public static String hashPassword(String pass) throws Exception {
/* 24 */     MessageDigest md = MessageDigest.getInstance("SHA1");
/*    */     
/* 26 */     String newPass = Base64.getEncoder().encodeToString(md.digest(pass.getBytes("UTF-8")));
/*    */     
/* 28 */     return newPass;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean comparePasswords(String pass, String hash) throws Exception {
/* 33 */     MessageDigest md = MessageDigest.getInstance("SHA1");
/*    */     
/* 35 */     byte[] hashedPass = md.digest(pass.getBytes("UTF-8"));
/* 36 */     byte[] decodedHash = Base64.getDecoder().decode(hash);
/*    */     
/* 38 */     return Arrays.equals(hashedPass, decodedHash);
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/util/Password.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */