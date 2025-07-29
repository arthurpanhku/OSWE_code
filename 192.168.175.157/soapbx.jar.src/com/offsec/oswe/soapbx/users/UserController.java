/*     */ package com.offsec.oswe.soapbx.users;
/*     */ 
/*     */ import com.offsec.oswe.soapbx.SoapbxBaseController;
/*     */ import com.offsec.oswe.soapbx.categories.Category;
/*     */ import com.offsec.oswe.soapbx.services.FileService;
/*     */ import com.offsec.oswe.soapbx.util.Password;
/*     */ import com.offsec.oswe.soapbx.util.TokenUtil;
/*     */ import java.util.List;
/*     */ import javax.mail.MessagingException;
/*     */ import javax.mail.internet.MimeMessage;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.mail.javamail.JavaMailSender;
/*     */ import org.springframework.mail.javamail.MimeMessageHelper;
/*     */ import org.springframework.stereotype.Controller;
/*     */ import org.springframework.ui.Model;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Controller
/*     */ public class UserController
/*     */   extends SoapbxBaseController
/*     */ {
/*  41 */   private static final Logger logger = LoggerFactory.getLogger(UserController.class);
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   UserDao userDao;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private JavaMailSender mailSender;
/*     */   
/*     */   @Autowired
/*     */   FileService fileService;
/*     */ 
/*     */   
/*     */   @GetMapping({"/login"})
/*     */   public String getLoginPage(HttpServletRequest req, Model model, HttpServletResponse res) {
/*  57 */     List<Category> categories = this.catDao.getAllCategories();
/*  58 */     model.addAttribute("categories", categories);
/*  59 */     String page = "login";
/*     */     
/*  61 */     if (req.getCookies() != null) {
/*  62 */       for (Cookie c : req.getCookies()) {
/*  63 */         if (c.getName().equalsIgnoreCase("rememberme")) {
/*  64 */           if (this.sessionService.isValidRememberMe(c.getValue())) {
/*     */             try {
/*  66 */               User u = this.userDao.getUserById(this.sessionService.getIdFromRememberMe(c.getValue()));
/*  67 */               this.sessionService.setupSessionState(req, u);
/*     */               
/*  69 */               if (u.isAdmin()) {
/*  70 */                 page = "redirect:/admin";
/*     */               }
/*     */               
/*  73 */               page = "redirect:/homepage";
/*     */             }
/*  75 */             catch (Exception e) {
/*  76 */               page = "login";
/*     */             } 
/*     */           } else {
/*  79 */             res.addCookie(new Cookie("rememberme", ""));
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*  86 */     return page;
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping({"/login"})
/*     */   public String postLogin(HttpServletRequest req, Model model, HttpServletResponse res) {
/*  92 */     if (req.getParameter("username") == null || req.getParameter("password") == null) {
/*  93 */       model.addAttribute("message", "Please enter a valid username and password");
/*  94 */       return "login";
/*     */     } 
/*  96 */     String username = req.getParameter("username");
/*  97 */     String password = req.getParameter("password");
/*     */     
/*     */     try {
/* 100 */       User u = this.userDao.getUserByName(username);
/*     */       
/* 102 */       if (!u.isActive()) {
/* 103 */         model.addAttribute("message", "Your account is currently inactive. Please contact your Soapbx instance administrator to reactivate it.");
/* 104 */         return "login";
/*     */       } 
/*     */       
/* 107 */       if (Password.comparePasswords(password, u.getPassword())) {
/* 108 */         this.sessionService.setupSessionState(req, u);
/* 109 */         model.addAttribute("user", u.getUsername());
/* 110 */         model.addAttribute("isAdmin", Boolean.valueOf(u.isAdmin()));
/*     */         
/* 112 */         logger.info("Successful login for " + u.getUsername());
/* 113 */         boolean remember = false;
/* 114 */         if (req.getParameter("rememberme") != null) {
/*     */           try {
/* 116 */             remember = Boolean.parseBoolean(req.getParameter("rememberme"));
/* 117 */           } catch (Exception exception) {}
/*     */         }
/*     */ 
/*     */         
/* 121 */         if (remember) {
/* 122 */           Cookie c = this.sessionService.createRememberMe(u);
/* 123 */           c.setHttpOnly(true);
/* 124 */           res.addCookie(c);
/*     */         } 
/* 126 */         if (u.isAdmin()) {
/* 127 */           return "redirect:/admin";
/*     */         }
/*     */         
/* 130 */         return "redirect:/homepage";
/*     */       } 
/* 132 */       model.addAttribute("message", "Please enter a valid username and password");
/* 133 */       model.addAttribute("displaymagiclink", Boolean.valueOf(true));
/* 134 */       model.addAttribute("username", username);
/* 135 */       return "login";
/*     */     }
/* 137 */     catch (Exception e) {
/* 138 */       model.addAttribute("message", "Please enter a valid username and password");
/* 139 */       return "login";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/logout"})
/*     */   public String getLogout(HttpServletRequest req, Model model, HttpServletResponse res) {
/* 149 */     HttpSession session = req.getSession();
/*     */     
/* 151 */     logger.info("Logout for " + session.getAttribute("username"));
/*     */     
/* 153 */     session.removeAttribute("authenticated");
/* 154 */     session.removeAttribute("username");
/* 155 */     session.removeAttribute("userid");
/* 156 */     session.removeAttribute("isAdmin");
/* 157 */     session.invalidate();
/*     */     
/* 159 */     return "redirect:/index";
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping({"/generateMagicLink"})
/*     */   public String postGenerateMagicLink(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirAttrs) {
/* 165 */     if (req.getParameter("username") != null) {
/*     */       try {
/* 167 */         User u = this.userDao.getUserByName(req.getParameter("username"));
/* 168 */         logger.info("Generating magic link for " + u.getUsername());
/*     */         
/* 170 */         String magicLink = TokenUtil.createToken(u.getId());
/*     */         
/* 172 */         this.userDao.insertTokenForUser(magicLink, u.getId());
/*     */ 
/*     */         
/* 175 */         emailMagicLink(u.getEmail(), magicLink);
/*     */         
/* 177 */         redirAttrs.addFlashAttribute("username", u.getUsername());
/* 178 */         redirAttrs.addFlashAttribute("message", "Magic link sent! Please check your email.");
/*     */         
/* 180 */         return "redirect:/login";
/* 181 */       } catch (Exception e) {
/* 182 */         logger.error("Exception occured looking up user during magic link generation: " + e.getLocalizedMessage());
/* 183 */         return "redirect:/login";
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 188 */     return "redirect:/login";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/magicLink/{token}"})
/*     */   public String processMagicLink(HttpServletRequest req, Model model, HttpServletResponse res, @PathVariable("token") String token) {
/*     */     try {
/* 200 */       Integer userId = this.userDao.getUserIdForToken(token);
/* 201 */       if (userId != null)
/*     */       {
/* 203 */         User u = this.userDao.getUserById(userId.intValue());
/*     */         
/* 205 */         logger.info("Processing magic token for " + u.getUsername());
/*     */         
/* 207 */         req.getSession().setAttribute("authenticated", Boolean.valueOf(true));
/* 208 */         req.getSession().setAttribute("username", u.getUsername());
/* 209 */         req.getSession().setAttribute("userid", Integer.valueOf(u.getId()));
/* 210 */         req.getSession().setAttribute("isAdmin", Boolean.valueOf(u.isAdmin()));
/* 211 */         req.getSession().setMaxInactiveInterval(600);
/* 212 */         model.addAttribute("user", u.getUsername());
/* 213 */         model.addAttribute("isAdmin", Boolean.valueOf(u.isAdmin()));
/* 214 */         this.userDao.deleteTokensForUser(userId.intValue());
/*     */       }
/*     */     
/* 217 */     } catch (Exception e) {
/* 218 */       logger.info("Invalid magic link");
/*     */     } 
/*     */     
/* 221 */     return "redirect:/";
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/user/changePassword"})
/*     */   public String getChangePassword(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirAttrs) {
/* 227 */     if (this.sessionService.isAuthenticated(req)) {
/* 228 */       this.sessionService.decorateModel(req, model);
/* 229 */       return "changepassword";
/*     */     } 
/* 231 */     redirAttrs.addFlashAttribute("message", "You must be logged in to do that.");
/* 232 */     return "redirect:/login";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/user/updatePassword"})
/*     */   public String updatePassword(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirAttrs) {
/* 241 */     if (this.sessionService.isAuthenticated(req)) {
/* 242 */       int userId = ((Integer)req.getSession(false).getAttribute("userid")).intValue();
/* 243 */       String oldPass = (req.getParameter("oldPass") != null) ? req.getParameter("oldPass") : "";
/* 244 */       String newPass = (req.getParameter("newPass") != null) ? req.getParameter("newPass") : "";
/*     */       
/*     */       try {
/* 247 */         User u = this.userDao.getUserById(userId);
/*     */ 
/*     */         
/* 250 */         if (Password.comparePasswords(oldPass, u.getPassword())) {
/* 251 */           logger.info("Updating password for " + u.getUsername());
/*     */           
/* 253 */           this.userDao.updatePasswordForUser(userId, Password.hashPassword(newPass));
/*     */         } else {
/* 255 */           this.sessionService.logout(req);
/* 256 */           return "redirect:/";
/*     */         } 
/* 258 */       } catch (Exception e) {
/* 259 */         logger.error("[!] Exception occured in UserController.updatePassword  -" + e.getMessage());
/* 260 */         return "redirect:/";
/*     */       } 
/*     */     } else {
/*     */       
/* 264 */       redirAttrs.addFlashAttribute("message", "You must be logged in to do that.");
/* 265 */       return "redirect:/login";
/*     */     } 
/*     */     
/* 268 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/user/uploadImage"})
/*     */   public String getUploadImage(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirAttrs) {
/* 274 */     if (this.sessionService.isAuthenticated(req)) {
/* 275 */       this.sessionService.decorateModel(req, model);
/* 276 */       return "upload";
/*     */     } 
/* 278 */     redirAttrs.addFlashAttribute("message", "You must be logged in to do that.");
/* 279 */     return "redirect:/login";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/user/uploadImage"})
/*     */   public String postUploadImage(HttpServletRequest req, Model model, HttpServletResponse res, @RequestParam("file") MultipartFile file, RedirectAttributes redirAttrs) {
/* 287 */     if (this.sessionService.isAuthenticated(req)) {
/* 288 */       this.sessionService.decorateModel(req, model);
/*     */       
/* 290 */       if (!file.getContentType().equalsIgnoreCase("image/jpeg")) {
/* 291 */         model.addAttribute("message", "Only JPEGs are supported.");
/* 292 */         return "upload";
/*     */       } 
/*     */       
/*     */       try {
/* 296 */         this.fileService.uploadFile(file, this.sessionService.getUsername(req));
/* 297 */         redirAttrs.addFlashAttribute("message", "Image uploaded!");
/* 298 */         return "redirect:/homepage";
/* 299 */       } catch (Exception e) {
/* 300 */         logger.error("Exception occurred uploading image: " + e.getMessage());
/* 301 */         model.addAttribute("message", "An error occurred. Please try again later.");
/*     */ 
/*     */         
/* 304 */         return "upload";
/*     */       } 
/* 306 */     }  redirAttrs.addFlashAttribute("message", "You must be logged in to do that.");
/* 307 */     return "redirect:/login";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/user/{id}/avatar"})
/*     */   public ResponseEntity<byte[]> getImage(@PathVariable("id") int id) {
/*     */     try {
/* 315 */       User u = this.userDao.getUserById(id);
/* 316 */       String filename = this.fileService.getFileNameForUser(u.getUsername());
/* 317 */       byte[] image = this.fileService.getFile(filename);
/* 318 */       HttpHeaders headers = new HttpHeaders();
/* 319 */       headers.add("Cache-Control", "max-age=6000");
/* 320 */       return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(headers)).contentType(MediaType.IMAGE_JPEG).body(image);
/* 321 */     } catch (Exception e) {
/* 322 */       logger.debug(e.getMessage());
/*     */       try {
/* 324 */         byte[] image = this.fileService.getFile("default");
/* 325 */         return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
/* 326 */       } catch (Exception ee) {
/* 327 */         return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new byte[0]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void emailMagicLink(String email, String token) {
/*     */     try {
/* 335 */       MimeMessage msg = this.mailSender.createMimeMessage();
/*     */ 
/*     */       
/* 338 */       MimeMessageHelper helper = new MimeMessageHelper(msg, false);
/* 339 */       helper.setTo(email);
/* 340 */       helper.setSubject("Soapbx login");
/* 341 */       helper.setText("Your session awaits. Just click <a href='http://soapbx/magicLink/" + token + "'>here</a> to get signed in automatically.", true);
/*     */       
/* 343 */       logger.info("Sent magic link to " + email);
/*     */       
/* 345 */       this.mailSender.send(msg);
/* 346 */     } catch (MessagingException e) {
/* 347 */       logger.error("[!] Exception occured in UserController.emailMagicLink - couldn't send email - " + e.getMessage());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/users/UserController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */