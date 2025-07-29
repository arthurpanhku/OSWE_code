/*     */ package com.offsec.oswe.soapbx.comments;
/*     */ 
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ import org.springframework.jdbc.core.RowMapper;
/*     */ import org.springframework.stereotype.Repository;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Repository
/*     */ public class CommentDao
/*     */ {
/*     */   @Autowired
/*     */   JdbcTemplate template;
/*     */   
/*     */   public void truncateTable() {
/*  20 */     String sql = "TRUNCATE TABLE comments";
/*  21 */     this.template.update(sql);
/*     */   }
/*     */   
/*     */   public List<Comment> getAllComments() {
/*  25 */     String sql = "SELECT id, description, owner_id, story_id, created  FROM answers";
/*     */     
/*  27 */     return this.template.query(sql, new CommentRowMapper());
/*     */   }
/*     */   
/*     */   public List<Comment> getCommentsForStory(int story_id) {
/*  31 */     String sql = "SELECT id, content, owner_id, story_id, created  FROM comments  WHERE story_id = ?  ORDER BY created DESC";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  36 */     return this.template.query(sql, new CommentRowMapper(), new Object[] { Integer.valueOf(story_id) });
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCommentToStory(String content, int ownerId, int storyId) {
/*  41 */     String sql = "INSERT INTO comments(content, owner_id, story_id, created)  VALUES (?,?,?,CURRENT_DATE);";
/*     */ 
/*     */     
/*  44 */     this.template.update(sql, new Object[] { content, Integer.valueOf(ownerId), Integer.valueOf(storyId) });
/*     */   }
/*     */   
/*     */   public List<DecoratedComment> getDecoratedCommentsForStory(int story_id) {
/*  48 */     String sql = "SELECT c.id, c.content, c.owner_id, c.story_id, c.created, u.username as ownerName FROM comments c  LEFT OUTER JOIN users u ON c.owner_id = u.id  WHERE c.story_id = ?  ORDER BY created ASC";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  54 */     return this.template.query(sql, new DecoratedCommentRowMapper(), new Object[] { Integer.valueOf(story_id) });
/*     */   }
/*     */   
/*     */   public void insertComment(Comment c) {
/*  58 */     String sql = "INSERT INTO comments(content, owner_id, story_id, created)  VALUES (?,?,?,?);";
/*     */ 
/*     */     
/*  61 */     this.template.update(sql, new Object[] { c.getContent(), Integer.valueOf(c.getOwner_id()), Integer.valueOf(c.getStory_id()), c.getCreated() });
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getCommentCount() {
/*  66 */     String sql = "SELECT count(*) FROM comments";
/*  67 */     return (Integer)this.template.queryForObject(sql, Integer.class);
/*     */   }
/*     */   
/*     */   public Integer getCommentCountForUser(int userId) {
/*  71 */     String sql = "SELECT count(*) FROM comments WHERE owner_id = ?";
/*  72 */     return (Integer)this.template.queryForObject(sql, Integer.class, new Object[] { Integer.valueOf(userId) });
/*     */   }
/*     */   
/*     */   private class CommentRowMapper
/*     */     implements RowMapper<Comment> {
/*     */     private CommentRowMapper() {}
/*     */     
/*     */     public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
/*  80 */       Comment c = new Comment();
/*  81 */       c.setId(rs.getInt("id"));
/*  82 */       c.setContent(rs.getString("content"));
/*  83 */       c.setOwner_id(rs.getInt("owner_id"));
/*  84 */       c.setStory_id(rs.getInt("story_id"));
/*  85 */       c.setCreated(rs.getDate("created"));
/*  86 */       return c;
/*     */     }
/*     */   }
/*     */   
/*     */   private class DecoratedCommentRowMapper
/*     */     implements RowMapper<DecoratedComment> {
/*     */     private DecoratedCommentRowMapper() {}
/*     */     
/*     */     public DecoratedComment mapRow(ResultSet rs, int rowNum) throws SQLException {
/*  95 */       DecoratedComment dc = new DecoratedComment();
/*  96 */       dc.setId(rs.getInt("id"));
/*  97 */       dc.setContent(rs.getString("content"));
/*  98 */       dc.setOwner_id(rs.getInt("owner_id"));
/*  99 */       dc.setStory_id(rs.getInt("story_id"));
/* 100 */       dc.setCreated(rs.getDate("created"));
/* 101 */       dc.setOwnerName(rs.getString("ownerName"));
/* 102 */       return dc;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/comments/CommentDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */