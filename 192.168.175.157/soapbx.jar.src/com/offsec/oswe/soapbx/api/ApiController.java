/*     */ package com.offsec.oswe.soapbx.api;
/*     */ 
/*     */ import com.offsec.oswe.soapbx.services.ConfigService;
/*     */ import com.offsec.oswe.soapbx.stories.DecoratedStory;
/*     */ import com.offsec.oswe.soapbx.stories.StoryDao;
/*     */ import com.offsec.oswe.soapbx.users.User;
/*     */ import com.offsec.oswe.soapbx.users.UserDao;
/*     */ import com.offsec.oswe.soapbx.util.SqlUtil;
/*     */ import java.util.ArrayList;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ public class ApiController
/*     */ {
/*  30 */   private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
/*     */ 
/*     */   
/*     */   private static final String APIKEY_PARAMETER = "apiKey";
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   protected StoryDao storyDao;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   UserDao userDao;
/*     */   
/*     */   @Autowired
/*     */   ConfigService cfgService;
/*     */ 
/*     */   
/*     */   @GetMapping({"/api", "/api/"})
/*     */   public ResponseEntity<String> getBase() {
/*  49 */     return ResponseEntity.ok().body("");
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/api/info"}, method = {RequestMethod.GET}, produces = {"application/json"})
/*     */   public ResponseEntity<String> getHealthCheck() {
/*  55 */     String instanceId = this.cfgService.getInstanceId();
/*  56 */     String version = "0.4.2";
/*  57 */     String response = "{\"name\":\"Soapbx\",\"description\":\"A platform for everyone.\",\"version\":\"" + version + "\"}";
/*     */ 
/*     */     
/*  60 */     return ResponseEntity.ok().body(response);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/api/stories"}, method = {RequestMethod.GET}, produces = {"application/json"})
/*     */   public ResponseEntity<String> getStories(HttpServletRequest request) {
/*  67 */     if (isAuthorized(request)) {
/*  68 */       ArrayList<DecoratedStory> stories = (ArrayList<DecoratedStory>)this.storyDao.getAllActiveStoriesDecorated();
/*     */       
/*  70 */       StringBuilder sb = new StringBuilder();
/*  71 */       sb.append("[");
/*  72 */       int i = stories.size() - 1;
/*  73 */       int x = 0;
/*  74 */       for (DecoratedStory s : stories) {
/*  75 */         sb.append("{");
/*  76 */         sb.append("\"id\":\"" + s.getId() + "\",");
/*  77 */         sb.append("\"title\":\"" + s.getTitle() + "\",");
/*  78 */         sb.append("\"owner\":\"" + s.getOwnerName() + "\"");
/*  79 */         sb.append("}");
/*  80 */         if (x < i) {
/*  81 */           sb.append(",");
/*  82 */           x++;
/*     */         } 
/*     */       } 
/*     */       
/*  86 */       sb.append("]");
/*     */       
/*  88 */       String response = sb.toString();
/*     */       
/*  90 */       return ResponseEntity.ok().body(response);
/*     */     } 
/*  92 */     return ResponseEntity.status(401).body("{\"message\":\"You are not authorized.\"}");
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/api/users"}, method = {RequestMethod.GET}, produces = {"application/json"})
/*     */   public ResponseEntity<String> getUsers(HttpServletRequest request) {
/*  98 */     if (isAuthorized(request)) {
/*  99 */       ArrayList<User> users = (ArrayList<User>)this.userDao.getAllUsers();
/*     */       
/* 101 */       StringBuilder sb = new StringBuilder();
/* 102 */       sb.append("[");
/* 103 */       int i = users.size() - 1;
/* 104 */       int x = 0;
/* 105 */       for (User u : users) {
/* 106 */         sb.append("{");
/* 107 */         sb.append("\"id\":\"" + u.getId() + "\",");
/* 108 */         sb.append("\"username\":\"" + u.getUsername() + "\",");
/* 109 */         sb.append("\"email\":\"" + u.getEmail() + "\",");
/* 110 */         sb.append("\"active\":\"" + u.isActive() + "\"");
/* 111 */         sb.append("}");
/* 112 */         if (x < i) {
/* 113 */           sb.append(",");
/* 114 */           x++;
/*     */         } 
/*     */       } 
/*     */       
/* 118 */       sb.append("]");
/*     */       
/* 120 */       String response = sb.toString();
/* 121 */       return ResponseEntity.ok().body(response);
/*     */     } 
/* 123 */     return ResponseEntity.status(401).body("{\"message\":\"You are not authorized.\"}");
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/api/user/{id}"}, method = {RequestMethod.GET}, produces = {"application/json"})
/*     */   public ResponseEntity<String> getUsers(HttpServletRequest request, @PathVariable("id") String id) {
/* 129 */     if (isAuthorized(request)) {
/* 130 */       String response = "";
/* 131 */       int code = 0;
/*     */       try {
/* 133 */         User user = this.userDao.getUserById(Integer.parseInt(id));
/*     */         
/* 135 */         StringBuilder sb = new StringBuilder();
/* 136 */         sb.append("{");
/* 137 */         sb.append("\"id\":\"" + user.getId() + "\",");
/* 138 */         sb.append("\"username\":\"" + user.getUsername() + "\",");
/* 139 */         sb.append("\"email\":\"" + user.getEmail() + "\",");
/* 140 */         sb.append("\"active\":\"" + user.isActive() + "\"");
/* 141 */         sb.append("}");
/* 142 */         response = sb.toString();
/* 143 */         code = 200;
/* 144 */       } catch (Exception e) {
/* 145 */         logger.warn("User lookup failed.");
/* 146 */         logger.warn(e.getLocalizedMessage());
/* 147 */         code = 404;
/* 148 */         response = "{\"message\":\"No user found..\"}";
/*     */       } 
/*     */       
/* 151 */       return ResponseEntity.status(code).body(response);
/*     */     } 
/* 153 */     return ResponseEntity.status(401).body("{\"message\":\"You are not authorized.\"}");
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/api/user/{id}/ban"}, method = {RequestMethod.POST}, produces = {"application/json"})
/*     */   public ResponseEntity<String> banUser(HttpServletRequest request, @PathVariable("id") String sid) {
/* 159 */     if (isAuthorized(request)) {
/*     */       
/* 161 */       User user = null;
/* 162 */       int code = 0;
/* 163 */       String message = "";
/* 164 */       String id = SqlUtil.escapeString(sid);
/*     */       
/*     */       try {
/* 167 */         user = this.userDao.getUserById(Integer.parseInt(id));
/*     */         
/* 169 */         if (!user.isAdmin()) {
/* 170 */           this.userDao.banUser(Integer.parseInt(id));
/* 171 */           code = 200;
/* 172 */           message = "{\"message\":\"User banned.\"}";
/*     */         } else {
/* 174 */           code = 400;
/* 175 */           message = "{\"message\":\"Cannot ban admin user.\"}";
/*     */         } 
/* 177 */       } catch (Exception e) {
/* 178 */         logger.error("Failed to ban userid : " + id);
/* 179 */         logger.error(e.getLocalizedMessage());
/* 180 */         code = 500;
/* 181 */         message = "{\"message\":\"An exception occurred.\"}";
/*     */       } 
/*     */       
/* 184 */       return ResponseEntity.status(code).body(message);
/*     */     } 
/* 186 */     return ResponseEntity.status(401).body("{\"message\":\"You are not authorized.\"}");
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/api/user/{id}/activate"}, method = {RequestMethod.POST}, produces = {"application/json"})
/*     */   public ResponseEntity<String> activateUser(HttpServletRequest request, @PathVariable("id") String sid) {
/* 192 */     if (isAuthorized(request)) {
/*     */       
/* 194 */       int code = 0;
/* 195 */       String message = "";
/* 196 */       String id = SqlUtil.escapeString(sid);
/*     */       try {
/* 198 */         this.userDao.activateUser(id);
/* 199 */         code = 200;
/* 200 */         message = "{\"message\":\"User activated.\"}";
/* 201 */       } catch (Exception e) {
/* 202 */         logger.error("Failed to activate userid : " + id);
/* 203 */         logger.error(e.getLocalizedMessage());
/* 204 */         code = 500;
/* 205 */         message = "{\"message\":\"An exception occurred.\"}";
/*     */       } 
/*     */       
/* 208 */       return ResponseEntity.status(code).body(message);
/*     */     } 
/* 210 */     return ResponseEntity.status(401).body("{\"message\":\"You are not authorized.\"}");
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isAuthorized(HttpServletRequest req) {
/* 215 */     return isValidKey(getKeyFromRequest(req));
/*     */   }
/*     */   
/*     */   private String getKeyFromRequest(HttpServletRequest req) {
/* 219 */     return (req.getParameter("apiKey") != null) ? req.getParameter("apiKey") : "";
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isValidKey(String inKey) {
/* 224 */     return this.cfgService.getApiKey().equals(inKey);
/*     */   }
/*     */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/api/ApiController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */