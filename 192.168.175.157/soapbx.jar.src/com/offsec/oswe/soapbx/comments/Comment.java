/*    */ package com.offsec.oswe.soapbx.comments;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.sql.Date;
/*    */ import java.text.SimpleDateFormat;
/*    */ import org.springframework.format.annotation.DateTimeFormat;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Comment
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -5212778710987944903L;
/*    */   protected int id;
/*    */   protected String content;
/*    */   protected int owner_id;
/*    */   protected int story_id;
/*    */   @DateTimeFormat(pattern = "MMMM dd, YYYY")
/*    */   protected Date created;
/*    */   
/*    */   public int getId() {
/* 26 */     return this.id;
/*    */   }
/*    */   public void setId(int id) {
/* 29 */     this.id = id;
/*    */   }
/*    */   public String getContent() {
/* 32 */     return this.content;
/*    */   }
/*    */   public void setContent(String content) {
/* 35 */     this.content = content;
/*    */   }
/*    */   public int getOwner_id() {
/* 38 */     return this.owner_id;
/*    */   }
/*    */   public void setOwner_id(int ownerId) {
/* 41 */     this.owner_id = ownerId;
/*    */   }
/*    */   public int getStory_id() {
/* 44 */     return this.story_id;
/*    */   }
/*    */   public void setStory_id(int storyId) {
/* 47 */     this.story_id = storyId;
/*    */   }
/*    */   public Date getCreated() {
/* 50 */     return this.created;
/*    */   }
/*    */   public void setCreated(Date created) {
/* 53 */     this.created = created;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFriendlyDate() {
/* 58 */     return (new SimpleDateFormat("MMMM dd, YYYY")).format(this.created);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toXML() {
/* 63 */     return "<comment><id>" + this.id + "</id><content><![CDATA[" + this.content + "]]></description><ownerId>" + this.owner_id + "</ownerId><storyId>" + this.story_id + "</storyId><created>" + this.created + "</created></comment>";
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/comments/Comment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */