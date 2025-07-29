/*    */ package com.offsec.oswe.soapbx.util;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AuthUtil
/*    */ {
/*    */   public static boolean isAuthenticated(HttpServletRequest req) {
/* 10 */     if (req.getSession(false) != null && 
/* 11 */       req.getSession(false).getAttribute("authenticated") != null) {
/* 12 */       return ((Boolean)req.getSession(false).getAttribute("authenticated")).booleanValue();
/*    */     }
/*    */ 
/*    */     
/* 16 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean isAdmin(HttpServletRequest req) {
/* 20 */     if (req.getSession(false) != null && 
/* 21 */       req.getSession(false).getAttribute("authenticated") != null && (
/* 22 */       (Boolean)req.getSession(false).getAttribute("authenticated")).booleanValue() && 
/* 23 */       req.getSession(false).getAttribute("isAdmin") != null) {
/* 24 */       return ((Boolean)req.getSession(false).getAttribute("isAdmin")).booleanValue();
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 29 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/util/AuthUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */