/*    */ package com.offsec.oswe.soapbx.users;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class User
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -1975159082755999074L;
/*    */   protected int id;
/*    */   protected String username;
/*    */   protected String password;
/*    */   protected boolean isActive;
/*    */   protected boolean isAdmin;
/*    */   protected String email;
/*    */   
/*    */   public int getId() {
/* 21 */     return this.id;
/*    */   }
/*    */   public void setId(int id) {
/* 24 */     this.id = id;
/*    */   }
/*    */   public String getUsername() {
/* 27 */     return this.username;
/*    */   }
/*    */   public void setUsername(String username) {
/* 30 */     this.username = username;
/*    */   }
/*    */   public String getPassword() {
/* 33 */     return this.password;
/*    */   }
/*    */   public void setPassword(String password) {
/* 36 */     this.password = password;
/*    */   }
/*    */   
/*    */   public boolean isAdmin() {
/* 40 */     return this.isAdmin;
/*    */   }
/*    */   public void setAdmin(boolean isAdmin) {
/* 43 */     this.isAdmin = isAdmin;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isActive() {
/* 48 */     return this.isActive;
/*    */   }
/*    */   public void setActive(boolean isActive) {
/* 51 */     this.isActive = isActive;
/*    */   }
/*    */   public String getEmail() {
/* 54 */     return this.email;
/*    */   }
/*    */   public void setEmail(String email) {
/* 57 */     this.email = email;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toXML() {
/* 64 */     return "<user><id>" + this.id + "</id><username>" + this.username + "</username><password>" + this.password + "</password><isAdmin>" + this.isAdmin + "</isAdmin><isActive>" + this.isActive + "</isActive><email>" + this.email + "</email></user>";
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/users/User.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */