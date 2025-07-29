/*    */ package com.offsec.oswe.soapbx.likes;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Like
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 902560956763441783L;
/*    */   protected int id;
/*    */   protected int userId;
/*    */   protected int storyId;
/*    */   protected boolean isActive;
/*    */   
/*    */   public Like() {}
/*    */   
/*    */   public Like(int id, int userId, int storyId, boolean isActive) {
/* 22 */     this.id = id;
/* 23 */     this.userId = userId;
/* 24 */     this.storyId = storyId;
/* 25 */     this.isActive = isActive;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 29 */     return this.id;
/*    */   }
/*    */   public void setId(int id) {
/* 32 */     this.id = id;
/*    */   }
/*    */   public int getUserId() {
/* 35 */     return this.userId;
/*    */   }
/*    */   public void setUserId(int userId) {
/* 38 */     this.userId = userId;
/*    */   }
/*    */   public int getStoryId() {
/* 41 */     return this.storyId;
/*    */   }
/*    */   public void setStoryId(int storyId) {
/* 44 */     this.storyId = storyId;
/*    */   }
/*    */   public boolean isActive() {
/* 47 */     return this.isActive;
/*    */   }
/*    */   public void setActive(boolean isActive) {
/* 50 */     this.isActive = isActive;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return "Like [id=" + this.id + ", userId=" + this.userId + ", storyId=" + this.storyId + ", isActive=" + this.isActive + "]";
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/likes/Like.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */