/*    */ package com.offsec.oswe.soapbx;
/*    */ 
/*    */ import com.offsec.oswe.soapbx.categories.CategoryDao;
/*    */ import com.offsec.oswe.soapbx.categories.DecoratedCategory;
/*    */ import com.offsec.oswe.soapbx.services.ConfigService;
/*    */ import com.offsec.oswe.soapbx.services.SessionService;
/*    */ import com.offsec.oswe.soapbx.stories.DecoratedStory;
/*    */ import com.offsec.oswe.soapbx.stories.StoryDao;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.ui.Model;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SoapbxBaseController
/*    */ {
/*    */   @Autowired
/*    */   protected CategoryDao catDao;
/*    */   @Autowired
/*    */   protected StoryDao storyDao;
/*    */   @Autowired
/*    */   ConfigService cfgService;
/*    */   @Autowired
/*    */   protected SessionService sessionService;
/*    */   
/*    */   protected void getDecoratedTopFive(Model model) {
/* 33 */     List<DecoratedStory> topFive = this.storyDao.getTopFiveStoriesDecorated();
/* 34 */     model.addAttribute("topfive", topFive);
/*    */   }
/*    */   
/*    */   protected void getDecoratedCategories(Model model) {
/* 38 */     List<DecoratedCategory> categories = this.catDao.getAllDecoratedCategories();
/* 39 */     model.addAttribute("categories", categories);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isAuthenticated(HttpServletRequest req) {
/* 46 */     if (req.getSession(false) != null && 
/* 47 */       req.getSession(false).getAttribute("authenticated") != null) {
/* 48 */       return ((Boolean)req.getSession(false).getAttribute("authenticated")).booleanValue();
/*    */     }
/*    */ 
/*    */     
/* 52 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAdmin(HttpServletRequest req) {
/* 57 */     if (req.getSession(false) != null && 
/* 58 */       req.getSession(false).getAttribute("authenticated") != null && (
/* 59 */       (Boolean)req.getSession(false).getAttribute("authenticated")).booleanValue() && 
/* 60 */       req.getSession(false).getAttribute("isAdmin") != null) {
/* 61 */       return ((Boolean)req.getSession(false).getAttribute("isAdmin")).booleanValue();
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 66 */     return false;
/*    */   }
/*    */   
/*    */   protected void decorateModel(HttpServletRequest req, Model model) {
/* 70 */     if (isAuthenticated(req)) {
/* 71 */       model.addAttribute("authenticated", Boolean.valueOf(true));
/* 72 */       model.addAttribute("username", req.getSession(false).getAttribute("username"));
/* 73 */       model.addAttribute("isAdmin", req.getSession(false).getAttribute("isAdmin"));
/* 74 */       model.addAttribute("isMod", req.getSession(false).getAttribute("isMod"));
/* 75 */       model.addAttribute("userid", req.getSession(false).getAttribute("userid"));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/SoapbxBaseController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */