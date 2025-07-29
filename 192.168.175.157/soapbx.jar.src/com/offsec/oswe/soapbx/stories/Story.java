/*    */ package com.offsec.oswe.soapbx.stories;
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
/*    */ public class Story
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -7336987072240637875L;
/*    */   protected int id;
/*    */   protected String title;
/*    */   protected String content;
/*    */   protected int ownerId;
/*    */   protected int categoryId;
/*    */   @DateTimeFormat(pattern = "MMMM dd, YYYY")
/*    */   protected Date created;
/*    */   protected boolean needsMod;
/*    */   protected boolean isActive;
/*    */   
/*    */   public int getId() {
/* 29 */     return this.id;
/*    */   }
/*    */   public void setId(int id) {
/* 32 */     this.id = id;
/*    */   }
/*    */   public String getTitle() {
/* 35 */     return this.title;
/*    */   }
/*    */   public void setTitle(String title) {
/* 38 */     this.title = title;
/*    */   }
/*    */   public String getContent() {
/* 41 */     return this.content;
/*    */   }
/*    */   public void setContent(String content) {
/* 44 */     this.content = content;
/*    */   }
/*    */   public String getLede() {
/* 47 */     if (this.content.length() > 200) {
/* 48 */       return this.content.substring(0, 200) + "...";
/*    */     }
/* 50 */     return this.content;
/*    */   }
/*    */   
/*    */   public String getFormattedContent() {
/* 54 */     return this.content.replace("\\n", "<br>").replace("\\r", "<br>");
/*    */   }
/*    */   public int getOwnerId() {
/* 57 */     return this.ownerId;
/*    */   }
/*    */   public void setOwnerId(int ownerId) {
/* 60 */     this.ownerId = ownerId;
/*    */   }
/*    */   public int getCategoryId() {
/* 63 */     return this.categoryId;
/*    */   }
/*    */   public void setCategoryId(int categoryId) {
/* 66 */     this.categoryId = categoryId;
/*    */   }
/*    */   public Date getCreated() {
/* 69 */     return this.created;
/*    */   }
/*    */   public void setCreated(Date created) {
/* 72 */     this.created = created;
/*    */   }
/*    */   public boolean isNeedsMod() {
/* 75 */     return this.needsMod;
/*    */   }
/*    */   public void setNeedsMod(boolean needsMod) {
/* 78 */     this.needsMod = needsMod;
/*    */   }
/*    */   public boolean isActive() {
/* 81 */     return this.isActive;
/*    */   }
/*    */   public void setActive(boolean isActive) {
/* 84 */     this.isActive = isActive;
/*    */   }
/*    */   public String getFriendlyDate() {
/* 87 */     return (new SimpleDateFormat("MMMM dd, YYYY")).format(this.created);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 92 */     return "Story [id=" + this.id + ", title=" + this.title + ", content=" + this.content + ", ownerId=" + this.ownerId + ", categoryId=" + this.categoryId + ", created=" + this.created + ", needsMod=" + this.needsMod + ", isActive=" + this.isActive + "]";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toXML() {
/* 98 */     return "<story><id>" + this.id + "</id><title>" + this.title + "</title><content><![CDATA[" + this.content + "]]></content><ownerId>" + this.ownerId + "</ownerId><categoryId>" + this.categoryId + "</categoryId><created>" + this.created + "</created><needsMod>" + this.needsMod + "</needsMod><isActive>" + this.isActive + "</isActive></story>";
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/stories/Story.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */