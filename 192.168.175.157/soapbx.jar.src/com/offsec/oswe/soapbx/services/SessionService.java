/*     */ package com.offsec.oswe.soapbx.services;
/*     */ 
/*     */ import com.offsec.oswe.soapbx.users.User;
/*     */ import com.offsec.oswe.soapbx.users.UserDao;
/*     */ import com.offsec.oswe.soapbx.util.EncryptUtil;
/*     */ import com.offsec.oswe.soapbx.util.RememberMeToken;
/*     */ import com.offsec.oswe.soapbx.util.RememberUtil;
/*     */ import com.offsec.oswe.soapbx.util.TokenUtil;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ import org.springframework.ui.Model;
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
/*     */ @Service
/*     */ public class SessionService
/*     */ {
/*     */   @Autowired
/*     */   private UserDao userDao;
/*     */   @Autowired
/*     */   private ConfigService cfgService;
/*     */   private static final String USERID = "userid";
/*     */   
/*     */   public boolean isAuthenticated(HttpServletRequest req) {
/*  36 */     boolean auth = false;
/*     */     
/*  38 */     if (req.getSession(false) != null) {
/*  39 */       if (req.getSession(false).getAttribute("authenticated") != null) {
/*  40 */         auth = ((Boolean)req.getSession(false).getAttribute("authenticated")).booleanValue();
/*     */       }
/*  42 */     } else if (req.getCookies() != null) {
/*  43 */       for (Cookie c : req.getCookies()) {
/*  44 */         if (c.getName().equalsIgnoreCase("rememberme") && 
/*  45 */           isValidRememberMe(c.getValue())) {
/*     */           
/*     */           try {
/*  48 */             int id = getIdFromRememberMe(c.getValue());
/*  49 */             User uzanto = this.userDao.getUserById(id);
/*  50 */             setupSessionState(req, uzanto);
/*  51 */             auth = true;
/*  52 */           } catch (Exception e) {
/*  53 */             auth = false;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  60 */     return auth;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUserId(HttpServletRequest req) {
/*  65 */     if (isAuthenticated(req))
/*     */     {
/*  67 */       return ((Integer)req.getSession(false).getAttribute("userid")).intValue();
/*     */     }
/*  69 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUsername(HttpServletRequest req) {
/*  74 */     if (isAuthenticated(req)) {
/*  75 */       return (String)req.getSession(false).getAttribute("username");
/*     */     }
/*  77 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cookie createRememberMe(User user) {
/*  87 */     String rawToken = TokenUtil.createToken(user.getId());
/*  88 */     RememberMeToken token = new RememberMeToken(user.getId(), rawToken);
/*     */     
/*  90 */     String cookieValRaw = rawToken + "|" + user.getId();
/*     */     
/*  92 */     String cookieValEnc = EncryptUtil.encryptToken(user, cookieValRaw, this.cfgService.getInstanceId());
/*  93 */     Cookie c = new Cookie("rememberme", user.getUsername() + "." + cookieValEnc);
/*     */     
/*  95 */     RememberUtil.addTokenToCache(token);
/*     */     
/*  97 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValidRememberMe(String value) {
/* 107 */     boolean valid = false;
/*     */     
/* 109 */     if (value.contains(".")) {
/*     */       
/*     */       try {
/* 112 */         String username = value.split("\\.")[0];
/* 113 */         User user = this.userDao.getUserByName(username);
/* 114 */         String cookeVal = value.split("\\.")[1];
/* 115 */         String cookeValClear = EncryptUtil.decryptToken(user, cookeVal, this.cfgService.getInstanceId());
/*     */         
/* 117 */         int id = Integer.parseInt(cookeValClear.split("\\|")[1]);
/* 118 */         String token = cookeValClear.split("\\|")[0];
/*     */ 
/*     */         
/* 121 */         valid = RememberUtil.isTokenValid(new RememberMeToken(id, token));
/* 122 */       } catch (Exception e) {
/* 123 */         valid = false;
/*     */       } 
/*     */     }
/*     */     
/* 127 */     return valid;
/*     */   }
/*     */   
/*     */   public int getIdFromRememberMe(String value) {
/* 131 */     int id = -1;
/*     */     
/* 133 */     if (value.contains(".")) {
/*     */       
/*     */       try {
/* 136 */         String username = value.split("\\.")[0];
/* 137 */         User user = this.userDao.getUserByName(username);
/* 138 */         String cookeVal = value.split("\\.")[1];
/* 139 */         String cookeValClear = EncryptUtil.decryptToken(user, cookeVal, this.cfgService.getInstanceId());
/*     */         
/* 141 */         String token = cookeValClear.split("\\|")[0];
/* 142 */         int uid = Integer.parseInt(cookeValClear.split("\\|")[1]);
/* 143 */         if (RememberUtil.isTokenValid(new RememberMeToken(uid, token))) {
/* 144 */           id = uid;
/*     */         }
/* 146 */       } catch (Exception e) {
/* 147 */         id = -1;
/*     */       } 
/*     */     }
/*     */     
/* 151 */     return id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupSessionState(HttpServletRequest req, User u) {
/* 156 */     HttpSession session = req.getSession();
/* 157 */     session.setAttribute("isAdmin", Boolean.valueOf(u.isAdmin()));
/* 158 */     session.setAttribute("username", u.getUsername());
/* 159 */     session.setAttribute("userid", Integer.valueOf(u.getId()));
/* 160 */     session.setAttribute("authenticated", Boolean.valueOf(true));
/* 161 */     session.setMaxInactiveInterval(1200);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decorateModel(HttpServletRequest req, Model model) {
/* 170 */     if (isAuthenticated(req)) {
/* 171 */       model.addAttribute("isAdmin", req.getSession(false).getAttribute("isAdmin"));
/* 172 */       model.addAttribute("username", req.getSession(false).getAttribute("username"));
/* 173 */       model.addAttribute("userid", req.getSession(false).getAttribute("userid"));
/* 174 */       model.addAttribute("authenticated", Boolean.valueOf(true));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logout(HttpServletRequest req) {
/* 184 */     HttpSession session = req.getSession();
/* 185 */     session.removeAttribute("username");
/* 186 */     session.removeAttribute("isAdmin");
/* 187 */     session.removeAttribute("userid");
/* 188 */     session.removeAttribute("authenticated");
/* 189 */     session.invalidate();
/*     */   }
/*     */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/services/SessionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */