/*    */ package com.offsec.oswe.soapbx.categories;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Category
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -4726662101154310522L;
/*    */   protected int id;
/*    */   protected String name;
/*    */   
/*    */   public int getId() {
/* 15 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(int id) {
/* 19 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 23 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 27 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 32 */     return "Category [id=" + this.id + ", name=" + this.name + "]";
/*    */   }
/*    */   
/*    */   public String toXML() {
/* 36 */     return "<category><id>" + this.id + "</id><name>" + this.name + "</name></category>";
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/categories/Category.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */