/*    */ package com.offsec.oswe.soapbx.stories;
/*    */ 
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import org.springframework.jdbc.core.RowMapper;
/*    */ 
/*    */ 
/*    */ public class StoryRowMapper
/*    */   implements RowMapper<Story>
/*    */ {
/*    */   public Story mapRow(ResultSet rs, int rowNum) throws SQLException {
/* 12 */     Story s = new Story();
/* 13 */     s.setId(rs.getInt("id"));
/* 14 */     s.setTitle(rs.getString("title"));
/* 15 */     s.setContent(rs.getString("content"));
/* 16 */     s.setOwnerId(rs.getInt("owner_id"));
/* 17 */     s.setCategoryId(rs.getInt("category_id"));
/* 18 */     s.setCreated(rs.getDate("created"));
/* 19 */     s.setNeedsMod(rs.getBoolean("needs_mod"));
/* 20 */     s.setActive(rs.getBoolean("active"));
/* 21 */     return s;
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/stories/StoryRowMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */