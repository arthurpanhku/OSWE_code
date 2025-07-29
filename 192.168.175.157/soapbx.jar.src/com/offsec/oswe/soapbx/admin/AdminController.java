/*     */ package com.offsec.oswe.soapbx.admin;
/*     */ 
/*     */ import com.offsec.oswe.soapbx.SoapbxBaseController;
/*     */ import com.offsec.oswe.soapbx.categories.Category;
/*     */ import com.offsec.oswe.soapbx.categories.DecoratedCategory;
/*     */ import com.offsec.oswe.soapbx.comments.CommentDao;
/*     */ import com.offsec.oswe.soapbx.stories.DecoratedStory;
/*     */ import com.offsec.oswe.soapbx.users.User;
/*     */ import com.offsec.oswe.soapbx.users.UserDao;
/*     */ import com.offsec.oswe.soapbx.util.Password;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import javax.mail.MessagingException;
/*     */ import javax.mail.internet.MimeMessage;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.mail.javamail.JavaMailSender;
/*     */ import org.springframework.mail.javamail.MimeMessageHelper;
/*     */ import org.springframework.stereotype.Controller;
/*     */ import org.springframework.ui.Model;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*     */ import org.thymeleaf.TemplateEngine;
/*     */ import org.thymeleaf.context.Context;
/*     */ import org.thymeleaf.context.IContext;
/*     */ import org.thymeleaf.spring5.SpringTemplateEngine;
/*     */ import org.thymeleaf.templateresolver.ITemplateResolver;
/*     */ import org.thymeleaf.templateresolver.StringTemplateResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Controller
/*     */ public class AdminController
/*     */   extends SoapbxBaseController
/*     */ {
/*  49 */   private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
/*     */   
/*     */   @Autowired
/*     */   CommentDao commentDao;
/*     */   
/*     */   @Autowired
/*     */   UserDao userDao;
/*     */   
/*     */   @Autowired
/*     */   AdminDao adminDao;
/*     */   
/*     */   @Autowired
/*     */   private JavaMailSender mailSender;
/*     */   
/*     */   @GetMapping({"/admin"})
/*     */   public String getAdminPage(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirAttrs) {
/*  65 */     if (!isAdmin(req)) {
/*  66 */       redirAttrs.addFlashAttribute("message", "You must be logged in to access this area.");
/*  67 */       return "redirect:/login";
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     this.sessionService.decorateModel(req, model);
/*     */     
/*  91 */     List<Category> categories = this.catDao.getAllCategories();
/*  92 */     model.addAttribute("categories", categories);
/*     */     
/*  94 */     int userCount = this.userDao.getUserCount().intValue();
/*  95 */     model.addAttribute("userCount", Integer.valueOf(userCount));
/*     */     
/*  97 */     int storyCount = this.storyDao.getStoryCount().intValue();
/*  98 */     model.addAttribute("storyCount", Integer.valueOf(storyCount));
/*     */     
/* 100 */     int commentCount = this.commentDao.getCommentCount().intValue();
/* 101 */     model.addAttribute("commentCount", Integer.valueOf(commentCount));
/*     */     
/* 103 */     List<User> users = this.userDao.getAllUsers();
/* 104 */     model.addAttribute("users", users);
/*     */     
/* 106 */     return "admin";
/*     */   }
/*     */   
/*     */   @GetMapping({"/admin/users"})
/*     */   public String getUsersPage(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirAttrs) {
/* 111 */     if (!isAdmin(req)) {
/* 112 */       redirAttrs.addFlashAttribute("message", "You must be logged in to access this area.");
/* 113 */       return "redirect:/login";
/*     */     } 
/*     */     
/* 116 */     this.sessionService.decorateModel(req, model);
/*     */     
/* 118 */     List<User> users = this.userDao.getAllUsers();
/* 119 */     model.addAttribute("users", users);
/*     */     
/* 121 */     return "users";
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"/admin/users/category"}, method = {RequestMethod.GET})
/*     */   public String getUsersByCategory(HttpServletRequest req, Model model, HttpServletResponse res, @RequestParam String id, RedirectAttributes redirAttrs) {
/* 126 */     if (!isAdmin(req)) {
/* 127 */       redirAttrs.addFlashAttribute("message", "You must be logged in to access this area.");
/* 128 */       return "redirect:/login";
/*     */     } 
/*     */     try {
/* 131 */       this.sessionService.decorateModel(req, model);
/*     */       
/* 133 */       List<Category> categories = this.catDao.getAllCategories();
/* 134 */       List<User> users = this.userDao.getUsersWithPostsInCategory(id);
/* 135 */       DecoratedCategory cat = this.catDao.getDecoratedCategoryById(id);
/*     */       
/* 137 */       model.addAttribute("categories", categories);
/* 138 */       model.addAttribute("cat", cat);
/* 139 */       model.addAttribute("users", users);
/*     */     
/*     */     }
/* 142 */     catch (Exception e) {
/* 143 */       model.addAttribute("message", "An error occurred.");
/* 144 */       logger.error(e.getMessage());
/*     */     } 
/*     */     
/* 147 */     return "analytics";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/admin/backupdb"})
/*     */   public String postBackupDB(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirAttrs) {
/* 154 */     if (!isAdmin(req)) {
/* 155 */       redirAttrs.addFlashAttribute("message", "You must be logged in to access this area.");
/* 156 */       return "redirect:/login";
/*     */     } 
/*     */     
/* 159 */     this.sessionService.decorateModel(req, model);
/*     */     
/* 161 */     int storyCount = this.storyDao.getStoryCount().intValue();
/* 162 */     model.addAttribute("storyCount", Integer.valueOf(storyCount));
/*     */     
/* 164 */     int userCount = this.userDao.getUserCount().intValue();
/* 165 */     model.addAttribute("userCount", Integer.valueOf(userCount));
/*     */     
/* 167 */     int commentCount = this.commentDao.getCommentCount().intValue();
/* 168 */     model.addAttribute("commentCount", Integer.valueOf(commentCount));
/*     */     
/* 170 */     List<User> users = this.userDao.getAllUsers();
/* 171 */     model.addAttribute("users", users);
/*     */     
/* 173 */     String filename = (req.getParameter("filename") != null) ? req.getParameter("filename") : "backup";
/*     */     
/* 175 */     ProcessBuilder pb = new ProcessBuilder(new String[0]);
/* 176 */     Path currentPath = Paths.get(".", new String[0]);
/*     */     
/* 178 */     String script = currentPath.toAbsolutePath().normalize().toString() + "/bin/backup.sh";
/*     */     
/* 180 */     pb = new ProcessBuilder(new String[0]);
/* 181 */     pb.command(new String[] { script });
/*     */     try {
/* 183 */       Process process = pb.start();
/* 184 */     } catch (IOException e) {
/* 185 */       logger.error("Exception occurred trying to start database backup. " + e.getMessage());
/*     */     } 
/* 187 */     model.addAttribute("message", "Database backup initiated.");
/*     */     
/* 189 */     return "admin";
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/admin/stories/category"}, method = {RequestMethod.GET})
/*     */   public String getStoriesByCategory(HttpServletRequest req, Model model, HttpServletResponse res, @RequestParam Integer id, RedirectAttributes redirAttrs) {
/* 195 */     if (!isAdmin(req)) {
/* 196 */       redirAttrs.addFlashAttribute("message", "You must be logged in to access this area.");
/* 197 */       return "redirect:/login";
/*     */     } 
/*     */     
/*     */     try {
/* 201 */       this.sessionService.decorateModel(req, model);
/*     */       
/* 203 */       List<Category> categories = this.catDao.getAllCategories();
/* 204 */       model.addAttribute("categories", categories);
/*     */       
/* 206 */       List<DecoratedStory> stories = this.userDao.getStoriesInCategory(id.toString());
/* 207 */       DecoratedCategory cat = this.catDao.getDecoratedCategoryById(id.toString());
/* 208 */       model.addAttribute("cat", cat);
/* 209 */       model.addAttribute("stories", stories);
/* 210 */     } catch (Exception e) {
/* 211 */       model.addAttribute("message", "An error occurred. Please try again later.");
/*     */     } 
/*     */     
/* 214 */     return "analytics";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/admin/users/create"})
/*     */   public String postUserCreate(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirAttrs) {
/* 223 */     if (!isAdmin(req)) {
/* 224 */       redirAttrs.addFlashAttribute("message", "You must be logged in to access this area.");
/* 225 */       return "redirect:/login";
/*     */     } 
/*     */     
/* 228 */     this.sessionService.decorateModel(req, model);
/*     */     
/* 230 */     String username = (req.getParameter("name") != null) ? req.getParameter("name") : "";
/* 231 */     String email = (req.getParameter("email") != null) ? req.getParameter("email") : "";
/* 232 */     boolean isAdmin = (req.getParameter("isAdmin") != null) ? Boolean.parseBoolean(req.getParameter("isAdmin")) : false;
/* 233 */     boolean isMod = (req.getParameter("isMod") != null) ? Boolean.parseBoolean(req.getParameter("isMod")) : false;
/*     */     
/* 235 */     if (username.equalsIgnoreCase("") || email.equalsIgnoreCase("")) {
/* 236 */       model.addAttribute("message", "Missing required fields.");
/* 237 */       return "redirect:/admin/users";
/*     */     } 
/* 239 */     String password = Password.generatePassword(16);
/* 240 */     logger.info("AdminController.postUserCreate() - Creating new user");
/*     */     try {
/* 242 */       String hashedPassword = Password.hashPassword(password);
/* 243 */       this.userDao.insertUser(username, hashedPassword, isAdmin, isMod, email);
/* 244 */       emailNewUser(email, username, password);
/* 245 */       password = "";
/* 246 */     } catch (Exception e) {
/* 247 */       logger.error("[!] Exception occurred while try to add a new user: " + e.getMessage());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 252 */     List<User> users = this.userDao.getAllUsers();
/* 253 */     model.addAttribute("users", users);
/*     */     
/* 255 */     return "users";
/*     */   }
/*     */   
/*     */   @PostMapping({"/admin/user/{id}/ban"})
/*     */   public String adminBanUser(HttpServletRequest req, Model model, HttpServletResponse res, @PathVariable("id") int id, RedirectAttributes redirAttrs) {
/* 260 */     if (!isAdmin(req)) {
/* 261 */       redirAttrs.addFlashAttribute("message", "You must be logged in to access this area.");
/* 262 */       return "redirect:/login";
/*     */     } 
/*     */     
/* 265 */     this.sessionService.decorateModel(req, model);
/*     */     try {
/* 267 */       User u = this.userDao.getUserById(id);
/* 268 */       if (u.isActive()) {
/*     */         
/* 270 */         this.userDao.banUser(id);
/* 271 */         redirAttrs.addFlashAttribute("message", "Userid " + id + " banned.");
/*     */       } else {
/* 273 */         redirAttrs.addFlashAttribute("message", "Userid " + id + " already banned.");
/*     */       } 
/* 275 */     } catch (Exception e) {
/* 276 */       logger.error("Failed to ban userid " + id + ". Is it valid?");
/* 277 */       logger.error(e.getMessage());
/*     */       
/* 279 */       redirAttrs.addFlashAttribute("message", "Invalid user identifier.");
/*     */     } 
/*     */ 
/*     */     
/* 283 */     return "redirect:/admin/users";
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping({"/admin/user/{id}/activate"})
/*     */   public String adminActivateUser(HttpServletRequest req, Model model, HttpServletResponse res, @PathVariable("id") int id, RedirectAttributes redirAttrs) {
/* 289 */     if (!isAdmin(req)) {
/* 290 */       redirAttrs.addFlashAttribute("message", "You must be logged in to access this area.");
/* 291 */       return "redirect:/login";
/*     */     } 
/*     */     
/* 294 */     this.sessionService.decorateModel(req, model);
/*     */     try {
/* 296 */       User u = this.userDao.getUserById(id);
/* 297 */       if (u.isActive()) {
/* 298 */         redirAttrs.addFlashAttribute("message", "Userid " + id + " already active.");
/*     */       }
/*     */       else {
/*     */         
/* 302 */         this.userDao.activateUser(id);
/* 303 */         redirAttrs.addFlashAttribute("message", "Userid " + id + " activated.");
/*     */       } 
/* 305 */     } catch (Exception e) {
/* 306 */       logger.error("Failed to activate userid " + id + ". Is it valid?");
/* 307 */       logger.error(e.getMessage());
/*     */       
/* 309 */       redirAttrs.addFlashAttribute("message", "Invalid user id.");
/*     */     } 
/*     */ 
/*     */     
/* 313 */     return "redirect:/admin/users";
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/admin/welcomeEmail"}, method = {RequestMethod.GET})
/*     */   public String getWelcomeEmail(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirAttrs) {
/* 319 */     if (!isAdmin(req)) {
/* 320 */       redirAttrs.addFlashAttribute("message", "You must be logged in to access this area.");
/* 321 */       return "redirect:/login";
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 326 */       this.sessionService.decorateModel(req, model);
/* 327 */       List<Category> categories = this.catDao.getAllCategories();
/* 328 */       model.addAttribute("categories", categories);
/*     */       
/* 330 */       int userCount = this.userDao.getUserCount().intValue();
/* 331 */       model.addAttribute("userCount", Integer.valueOf(userCount));
/*     */       
/* 333 */       int storyCount = this.storyDao.getStoryCount().intValue();
/* 334 */       model.addAttribute("storyCount", Integer.valueOf(storyCount));
/*     */       
/* 336 */       int commentCount = this.commentDao.getCommentCount().intValue();
/* 337 */       model.addAttribute("commentCount", Integer.valueOf(commentCount));
/*     */       
/* 339 */       List<User> users = this.userDao.getAllUsers();
/* 340 */       model.addAttribute("users", users);
/*     */       
/* 342 */       String content = this.adminDao.getWelcomeEmail();
/* 343 */       model.addAttribute("content", content);
/*     */ 
/*     */       
/* 346 */       return "email";
/*     */     }
/* 348 */     catch (Exception e) {
/* 349 */       logger.error(e.getLocalizedMessage());
/* 350 */       redirAttrs.addFlashAttribute("message", "An error occurred. Please try again later.");
/* 351 */       model.addAttribute("message", "An error occurred. Please try again later.");
/*     */ 
/*     */       
/* 354 */       return "redirect:/admin";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/admin/welcomeEmail/edit"}, method = {RequestMethod.POST})
/*     */   public String postWelcomeEmailEdit(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirAttrs) {
/* 363 */     if (!isAdmin(req)) {
/* 364 */       redirAttrs.addFlashAttribute("message", "You must be logged in to access this area.");
/* 365 */       return "redirect:/login";
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 370 */       this.sessionService.decorateModel(req, model);
/* 371 */       List<Category> categories = this.catDao.getAllCategories();
/* 372 */       model.addAttribute("categories", categories);
/*     */ 
/*     */ 
/*     */       
/* 376 */       String editContent = (req.getParameter("content") != null) ? req.getParameter("content") : "";
/*     */       
/* 378 */       if (!editContent.equalsIgnoreCase("")) {
/* 379 */         this.adminDao.updateWelcomeEmail(editContent);
/* 380 */         redirAttrs.addFlashAttribute("message", "Welcome email updated.");
/*     */       } else {
/* 382 */         redirAttrs.addFlashAttribute("message", "Welcome email content was empty.");
/*     */       } 
/*     */       
/* 385 */       String content = this.adminDao.getWelcomeEmail();
/* 386 */       model.addAttribute("content", content);
/*     */       
/* 388 */       return "redirect:/admin/welcomeEmail";
/*     */     }
/* 390 */     catch (Exception e) {
/* 391 */       model.addAttribute("message", "An error occurred. Please try again later.");
/*     */ 
/*     */       
/* 394 */       return "redirect:/admin";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private TemplateEngine getTemplateEngine() {
/* 400 */     SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
/* 401 */     StringTemplateResolver resolver = new StringTemplateResolver();
/* 402 */     springTemplateEngine.setTemplateResolver((ITemplateResolver)resolver);
/* 403 */     return (TemplateEngine)springTemplateEngine;
/*     */   }
/*     */   
/*     */   private void emailNewUser(String email, String username, String password) {
/*     */     try {
/* 408 */       MimeMessage msg = this.mailSender.createMimeMessage();
/*     */ 
/*     */       
/* 411 */       MimeMessageHelper helper = new MimeMessageHelper(msg, false);
/* 412 */       helper.setTo(email);
/* 413 */       helper.setSubject("Welcome to Soapbx!");
/*     */       
/* 415 */       String emailContent = this.adminDao.getWelcomeEmail();
/*     */       
/* 417 */       TemplateEngine engine = getTemplateEngine();
/* 418 */       Context context = new Context(new Locale("US"));
/* 419 */       context.setVariable("username", username);
/* 420 */       context.setVariable("password", password);
/*     */ 
/*     */       
/* 423 */       helper.setText(engine.process(emailContent, (IContext)context), true);
/*     */       
/* 425 */       this.mailSender.send(msg);
/* 426 */     } catch (MessagingException e) {
/* 427 */       logger.error("[!] Exception occurred in AdminController.emailNewUser - " + e.getMessage());
/* 428 */     } catch (Exception e) {
/* 429 */       logger.error("[!] Exception occurred in AdminController.emailNewUser - " + e.getMessage());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/admin/AdminController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */