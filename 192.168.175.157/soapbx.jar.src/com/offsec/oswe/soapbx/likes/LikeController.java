/*    */ package com.offsec.oswe.soapbx.likes;
/*    */ 
/*    */ import com.offsec.oswe.soapbx.SoapbxBaseController;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Controller;
/*    */ import org.springframework.ui.Model;
/*    */ import org.springframework.web.bind.annotation.PathVariable;
/*    */ import org.springframework.web.bind.annotation.PostMapping;
/*    */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Controller
/*    */ public class LikeController
/*    */   extends SoapbxBaseController
/*    */ {
/*    */   @Autowired
/*    */   LikeDao likeDao;
/*    */   
/*    */   @PostMapping({"/like/{sid}"})
/*    */   public String likeStory(HttpServletRequest req, Model model, HttpServletResponse res, @PathVariable("sid") String sid, RedirectAttributes redirectAttributes) {
/* 24 */     if (!isAuthenticated(req)) {
/* 25 */       redirectAttributes.addFlashAttribute("message", "Must be logged in to like stories!");
/* 26 */       return "redirect:/login";
/*    */     } 
/* 28 */     int storyId = Integer.parseInt(sid);
/* 29 */     int uId = ((Integer)req.getSession(false).getAttribute("userid")).intValue();
/*    */     
/* 31 */     this.likeDao.addLikeToStory(storyId, uId);
/* 32 */     return "redirect:/story/" + storyId;
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/likes/LikeController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */