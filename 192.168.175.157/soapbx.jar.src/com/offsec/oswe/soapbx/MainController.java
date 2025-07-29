/*     */ package com.offsec.oswe.soapbx;
/*     */ 
/*     */ import com.offsec.oswe.soapbx.categories.Category;
/*     */ import com.offsec.oswe.soapbx.comments.CommentDao;
/*     */ import com.offsec.oswe.soapbx.services.FileService;
/*     */ import com.offsec.oswe.soapbx.stories.DecoratedStory;
/*     */ import com.offsec.oswe.soapbx.users.User;
/*     */ import com.offsec.oswe.soapbx.users.UserDao;
/*     */ import com.offsec.oswe.soapbx.util.Password;
/*     */ import com.offsec.oswe.soapbx.util.StringUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.stereotype.Controller;
/*     */ import org.springframework.ui.Model;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Controller
/*     */ public class MainController
/*     */   extends SoapbxBaseController
/*     */ {
/*  35 */   private static final Logger logger = LoggerFactory.getLogger(MainController.class);
/*     */   
/*     */   @Autowired
/*     */   CommentDao commentDao;
/*     */   
/*     */   @Autowired
/*     */   UserDao userDao;
/*     */   
/*     */   @Autowired
/*     */   FileService fileService;
/*     */ 
/*     */   
/*     */   @GetMapping({"/"})
/*     */   public String getDefaultIndex(HttpServletRequest req, Model model, HttpServletResponse res) {
/*  49 */     decorateModel(req, model);
/*     */     
/*  51 */     model.addAttribute("page", Integer.valueOf(0));
/*     */     
/*  53 */     List<DecoratedStory> topFive = this.storyDao.getTopFiveStoriesDecorated();
/*  54 */     List<Category> categories = this.catDao.getAllCategories();
/*     */     
/*  56 */     model.addAttribute("stories", topFive);
/*  57 */     model.addAttribute("categories", categories);
/*     */     
/*  59 */     getDecoratedTopFive(model);
/*     */     
/*  61 */     return "index";
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/about"})
/*     */   public String getAboutPage(HttpServletRequest req, Model model, HttpServletResponse res) {
/*  67 */     decorateModel(req, model);
/*     */     
/*  69 */     List<Category> categories = this.catDao.getAllCategories();
/*  70 */     model.addAttribute("categories", categories);
/*     */     
/*  72 */     return "about";
/*     */   }
/*     */   
/*     */   @GetMapping({"/index"})
/*     */   public String getIndex(HttpServletRequest req, Model model, HttpServletResponse res) {
/*     */     List<DecoratedStory> topFive;
/*  78 */     int pageNum = 0;
/*  79 */     if (req.getParameter("page") != null) {
/*     */       try {
/*  81 */         pageNum = Integer.parseInt(req.getParameter("page"));
/*  82 */       } catch (Exception e) {
/*  83 */         pageNum = 0;
/*     */       } 
/*     */     }
/*     */     
/*  87 */     decorateModel(req, model);
/*     */     
/*  89 */     List<Category> categories = this.catDao.getAllCategories();
/*  90 */     model.addAttribute("categories", categories);
/*     */ 
/*     */ 
/*     */     
/*  94 */     if (pageNum == 0) {
/*  95 */       topFive = this.storyDao.getTopFiveStoriesDecorated();
/*     */     } else {
/*  97 */       topFive = this.storyDao.getTopFiveStoriesDecoratedOffset(pageNum * 5);
/*     */     } 
/*     */ 
/*     */     
/* 101 */     model.addAttribute("page", Integer.valueOf(pageNum));
/* 102 */     model.addAttribute("stories", topFive);
/*     */     
/* 104 */     getDecoratedTopFive(model);
/*     */ 
/*     */     
/* 107 */     return "index";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/signup"})
/*     */   public String postSignUp(HttpServletRequest req, Model model, HttpServletResponse res) {
/* 115 */     if (isAuthenticated(req)) {
/* 116 */       return "redirect:/homepage";
/*     */     }
/*     */     
/* 119 */     if (req.getParameter("username") == null || req.getParameter("password") == null || req.getParameter("email") == null) {
/* 120 */       model.addAttribute("message", "Please enter an email, username, and password");
/*     */     } else {
/*     */       
/* 123 */       String email = req.getParameter("email");
/* 124 */       String username = req.getParameter("username");
/* 125 */       String password = req.getParameter("password");
/*     */ 
/*     */       
/* 128 */       User u = null;
/*     */       try {
/* 130 */         u = this.userDao.getUserByName(username);
/* 131 */       } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */       
/* 135 */       if (u != null) {
/* 136 */         model.addAttribute("message", "A user with that name already exists. Please try logging in.");
/* 137 */         return "signup";
/*     */       } 
/*     */       
/* 140 */       if (password.length() < 8) {
/* 141 */         model.addAttribute("message", "Password is too short.");
/* 142 */         return "signup";
/*     */       } 
/*     */       
/*     */       try {
/* 146 */         String encPass = Password.hashPassword(password);
/* 147 */         this.userDao.insertUser(username, encPass, false, true, email);
/*     */         
/* 149 */         logger.info("New user created: " + username);
/*     */         
/* 151 */         u = this.userDao.getUserByName(username);
/*     */         
/* 153 */         req.getSession().setAttribute("authenticated", Boolean.valueOf(true));
/* 154 */         req.getSession().setAttribute("userid", Integer.valueOf(u.getId()));
/* 155 */         req.getSession().setAttribute("username", u.getUsername());
/* 156 */         req.getSession().setAttribute("isAdmin", Boolean.valueOf(u.isAdmin()));
/* 157 */         req.getSession().setMaxInactiveInterval(600);
/* 158 */         model.addAttribute("user", u.getUsername());
/* 159 */         model.addAttribute("isAdmin", Boolean.valueOf(u.isAdmin()));
/*     */         
/* 161 */         return "redirect:/";
/*     */       }
/* 163 */       catch (Exception e) {
/* 164 */         logger.error("Failed to hash a password: " + e.getMessage());
/* 165 */         model.addAttribute("message", "An error occurred. Please try again later.");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 170 */     decorateModel(req, model);
/*     */     
/* 172 */     List<Category> categories = this.catDao.getAllCategories();
/* 173 */     model.addAttribute("categories", categories);
/*     */ 
/*     */     
/* 176 */     return "signup";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/getStarted"})
/*     */   public String getSignUpPage(HttpServletRequest req, Model model, HttpServletResponse res) {
/* 183 */     if (isAuthenticated(req)) {
/* 184 */       return "redirect:/homepage";
/*     */     }
/*     */     
/* 187 */     decorateModel(req, model);
/*     */     
/* 189 */     List<Category> categories = this.catDao.getAllCategories();
/* 190 */     model.addAttribute("categories", categories);
/*     */     
/* 192 */     return "signup";
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/search"})
/*     */   public String postSearch(HttpServletRequest req, Model model, HttpServletResponse res) {
/* 198 */     decorateModel(req, model);
/*     */     
/* 200 */     List<Category> categories = this.catDao.getAllCategories();
/* 201 */     model.addAttribute("categories", categories);
/*     */     
/* 203 */     String keyword = "";
/* 204 */     if (req.getParameter("query") != null) {
/* 205 */       keyword = req.getParameter("query");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 210 */     if (keyword.equalsIgnoreCase("")) {
/* 211 */       return "redirect:/index";
/*     */     }
/* 213 */     List<DecoratedStory> stories = this.storyDao.searchForStories(keyword);
/* 214 */     model.addAttribute("stories", stories);
/*     */     
/* 216 */     if (stories.size() == 0) {
/* 217 */       model.addAttribute("message", "Sorry, we couldn't find anything that matched your search.");
/*     */     }
/*     */ 
/*     */     
/* 221 */     getDecoratedTopFive(model);
/*     */     
/* 223 */     return "index";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/homepage"})
/*     */   public String getHomePage(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirectAttributes) {
/* 230 */     if (isAuthenticated(req)) {
/* 231 */       decorateModel(req, model);
/*     */       
/*     */       try {
/* 234 */         int userid = ((Integer)req.getSession().getAttribute("userid")).intValue();
/*     */         
/* 236 */         User u = this.userDao.getUserById(userid);
/*     */         
/* 238 */         int storyCount = this.storyDao.getStoryCountForUser(u.getId()).intValue();
/*     */         
/* 240 */         int commentCount = this.commentDao.getCommentCountForUser(u.getId()).intValue();
/*     */ 
/*     */ 
/*     */         
/* 244 */         List<DecoratedStory> stories = new ArrayList<>();
/*     */         try {
/* 246 */           stories = this.storyDao.getAllStoriesForUser(u.getId());
/* 247 */         } catch (Exception eee) {
/* 248 */           logger.error("Exception occured fetching user's homepage: " + eee.getLocalizedMessage());
/* 249 */           model.addAttribute("message", "An error occurred. Please try again later.");
/*     */         } 
/*     */         
/* 252 */         model.addAttribute("user", u);
/* 253 */         model.addAttribute("storyCount", Integer.valueOf(storyCount));
/* 254 */         model.addAttribute("stories", stories);
/* 255 */         model.addAttribute("commentCount", Integer.valueOf(commentCount));
/*     */       }
/* 257 */       catch (Exception e) {
/* 258 */         redirectAttributes.addFlashAttribute("message", "An error occurred. Please try again later.");
/* 259 */         logger.error("Exception occured fetching user's homepage: " + e.getLocalizedMessage());
/* 260 */         return "redirect:/index";
/*     */       } 
/*     */       
/* 263 */       return "homepage";
/*     */     } 
/*     */ 
/*     */     
/* 267 */     return "redirect:/index";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/error"})
/*     */   public String getErrorPage(HttpServletRequest req, Model model, HttpServletResponse res) {
/* 275 */     return "error";
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/image"})
/*     */   public ResponseEntity<byte[]> getImage(@RequestParam("id") String id) {
/*     */     try {
/* 282 */       byte[] image = this.fileService.getFile(StringUtil.cleanPath(id));
/* 283 */       return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
/* 284 */     } catch (Exception e) {
/* 285 */       logger.error(e.getMessage());
/*     */       try {
/* 287 */         byte[] image = this.fileService.getFile("default");
/* 288 */         return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
/* 289 */       } catch (Exception ee) {
/* 290 */         return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new byte[0]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/profile/{id}"})
/*     */   public String getProfile(HttpServletRequest req, Model model, HttpServletResponse res, @PathVariable("id") String id, RedirectAttributes redirectAttributes) {
/* 300 */     decorateModel(req, model);
/*     */     
/* 302 */     getDecoratedTopFive(model);
/*     */     
/*     */     try {
/* 305 */       int userId = Integer.parseInt(id);
/*     */       
/* 307 */       User u = this.userDao.getUserById(userId);
/*     */ 
/*     */       
/* 310 */       int commentCount = this.commentDao.getCommentCountForUser(userId).intValue();
/*     */       
/* 312 */       int storyCount = this.storyDao.getStoryCountForUser(userId).intValue();
/*     */ 
/*     */       
/* 315 */       model.addAttribute("user", u);
/* 316 */       model.addAttribute("commentCount", Integer.valueOf(commentCount));
/* 317 */       model.addAttribute("storyCount", Integer.valueOf(storyCount));
/* 318 */       model.addAttribute("image", this.fileService.getFileNameForUser(u.getUsername()));
/*     */     }
/* 320 */     catch (NumberFormatException nfe) {
/* 321 */       redirectAttributes.addFlashAttribute("message", "Invalid user identifier.");
/* 322 */       return "redirect:/index";
/*     */     }
/* 324 */     catch (Exception e) {
/* 325 */       redirectAttributes.addFlashAttribute("message", "Invalid user identifier.");
/* 326 */       return "redirect:/index";
/*     */     } 
/*     */ 
/*     */     
/* 330 */     return "profile";
/*     */   }
/*     */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/MainController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */