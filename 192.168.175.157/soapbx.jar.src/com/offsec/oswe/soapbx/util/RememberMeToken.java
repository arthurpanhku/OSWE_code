/*    */ package com.offsec.oswe.soapbx.util;
/*    */ 
/*    */ public class RememberMeToken
/*    */ {
/*    */   protected int userId;
/*    */   protected String tokenValue;
/*    */   
/*    */   public RememberMeToken() {
/*  9 */     this.userId = 0;
/* 10 */     this.tokenValue = "";
/*    */   }
/*    */ 
/*    */   
/*    */   public RememberMeToken(int userId, String tokenValue) {
/* 15 */     this.userId = userId;
/* 16 */     this.tokenValue = tokenValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getUserId() {
/* 21 */     return this.userId;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setUserId(int userId) {
/* 26 */     this.userId = userId;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTokenValue() {
/* 31 */     return this.tokenValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTokenValue(String tokenValue) {
/* 36 */     this.tokenValue = tokenValue;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return "RememberMeToken [userId=" + this.userId + ", tokenValue=" + this.tokenValue + "]";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 48 */     int prime = 31;
/* 49 */     int result = 1;
/* 50 */     result = 31 * result + ((this.tokenValue == null) ? 0 : this.tokenValue.hashCode());
/* 51 */     result = 31 * result + this.userId;
/* 52 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(RememberMeToken token) {
/* 57 */     return (token.getUserId() == this.userId && token.getTokenValue().equals(this.tokenValue));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 62 */     if (obj instanceof RememberMeToken) {
/* 63 */       RememberMeToken tok = (RememberMeToken)obj;
/* 64 */       return equals(tok);
/*    */     } 
/* 66 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/util/RememberMeToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */