/*    */ package com.offsec.oswe.soapbx.admin;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.jdbc.core.JdbcTemplate;
/*    */ import org.springframework.stereotype.Repository;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Repository
/*    */ public class AdminDao
/*    */ {
/*    */   @Autowired
/*    */   JdbcTemplate template;
/*    */   
/*    */   public List<Map<String, Object>> runQuery(String query) throws Exception {
/* 18 */     return this.template.queryForList(query);
/*    */   }
/*    */   
/*    */   public String getWelcomeEmail() throws Exception {
/* 22 */     String sql = "SELECT content FROM templates WHERE title = 'welcome';";
/* 23 */     return (String)this.template.queryForObject(sql, String.class);
/*    */   }
/*    */   
/*    */   public void updateWelcomeEmail(String content) throws Exception {
/* 27 */     String sql = "UPDATE templates SET content = ? WHERE title = 'welcome';";
/* 28 */     this.template.update(sql, new Object[] { content });
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/admin/AdminDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */