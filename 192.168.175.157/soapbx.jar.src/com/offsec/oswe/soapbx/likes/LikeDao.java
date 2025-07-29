/*    */ package com.offsec.oswe.soapbx.likes;
/*    */ 
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.jdbc.core.JdbcTemplate;
/*    */ import org.springframework.jdbc.core.RowMapper;
/*    */ import org.springframework.stereotype.Repository;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Repository
/*    */ public class LikeDao
/*    */ {
/*    */   @Autowired
/*    */   JdbcTemplate template;
/*    */   
/*    */   public int getLikeCountForStory(int storyId) {
/* 19 */     String sql = "SELECT count(*) FROM likes  WHERE story_id = " + storyId + " AND isActive = true ";
/*    */ 
/*    */     
/* 22 */     return ((Integer)this.template.queryForObject(sql, int.class)).intValue();
/*    */   }
/*    */   
/*    */   public Like getLikeForStory(int storyId, int uId) throws Exception {
/* 26 */     String sql = "SELECT id, owner_id, story_id, active  FROM likes  WHERE story_id = " + storyId + " AND owner_id = " + uId;
/*    */ 
/*    */ 
/*    */     
/* 30 */     return (Like)this.template.queryForObject(sql, new LikeRowMapper());
/*    */   }
/*    */   
/*    */   public boolean isStoryLikedByUser(int storyId, int uId) {
/* 34 */     String sql = "SELECT id, owner_id, story_id, active  FROM likes  WHERE story_id = " + storyId + " WHERE owner_id = " + uId;
/*    */ 
/*    */ 
/*    */     
/* 38 */     Like l = (Like)this.template.queryForObject(sql, new LikeRowMapper());
/*    */     
/* 40 */     return (l != null) ? l.isActive() : false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateLikeActive(Like l) {
/* 46 */     String sql = "UPDATE likes  SET active = " + l.isActive + " WHERE id = " + l.getId();
/* 47 */     this.template.update(sql);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addLikeToStory(int storyId, int userId) {
/*    */     Like l;
/*    */     try {
/* 54 */       l = getLikeForStory(storyId, userId);
/* 55 */     } catch (Exception e) {
/* 56 */       l = null;
/*    */     } 
/* 58 */     if (l != null) {
/*    */       
/* 60 */       if (!l.isActive()) {
/*    */         
/* 62 */         l.setActive(true);
/* 63 */         updateLikeActive(l);
/*    */       } else {
/*    */         
/* 66 */         l.setActive(false);
/* 67 */         updateLikeActive(l);
/*    */       } 
/*    */     } else {
/*    */       
/* 71 */       String sql = "INSERT INTO likes(owner_id, story_id, active) VALUES (?, ?, true)";
/* 72 */       this.template.update(sql, new Object[] { Integer.valueOf(userId), Integer.valueOf(storyId) });
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private class LikeRowMapper
/*    */     implements RowMapper<Like>
/*    */   {
/*    */     private LikeRowMapper() {}
/*    */     
/*    */     public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
/* 83 */       Like l = new Like();
/* 84 */       l.setId(rs.getInt("id"));
/* 85 */       l.setUserId(rs.getInt("owner_id"));
/* 86 */       l.setStoryId(rs.getInt("story_id"));
/* 87 */       l.setActive(rs.getBoolean("active"));
/*    */       
/* 89 */       return l;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/likes/LikeDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */