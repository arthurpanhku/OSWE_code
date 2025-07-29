/*    */ package com.offsec.oswe.soapbx.categories;
/*    */ 
/*    */ import com.offsec.oswe.soapbx.SoapbxBaseController;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.stereotype.Controller;
/*    */ import org.springframework.ui.Model;
/*    */ import org.springframework.web.bind.annotation.GetMapping;
/*    */ import org.springframework.web.bind.annotation.PathVariable;
/*    */ import org.springframework.web.bind.annotation.PostMapping;
/*    */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Controller
/*    */ public class CategoryController
/*    */   extends SoapbxBaseController
/*    */ {
/* 23 */   private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
/*    */ 
/*    */   
/*    */   @GetMapping({"/admin/categories"})
/*    */   public String getCategoriesPage(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirAttrs) {
/* 28 */     if (!isAdmin(req)) {
/* 29 */       redirAttrs.addFlashAttribute("message", "You must be logged in to access this area.");
/* 30 */       return "redirect:/login";
/*    */     } 
/*    */     
/* 33 */     decorateModel(req, model);
/*    */     
/* 35 */     getDecoratedTopFive(model);
/*    */     
/* 37 */     String sort = (req.getParameter("order") != null) ? req.getParameter("order") : "";
/*    */     
/* 39 */     List<DecoratedCategory> categories = this.catDao.getAllDecoratedCategoriesSorted(sort);
/* 40 */     model.addAttribute("categories", categories);
/*    */     
/* 42 */     return "categories";
/*    */   }
/*    */ 
/*    */   
/*    */   @PostMapping({"/admin/category/create"})
/*    */   public String postCategoryCreate(HttpServletRequest req, Model model, HttpServletResponse res, RedirectAttributes redirAttrs) {
/* 48 */     if (!isAdmin(req)) {
/* 49 */       redirAttrs.addFlashAttribute("message", "You must be logged in to access this area.");
/* 50 */       return "redirect:/login";
/*    */     } 
/*    */     
/* 53 */     decorateModel(req, model);
/*    */     
/* 55 */     String name = (req.getParameter("name") != null) ? req.getParameter("name") : "";
/*    */     
/* 57 */     if (name.equalsIgnoreCase("")) {
/* 58 */       model.addAttribute("message", "A name value must be supplied.");
/*    */     } else {
/* 60 */       this.catDao.addCategory(name);
/*    */     } 
/*    */     
/* 63 */     getDecoratedTopFive(model);
/*    */     
/* 65 */     getDecoratedCategories(model);
/*    */     
/* 67 */     return "categories";
/*    */   }
/*    */ 
/*    */   
/*    */   @GetMapping({"/admin/category/{id}/delete"})
/*    */   public String deleteCategory(HttpServletRequest req, Model model, HttpServletResponse res, @PathVariable("id") String id, RedirectAttributes redirAttrs) {
/* 73 */     if (!isAdmin(req)) {
/* 74 */       redirAttrs.addFlashAttribute("message", "Must be authenticated to delete categories");
/* 75 */       return "redirect:/login";
/*    */     } 
/*    */     
/* 78 */     decorateModel(req, model);
/*    */     
/* 80 */     getDecoratedTopFive(model);
/*    */ 
/*    */     
/* 83 */     DecoratedCategory cat = this.catDao.getDecoratedCategoryById(Integer.parseInt(id));
/*    */     
/* 85 */     if (cat.getStoryCount() > 0) {
/* 86 */       model.addAttribute("message", "Cannot delete Category that has Questions");
/*    */     } else {
/*    */       
/* 89 */       logger.info("Deleting category with id " + id);
/* 90 */       this.catDao.deleteCategory(Integer.parseInt(id));
/*    */     } 
/*    */     
/* 93 */     return "redirect:/admin/categories";
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/categories/CategoryController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */