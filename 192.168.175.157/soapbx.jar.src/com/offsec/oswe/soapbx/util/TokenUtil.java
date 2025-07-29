/*    */ package com.offsec.oswe.soapbx.util;
/*    */ 
/*    */ import java.security.SecureRandom;
/*    */ import java.util.Base64;
/*    */ 
/*    */ 
/*    */ public class TokenUtil
/*    */ {
/*    */   public static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
/*    */   public static final String NUMBERS = "1234567890";
/*    */   public static final String SYMBOLS = "!@#$%^&*()";
/* 12 */   public static final String CHARSET = "abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz".toUpperCase() + "1234567890" + "!@#$%^&*()";
/*    */   
/*    */   public static final int TOKEN_LENGTH = 48;
/*    */ 
/*    */   
/*    */   public static String createToken(int userId) {
/* 18 */     StringBuilder sbuild = new StringBuilder();
/*    */     
/* 20 */     byte[] encbytes = new byte[48];
/*    */     
/* 22 */     SecureRandom random = new SecureRandom();
/*    */     
/* 24 */     for (int i = 0; i < 48; i++) {
/* 25 */       sbuild.append(CHARSET.charAt(random.nextInt(CHARSET.length())));
/*    */     }
/*    */     
/* 28 */     byte[] bytes = sbuild.toString().getBytes();
/*    */     
/* 30 */     for (int j = 0; j < bytes.length; j++) {
/* 31 */       encbytes[j] = (byte)(bytes[j] ^ (byte)userId);
/*    */     }
/*    */     
/* 34 */     return Base64.getUrlEncoder().withoutPadding().encodeToString(encbytes);
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/util/TokenUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */