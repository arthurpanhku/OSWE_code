/*    */ package com.offsec.oswe.soapbx.users;
/*    */ 
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import org.springframework.jdbc.core.RowMapper;
/*    */ 
/*    */ 
/*    */ public class UserRowMapper
/*    */   implements RowMapper<User>
/*    */ {
/*    */   public User mapRow(ResultSet rs, int rowNum) throws SQLException {
/* 12 */     User u = new User();
/*    */     
/* 14 */     u.setId(rs.getInt("id"));
/* 15 */     u.setUsername(rs.getString("username"));
/* 16 */     u.setPassword(rs.getString("password"));
/* 17 */     u.setAdmin(rs.getBoolean("isAdmin"));
/* 18 */     u.setActive(rs.getBoolean("isActive"));
/* 19 */     u.setEmail(rs.getString("email"));
/* 20 */     return u;
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/users/UserRowMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */