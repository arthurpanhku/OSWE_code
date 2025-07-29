/*    */ package com.offsec.oswe.soapbx.util;
/*    */ 
/*    */ public class SqlUtil
/*    */ {
/*    */   public static String escapeString(String s) {
/*  6 */     String tmp = s.replace("'", "");
/*    */     
/*  8 */     tmp = tmp.replace("\"", "");
/*  9 */     tmp = tmp.replace("\n", "");
/* 10 */     tmp = tmp.replace(";", "");
/*    */     
/* 12 */     return tmp;
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/util/SqlUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */