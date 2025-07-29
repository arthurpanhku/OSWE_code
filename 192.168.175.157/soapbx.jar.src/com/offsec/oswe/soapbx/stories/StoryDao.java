/*     */ package com.offsec.oswe.soapbx.stories;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ import org.springframework.stereotype.Repository;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Repository
/*     */ public class StoryDao
/*     */ {
/*     */   @Autowired
/*     */   JdbcTemplate template;
/*     */   
/*     */   public void truncateTable() {
/*  19 */     String sql = "TRUNCATE TABLE stories";
/*  20 */     this.template.update(sql);
/*     */   }
/*     */   
/*     */   public List<Story> getAllStories() {
/*  24 */     String sql = "SELECT id, title, content, owner_id, category_id, created, needs_mod, active  FROM stories";
/*     */     
/*  26 */     return this.template.query(sql, new StoryRowMapper());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<DecoratedStory> getAllStoriesForUser(int userId) {
/*  31 */     String sql = "SELECT s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username as ownerName, c.name as categoryName, count(com.*) as commentCount, sum(case when l.active = true then 1 else 0 end) as likeCount FROM stories s  LEFT OUTER JOIN users u ON s.owner_id = u.id  LEFT OUTER JOIN categories c ON s.category_id = c.id LEFT OUTER JOIN comments com on s.id = com.story_id LEFT OUTER JOIN likes l on s.id = l.story_id WHERE s.owner_id = " + userId + " GROUP BY s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username, c.name ORDER BY created DESC";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  43 */     return this.template.query(sql, new DecoratedStoryRowMapper());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<DecoratedStory> getAllStoriesForCategory(int categoryId) {
/*  48 */     String sql = "SELECT s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username as ownerName, c.name as categoryName, count(com.*) as commentCount,  sum(case when l.active = true then 1 else 0 end) as likeCount FROM stories s  LEFT OUTER JOIN users u ON s.owner_id = u.id  LEFT OUTER JOIN categories c ON s.category_id = c.id LEFT OUTER JOIN comments com on s.id = com.story_id LEFT OUTER JOIN likes l on s.id = l.story_id WHERE s.category_id = " + categoryId + " and l.active = true GROUP BY s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username, c.name ORDER BY created DESC";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     return this.template.query(sql, new DecoratedStoryRowMapper());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Story> getAllActiveStories() {
/*  65 */     String sql = "SELECT id, title, content, owner_id, category_id, created, needs_mod, active  FROM stories WHERE active = true AND needs_mod = false";
/*     */     
/*  67 */     return this.template.query(sql, new StoryRowMapper());
/*     */   }
/*     */   
/*     */   public List<Story> getAllModStories() {
/*  71 */     String sql = "SELECT id, title, content, owner_id, category_id, created, needs_mod, active  FROM stories WHERE active = true AND needs_mod = true";
/*     */     
/*  73 */     return this.template.query(sql, new StoryRowMapper());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<DecoratedStory> getAllModStoriesDecorated() {
/*  78 */     String sql = "SELECT s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username as ownerName, c.name as categoryName,count(com.*) as commentCount,  count(l.*) as likeCount  FROM stories s  LEFT OUTER JOIN users u ON s.owner_id = u.id  LEFT OUTER JOIN categories c ON s.category_id = c.id LEFT OUTER JOIN comments com on s.id = com.story_id LEFT OUTER JOIN likes l on s.id = l.story_id and l.active = true WHERE s.active = false OR s.needs_mod = true  GROUP BY s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username, c.name ORDER BY created DESC";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     return this.template.query(sql, new DecoratedStoryRowMapper());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<DecoratedStory> getAllActiveStoriesDecorated() {
/*  96 */     String sql = "SELECT s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username as ownerName, c.name as categoryName,count(com.*) as commentCount,  count(l.*) as likeCount  FROM stories s  LEFT OUTER JOIN users u ON s.owner_id = u.id  LEFT OUTER JOIN categories c ON s.category_id = c.id LEFT OUTER JOIN comments com on s.id = com.story_id LEFT OUTER JOIN likes l on s.id = l.story_id and l.active = true WHERE s.active = true AND s.needs_mod = false  GROUP BY s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username, c.name ORDER BY s.id ASC";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     return this.template.query(sql, new DecoratedStoryRowMapper());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateModStory(int id, String needsMod, String active) {
/* 117 */     String sql = "UPDATE stories SET active = " + Boolean.parseBoolean(active) + ", needs_mod = " + Boolean.parseBoolean(needsMod) + " WHERE id = " + id;
/*     */ 
/*     */     
/* 120 */     this.template.update(sql);
/*     */   }
/*     */   
/*     */   public void updateStory(int id, String title, String content, int categoryId, boolean isActive) {
/* 124 */     String sql = "UPDATE stories SET title = ?, content = ?, category_id = " + categoryId + ",  active = " + isActive + " WHERE id = " + id;
/*     */ 
/*     */ 
/*     */     
/* 128 */     this.template.update(sql, new Object[] { title, content });
/*     */   }
/*     */   
/*     */   public List<Story> getTopFiveStories() {
/* 132 */     String sql = "SELECT s.id, s.title, s.content, s.owner_id, s.category_id,  s.created, s.needs_mod, s.active  FROM stories s  LEFT OUTER JOIN likes l ON s.id = l.story_id WHERE active = true AND needs_mod = false  ORDER BY count(s.*) DESC LIMIT 5";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     return this.template.query(sql, new StoryRowMapper());
/*     */   }
/*     */   
/*     */   public void insertStory(String title, String content, int ownerId, int categoryId, boolean needsMod, boolean isActive) {
/* 142 */     String sql = "INSERT INTO stories(title, content, owner_id, category_id, created, needs_mod, active)  VALUES (?,?,?,?,CURRENT_DATE,?,?)";
/*     */ 
/*     */     
/* 145 */     this.template.update(sql, new Object[] { title, content, Integer.valueOf(ownerId), Integer.valueOf(categoryId), Boolean.valueOf(needsMod), Boolean.valueOf(isActive) });
/*     */   }
/*     */ 
/*     */   
/*     */   public void insertStory(Story s) {
/* 150 */     String sql = "INSERT INTO stories(title, content, owner_id, category_id, created, needs_mod, active)  VALUES (?,?,?,?,?,?,?)";
/*     */ 
/*     */     
/* 153 */     this.template.update(sql, new Object[] { s.getTitle(), s.getContent(), Integer.valueOf(s.getOwnerId()), Integer.valueOf(s.getCategoryId()), s.getCreated(), Boolean.valueOf(s.isNeedsMod()), Boolean.valueOf(s.isActive()) });
/*     */   }
/*     */   
/*     */   public List<DecoratedStory> getTopFiveStoriesDecorated() {
/* 157 */     String sql = "SELECT s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username as ownerName, cat.name as categoryName, count(c.*) as commentCount,  count(l.*) as likeCount FROM stories s  LEFT OUTER JOIN users u ON s.owner_id = u.id  LEFT OUTER JOIN categories cat ON s.category_id = cat.id LEFT OUTER JOIN comments c on s.id = c.story_id LEFT OUTER JOIN likes l on s.id = l.story_id and l.active = true WHERE s.active = true AND s.needs_mod = false  GROUP BY s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username, cat.name ORDER BY count(l.*) DESC LIMIT 5";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     return this.template.query(sql, new DecoratedStoryRowMapper());
/*     */   }
/*     */   
/*     */   public List<DecoratedStory> getTopFiveStoriesDecoratedOffset(int offset) {
/* 173 */     String sql = "SELECT s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username as ownerName, cat.name as categoryName, count(c.*) as commentCount, count(l.*) as likeCount FROM stories s  LEFT OUTER JOIN users u ON s.owner_id = u.id  LEFT OUTER JOIN categories cat ON s.category_id = cat.id LEFT OUTER JOIN comments c on s.id = c.story_id LEFT OUTER JOIN likes l ON s.id = l.story_id AND l.active = true WHERE s.active = true AND s.needs_mod = false  GROUP BY s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username, cat.name ORDER BY count(l.*) DESC LIMIT 5 OFFSET " + offset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     return this.template.query(sql, new DecoratedStoryRowMapper());
/*     */   }
/*     */   
/*     */   public DecoratedStory getDecoratedStoryById(int id) {
/* 189 */     String sql = "SELECT s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username as ownerName, cat.name as categoryName, count(c.*) as commentCount, count(l.*) as likeCount FROM stories s  LEFT OUTER JOIN users u ON s.owner_id = u.id  LEFT OUTER JOIN categories cat ON s.category_id = cat.id LEFT OUTER JOIN comments c on s.id = c.story_id LEFT OUTER JOIN likes l ON s.id = l.story_id AND l.active = true WHERE s.id = ?  GROUP BY s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username, cat.name ORDER BY created DESC";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     return (DecoratedStory)this.template.queryForObject(sql, new DecoratedStoryRowMapper(), new Object[] { Integer.valueOf(id) });
/*     */   }
/*     */   
/*     */   public List<DecoratedStory> searchForStories(String search) {
/* 205 */     String sql = "SELECT s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username as ownerName, cat.name as categoryName, count(c.*) as commentCount, count(l.*) as likeCount FROM stories s  LEFT OUTER JOIN users u ON s.owner_id = u.id  LEFT OUTER JOIN categories cat ON s.category_id = cat.id LEFT OUTER JOIN comments c on s.id = c.story_id LEFT OUTER JOIN likes l ON s.id = l.story_id and l.active = true WHERE s.title like ? OR c.content like ? OR s.content like ?   GROUP BY s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username, cat.name ORDER BY created DESC";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     String term = "%" + search + "%";
/*     */     
/* 218 */     return this.template.query(sql, new DecoratedStoryRowMapper(), new Object[] { term, term, term });
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getStoryCountForUser(int userId) {
/* 223 */     String sql = "SELECT count(*) FROM stories WHERE owner_id = ?";
/*     */     
/* 225 */     return (Integer)this.template.queryForObject(sql, Integer.class, new Object[] { Integer.valueOf(userId) });
/*     */   }
/*     */   
/*     */   public Integer getStoryCount() {
/* 229 */     String sql = "SELECT count(*) FROM stories";
/* 230 */     return (Integer)this.template.queryForObject(sql, Integer.class);
/*     */   }
/*     */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/stories/StoryDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */