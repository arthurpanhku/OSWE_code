/*    */ package com.offsec.oswe.soapbx.util;
/*    */ 
/*    */ 
/*    */ public class StringUtil
/*    */ {
/*    */   public static String cleanText(String input) {
/*  7 */     String tmp = input;
/*  8 */     tmp = tmp.replace("<", "&lt;");
/*  9 */     tmp = tmp.replace(">", "&gt;");
/* 10 */     tmp = tmp.replace("'", "&#x27;");
/* 11 */     tmp = tmp.replace("\"", "&quot;");
/*    */ 
/*    */     
/* 14 */     return tmp;
/*    */   }
/*    */   
/*    */   public static String cleanPath(String input) {
/* 18 */     String tmp = input.replace(".", "");
/* 19 */     tmp = tmp.replace("/", "");
/* 20 */     return tmp;
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/util/StringUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */