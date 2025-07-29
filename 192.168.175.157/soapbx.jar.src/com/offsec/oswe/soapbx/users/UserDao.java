/*     */ package com.offsec.oswe.soapbx.users;
/*     */ 
/*     */ import com.offsec.oswe.soapbx.stories.DecoratedStory;
/*     */ import com.offsec.oswe.soapbx.stories.DecoratedStoryRowMapper;
/*     */ import com.offsec.oswe.soapbx.util.SqlUtil;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ import org.springframework.jdbc.core.RowMapper;
/*     */ import org.springframework.stereotype.Repository;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Repository
/*     */ public class UserDao
/*     */ {
/*     */   @Autowired
/*     */   JdbcTemplate template;
/*     */   
/*     */   public void truncateTable() {
/*  22 */     String sql = "TRUNCATE TABLE users";
/*  23 */     this.template.update(sql);
/*     */   }
/*     */   
/*     */   public void insertUser(String username, String password, boolean isAdmin, boolean isActive, String email) {
/*  27 */     String sql = "INSERT INTO users(username, password, isAdmin, isActive, email)  VALUES (?,?,?,?,?);";
/*     */     
/*  29 */     this.template.update(sql, new Object[] { username, password, Boolean.valueOf(isAdmin), Boolean.valueOf(isActive), email });
/*     */   }
/*     */ 
/*     */   
/*     */   public User getUserById(int id) throws Exception {
/*  34 */     String sql = "SELECT id, username, password, isAdmin, isActive, email  FROM users WHERE id = ?";
/*     */ 
/*     */     
/*  37 */     return (User)this.template.queryForObject(sql, new UserRowMapper(), new Object[] { Integer.valueOf(id) });
/*     */   }
/*     */   
/*     */   public User getUserByName(String username) throws Exception {
/*  41 */     String sql = "SELECT id, username, password, isAdmin, isActive, email  FROM users WHERE username = ?";
/*     */ 
/*     */     
/*  44 */     return (User)this.template.queryForObject(sql, new UserRowMapper(), new Object[] { username });
/*     */   }
/*     */ 
/*     */   
/*     */   public void banUser(int id) throws Exception {
/*  49 */     String sql = "UPDATE users SET isActive = false WHERE id = " + id;
/*  50 */     this.template.update(sql);
/*     */   }
/*     */ 
/*     */   
/*     */   public void banUser(String id) throws Exception {
/*  55 */     String sql = "UPDATE users SET isActive = false WHERE id = " + SqlUtil.escapeString(id);
/*  56 */     this.template.update(sql);
/*     */   }
/*     */ 
/*     */   
/*     */   public void activateUser(int id) throws Exception {
/*  61 */     String sql = "UPDATE users SET isActive = true WHERE id = " + id;
/*  62 */     this.template.update(sql);
/*     */   }
/*     */   
/*     */   public void activateUser(String id) throws Exception {
/*  66 */     String sql = "UPDATE users SET isActive = true WHERE id = " + SqlUtil.escapeString(id);
/*  67 */     this.template.update(sql);
/*     */   }
/*     */   
/*     */   public List<User> getAllUsers() {
/*  71 */     String sql = "SELECT id, username, password, isAdmin, isActive, email  FROM users ORDER BY id ASC";
/*     */ 
/*     */     
/*  74 */     return this.template.query(sql, new UserRowMapper());
/*     */   }
/*     */   
/*     */   public List<User> getUsersWithPostsInCategory(int categoryId) throws Exception {
/*  78 */     String sql = "SELECT u.id, u.username, '***' as password, u.isAdmin, u.isActive, u.email  FROM users u  LEFT OUTER JOIN stories s ON s.owner_id = u.id  LEFT OUTER JOIN categories c ON s.category_id = c.id WHERE s.category_id = " + categoryId;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     return this.template.query(sql, new UserRowMapper());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<User> getUsersWithPostsInCategory(String categoryId) throws Exception {
/*  92 */     String sql = "SELECT u.id, u.username, '***' as password, u.isAdmin, u.isActive, u.email  FROM users u  LEFT OUTER JOIN stories s ON s.owner_id = u.id  LEFT OUTER JOIN categories c ON s.category_id = c.id WHERE s.category_id = " + SqlUtil.escapeString(categoryId);
/*     */     
/*  94 */     return this.template.query(sql, new UserRowMapper());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<DecoratedStory> getStoriesInCategory(String categoryId) throws Exception {
/*  99 */     return getStoriesInCategory(Integer.parseInt(categoryId));
/*     */   }
/*     */   
/*     */   public List<DecoratedStory> getStoriesInCategory(int categoryId) throws Exception {
/* 103 */     String sql = "SELECT s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active,  u.username as ownerName, c.name as categoryName, 0 as commentCount, 0 as likeCount FROM stories s  LEFT OUTER JOIN users u ON s.owner_id = u.id  LEFT OUTER JOIN categories c ON s.category_id = c.id WHERE s.category_id = " + categoryId + " GROUP BY s.id, s.title, s.content, s.owner_id, s.category_id, s.created, s.needs_mod, s.active, u.username, c.name ORDER BY created DESC";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     return this.template.query(sql, (RowMapper)new DecoratedStoryRowMapper());
/*     */   }
/*     */   
/*     */   public Integer getUserCount() {
/* 115 */     String sql = "SELECT count(*) FROM users";
/* 116 */     return (Integer)this.template.queryForObject(sql, Integer.class);
/*     */   }
/*     */   
/*     */   public void updatePasswordForUser(int userId, String newPass) {
/* 120 */     String sql = "UPDATE users SET password = ? WHERE id = ?";
/*     */     
/* 122 */     this.template.update(sql, new Object[] { newPass, Integer.valueOf(userId) });
/*     */   }
/*     */   
/*     */   public Integer getUserIdForToken(String token) throws Exception {
/* 126 */     String sql = "SELECT user_id FROM tokens WHERE token = ?";
/*     */     
/* 128 */     return (Integer)this.template.queryForObject(sql, Integer.class, new Object[] { token });
/*     */   }
/*     */   
/*     */   public void insertTokenForUser(String token, int userId) {
/* 132 */     String sql = "INSERT INTO tokens(user_id, token) VALUES (?,?)";
/* 133 */     this.template.update(sql, new Object[] { Integer.valueOf(userId), token });
/*     */   }
/*     */   
/*     */   public void deleteTokensForUser(int userId) {
/* 137 */     String sql = "DELETE FROM tokens WHERE user_id = ?";
/* 138 */     this.template.update(sql, new Object[] { Integer.valueOf(userId) });
/*     */   }
/*     */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/users/UserDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */