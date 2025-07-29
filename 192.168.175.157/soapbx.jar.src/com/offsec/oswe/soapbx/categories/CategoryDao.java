/*     */ package com.offsec.oswe.soapbx.categories;
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
/*     */ @Repository
/*     */ public class CategoryDao
/*     */ {
/*     */   @Autowired
/*     */   JdbcTemplate template;
/*     */   
/*     */   public void truncateTable() {
/*  19 */     String sql = "TRUNCATE TABLE categories";
/*  20 */     this.template.update(sql);
/*     */   }
/*     */   
/*     */   public List<Category> getAllCategories() {
/*  24 */     String sql = "SELECT id, name  FROM categories  ORDER BY name ASC;";
/*     */ 
/*     */     
/*  27 */     return this.template.query(sql, new CategoryRowMapper());
/*     */   }
/*     */   
/*     */   public void addCategory(String name) {
/*  31 */     String sql = "INSERT INTO categories(name) VALUES  (?)";
/*     */     
/*  33 */     this.template.update(sql, new Object[] { name });
/*     */   }
/*     */   
/*     */   public void deleteCategory(int id) {
/*  37 */     String sql = "DELETE FROM categories  WHERE id = ?";
/*     */     
/*  39 */     this.template.update(sql, new Object[] { Integer.valueOf(id) });
/*     */   }
/*     */ 
/*     */   
/*     */   public List<DecoratedCategory> getAllDecoratedCategories() {
/*  44 */     String sql = "SELECT c.id, c.name, count(s.id) as storyCount  FROM categories c  LEFT JOIN stories s ON c.id = s.category_id GROUP BY c.id, c.name ORDER BY c.name ASC ";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  49 */     return this.template.query(sql, new DecoratedCategoryRowMapper());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<DecoratedCategory> getAllDecoratedCategoriesSorted(String s) {
/*     */     String sort;
/*  58 */     if (s.equalsIgnoreCase("count")) {
/*  59 */       sort = "count(s.id)";
/*  60 */     } else if (s.equalsIgnoreCase("name")) {
/*  61 */       sort = "c.name";
/*     */     } else {
/*  63 */       sort = "";
/*     */     } 
/*     */     
/*  66 */     String sql = "SELECT c.id, c.name, count(s.id) as storyCount FROM categories c  LEFT JOIN stories s ON c.id = s.category_id GROUP BY c.id, c.name ";
/*     */ 
/*     */ 
/*     */     
/*  70 */     if (!sort.equalsIgnoreCase("")) {
/*  71 */       sql = sql + " ORDER BY " + sort + " DESC ";
/*     */     }
/*     */     
/*  74 */     return this.template.query(sql, new DecoratedCategoryRowMapper());
/*     */   }
/*     */ 
/*     */   
/*     */   public DecoratedCategory getDecoratedCategoryById(String id) throws Exception {
/*  79 */     return getDecoratedCategoryById(Integer.parseInt(id));
/*     */   }
/*     */   
/*     */   public DecoratedCategory getDecoratedCategoryById(int id) {
/*  83 */     String sql = "SELECT c.id, c.name, count(s.*) as storyCount FROM categories c  LEFT JOIN stories s ON c.id = s.category_id WHERE c.id = " + id + " GROUP BY c.id, c.name ORDER BY c.name DESC ";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     return (DecoratedCategory)this.template.queryForObject(sql, new DecoratedCategoryRowMapper());
/*     */   }
/*     */   
/*     */   private class CategoryRowMapper
/*     */     implements RowMapper<Category>
/*     */   {
/*     */     private CategoryRowMapper() {}
/*     */     
/*     */     public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
/*  97 */       Category cat = new Category();
/*  98 */       cat.setId(rs.getInt("id"));
/*  99 */       cat.setName(rs.getString("name"));
/* 100 */       return cat;
/*     */     }
/*     */   }
/*     */   
/*     */   private class DecoratedCategoryRowMapper
/*     */     implements RowMapper<DecoratedCategory>
/*     */   {
/*     */     private DecoratedCategoryRowMapper() {}
/*     */     
/*     */     public DecoratedCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
/* 110 */       DecoratedCategory cat = new DecoratedCategory();
/* 111 */       cat.setId(rs.getInt("id"));
/* 112 */       cat.setName(rs.getString("name"));
/* 113 */       cat.setStoryCount(rs.getInt("storyCount"));
/* 114 */       return cat;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/categories/CategoryDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */