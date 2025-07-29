/*     */ package com.offsec.oswe.soapbx.stories;
/*     */ 
/*     */ import com.offsec.oswe.soapbx.SoapbxBaseController;
/*     */ import com.offsec.oswe.soapbx.categories.Category;
/*     */ import com.offsec.oswe.soapbx.comments.CommentDao;
/*     */ import com.offsec.oswe.soapbx.comments.DecoratedComment;
/*     */ import com.offsec.oswe.soapbx.likes.LikeDao;
/*     */ import com.offsec.oswe.soapbx.services.DownloadService;
/*     */ import com.offsec.oswe.soapbx.users.UserDao;
/*     */ import com.offsec.oswe.soapbx.util.SqlUtil;
/*     */ import com.offsec.oswe.soapbx.util.StringUtil;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.http.HttpHeaders;
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
/*     */ public class StoryController
/*     */   extends SoapbxBaseController
/*     */ {
/*  36 */   private static final Logger logger = LoggerFactory.getLogger(StoryController.class);
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   UserDao userDao;
/*     */   
/*     */   @Autowired
/*     */   CommentDao commentDao;
/*     */   
/*     */   @Autowired
/*     */   LikeDao likeDao;
/*     */   
/*     */   @Autowired
/*     */   DownloadService downloadService;
/*     */ 
/*     */   
/*     */   @GetMapping({"/story"})
/*     */   public String getStory(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirectAttributes) {
/*  54 */     if (!isAuthenticated(req)) {
/*  55 */       redirectAttributes.addFlashAttribute("message", "Must be logged in to post stories!");
/*  56 */       return "redirect:/login";
/*     */     } 
/*  58 */     decorateModel(req, model);
/*     */     
/*  60 */     getDecoratedCategories(model);
/*     */     
/*  62 */     return "story";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/story"})
/*     */   public String postStory(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirectAttributes) {
/*     */     int categoryId;
/*  70 */     if (!isAuthenticated(req)) {
/*  71 */       redirectAttributes.addFlashAttribute("message", "Must be logged in to post stories!");
/*  72 */       return "redirect:/login";
/*     */     } 
/*     */ 
/*     */     
/*  76 */     String title = (req.getParameter("title") != null) ? req.getParameter("title") : "";
/*  77 */     String content = (req.getParameter("content") != null) ? req.getParameter("content") : "";
/*  78 */     boolean isDraft = (req.getParameter("draft") != null) ? Boolean.parseBoolean(req.getParameter("draft")) : false;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  83 */       categoryId = Integer.parseInt(req.getParameter("category"));
/*  84 */     } catch (Exception e) {
/*  85 */       categoryId = 0;
/*     */     } 
/*  87 */     boolean needsMod = false;
/*  88 */     boolean isActive = !isDraft;
/*     */     
/*  90 */     int ownerId = this.sessionService.getUserId(req);
/*     */     
/*  92 */     if (title.equalsIgnoreCase("") || content.equalsIgnoreCase("") || categoryId == 0) {
/*  93 */       model.addAttribute("message", "Please fill out all required fields.");
/*  94 */       decorateModel(req, model);
/*  95 */       getDecoratedCategories(model);
/*  96 */       return "story";
/*     */     } 
/*  98 */     content = SqlUtil.escapeString(content);
/*  99 */     content = StringUtil.cleanText(content);
/* 100 */     logger.info("Creating new story");
/* 101 */     this.storyDao.insertStory(title, content, ownerId, categoryId, needsMod, isActive);
/*     */ 
/*     */     
/* 104 */     decorateModel(req, model);
/* 105 */     getDecoratedCategories(model);
/* 106 */     redirectAttributes.addFlashAttribute("message", "Story submitted!");
/* 107 */     return "redirect:/homepage";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/story/{id}"})
/*     */   public String getStoryThread(HttpServletRequest req, Model model, HttpServletResponse res, @PathVariable("id") String id, RedirectAttributes redirectAttributes) {
/* 116 */     decorateModel(req, model);
/*     */     
/* 118 */     getDecoratedTopFive(model);
/*     */     
/* 120 */     List<Category> categories = this.catDao.getAllCategories();
/* 121 */     model.addAttribute("categories", categories);
/*     */     
/*     */     try {
/* 124 */       int storyId = Integer.parseInt(id);
/*     */       
/* 126 */       DecoratedStory s = this.storyDao.getDecoratedStoryById(storyId);
/*     */       
/* 128 */       if (!s.isActive()) {
/* 129 */         model.addAttribute("message", "Invalid story identifier.");
/* 130 */         return "redirect:/index";
/*     */       } 
/* 132 */       List<DecoratedComment> comments = this.commentDao.getDecoratedCommentsForStory(storyId);
/*     */       
/* 134 */       model.addAttribute("story", s);
/*     */ 
/*     */       
/* 137 */       model.addAttribute("comments", comments);
/* 138 */     } catch (NumberFormatException nfe) {
/* 139 */       redirectAttributes.addFlashAttribute("message", "Invalid thread identifier.");
/* 140 */       return "redirect:/index";
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 145 */     return "thread";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/story/{id}/download"})
/*     */   public String getStoryDownload(HttpServletRequest req, Model model, HttpServletResponse res, @PathVariable("id") String id, RedirectAttributes redirectAttributes) {
/*     */     try {
/* 153 */       decorateModel(req, model);
/*     */       
/* 155 */       getDecoratedTopFive(model);
/*     */       
/* 157 */       int storyId = Integer.parseInt(id);
/*     */       
/* 159 */       DecoratedStory s = this.storyDao.getDecoratedStoryById(storyId);
/*     */       
/* 161 */       if (!s.isActive()) {
/* 162 */         redirectAttributes.addFlashAttribute("message", "Invalid story identifier.");
/* 163 */         return "redirect:/index";
/*     */       } 
/*     */       
/* 166 */       String filename = this.downloadService.createPDFFromStory(s);
/* 167 */       String link = "/download?id=" + filename;
/* 168 */       if (!filename.equalsIgnoreCase("")) {
/* 169 */         model.addAttribute("link", link);
/* 170 */         return "download";
/*     */       } 
/* 172 */       redirectAttributes.addFlashAttribute("message", "An error occurred. Please try again later.");
/* 173 */       return "redirect:/";
/*     */     }
/* 175 */     catch (Exception e) {
/* 176 */       logger.error("Exception occurred in StoryController.getStoryDownload: " + e.getLocalizedMessage());
/* 177 */       redirectAttributes.addFlashAttribute("message", "An error occurred. Please try again later.");
/* 178 */       return "redirect:/";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/download"})
/*     */   public ResponseEntity<byte[]> getImage(@RequestParam("id") String id) {
/*     */     try {
/* 187 */       HttpHeaders headers = new HttpHeaders();
/*     */       
/* 189 */       headers.add("Cache-Control", "no-cache, no-store");
/* 190 */       headers.add("Content-Disposition", "attachment; filename=soapbx.pdf");
/*     */       
/* 192 */       byte[] image = this.downloadService.getPDF(id.replace("../", ""));
/*     */       
/* 194 */       return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(headers)).contentType(MediaType.APPLICATION_PDF).body(image);
/* 195 */     } catch (Exception e) {
/* 196 */       logger.error(e.getMessage());
/* 197 */       return ResponseEntity.notFound().build();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/story/{sid}/comment"})
/*     */   public String postCommentToStory(HttpServletRequest req, Model model, HttpServletResponse res, @PathVariable("sid") String sid, RedirectAttributes redirectAttributes) {
/* 205 */     if (!isAuthenticated(req)) {
/* 206 */       redirectAttributes.addFlashAttribute("message", "Must be logged in to post comments!");
/* 207 */       return "redirect:/login";
/*     */     } 
/* 209 */     String content = (req.getParameter("comment") != null) ? req.getParameter("comment") : "";
/* 210 */     int storyId = Integer.parseInt(sid);
/* 211 */     int ownerId = ((Integer)req.getSession(false).getAttribute("userid")).intValue();
/*     */     
/* 213 */     if (content.equalsIgnoreCase("")) {
/* 214 */       redirectAttributes.addFlashAttribute("message", "Must be provide a comment!");
/* 215 */       return "redirect:/thread/" + storyId;
/*     */     } 
/*     */ 
/*     */     
/* 219 */     Story s = this.storyDao.getDecoratedStoryById(storyId);
/* 220 */     if (s.isActive()) {
/* 221 */       content = StringUtil.cleanText(content);
/* 222 */       this.commentDao.addCommentToStory(content, ownerId, storyId);
/* 223 */       logger.info("Adding comment to story id " + storyId);
/* 224 */       return "redirect:/thread/" + storyId;
/*     */     } 
/* 226 */     redirectAttributes.addFlashAttribute("message", "Couldn't find that story. Sorry.");
/* 227 */     return "redirect:/";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/story/{id}/edit"})
/*     */   public String getStoryEdit(HttpServletRequest req, Model model, HttpServletResponse res, @PathVariable("id") String id, RedirectAttributes redirectAttributes) {
/* 237 */     if (!isAuthenticated(req)) {
/* 238 */       redirectAttributes.addFlashAttribute("message", "Must be logged in to edit drafts!");
/* 239 */       return "redirect:/login";
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 244 */       int storyId = Integer.parseInt(id);
/*     */       
/* 246 */       DecoratedStory s = this.storyDao.getDecoratedStoryById(storyId);
/*     */       
/* 248 */       if (s.getOwnerId() != this.sessionService.getUserId(req)) {
/* 249 */         return "redirect:/";
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 254 */       if (s.isActive()) {
/* 255 */         redirectAttributes.addFlashAttribute("message", "This story is already published.");
/* 256 */         return "redirect:/homepage";
/*     */       } 
/* 258 */       model.addAttribute("story", s);
/*     */       
/* 260 */       decorateModel(req, model);
/* 261 */       getDecoratedCategories(model);
/* 262 */       getDecoratedTopFive(model);
/*     */     }
/* 264 */     catch (NumberFormatException nfe) {
/* 265 */       redirectAttributes.addFlashAttribute("message", "Invalid thread");
/*     */       
/* 267 */       return "redirect:/index";
/*     */     } 
/*     */     
/* 270 */     return "edit";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/story/{id}/edit"})
/*     */   public String postStoryEdit(HttpServletRequest req, Model model, HttpServletResponse res, @PathVariable("id") String id, RedirectAttributes redirectAttributes) {
/* 278 */     if (!isAuthenticated(req)) {
/* 279 */       model.addAttribute("message", "Must be logged in to edit drafts!");
/* 280 */       return "redirect:/login";
/*     */     } 
/*     */     
/*     */     try {
/* 284 */       int categoryId, storyId = Integer.parseInt(id);
/*     */       
/* 286 */       DecoratedStory s = this.storyDao.getDecoratedStoryById(storyId);
/*     */       
/* 288 */       if (s.getOwnerId() != this.sessionService.getUserId(req)) {
/* 289 */         return "redirect:/";
/*     */       }
/*     */ 
/*     */       
/* 293 */       if (s.isActive()) {
/* 294 */         redirectAttributes.addFlashAttribute("message", "This story is already published.");
/* 295 */         return "redirect:/homepage";
/*     */       } 
/*     */       
/* 298 */       String title = (req.getParameter("title") != null) ? req.getParameter("title") : "";
/* 299 */       String content = (req.getParameter("content") != null) ? req.getParameter("content") : "";
/* 300 */       boolean isDraft = (req.getParameter("draft") != null) ? Boolean.parseBoolean(req.getParameter("draft")) : false;
/*     */       
/*     */       try {
/* 303 */         categoryId = Integer.parseInt(req.getParameter("category"));
/* 304 */       } catch (Exception e) {
/* 305 */         categoryId = 0;
/*     */       } 
/*     */       
/* 308 */       if (title.equalsIgnoreCase("") || content.equalsIgnoreCase("") || categoryId == 0) {
/* 309 */         model.addAttribute("message", "Please fill out all required fields.");
/* 310 */         decorateModel(req, model);
/* 311 */         getDecoratedCategories(model);
/* 312 */         return "edit";
/*     */       } 
/* 314 */       content = SqlUtil.escapeString(content);
/* 315 */       content = StringUtil.cleanText(content);
/* 316 */       logger.info("Updating story " + storyId);
/*     */       
/* 318 */       this.storyDao.updateStory(storyId, title, content, categoryId, !isDraft);
/*     */       
/* 320 */       s = this.storyDao.getDecoratedStoryById(storyId);
/* 321 */       model.addAttribute("story", s);
/*     */       
/* 323 */       decorateModel(req, model);
/*     */       
/* 325 */       getDecoratedTopFive(model);
/*     */       
/* 327 */       redirectAttributes.addFlashAttribute("message", "This story is already published.");
/* 328 */       return "redirect:/homepage";
/*     */     
/*     */     }
/* 331 */     catch (NumberFormatException nfe) {
/* 332 */       redirectAttributes.addFlashAttribute("message", "Invalid thread");
/* 333 */       return "redirect:/index";
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/stories/StoryController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */