/*    */ package com.offsec.oswe.soapbx.services;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import java.nio.file.CopyOption;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.nio.file.StandardCopyOption;
/*    */ import java.security.MessageDigest;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ import org.springframework.web.multipart.MultipartFile;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class FileService
/*    */ {
/*    */   @Autowired
/*    */   private ConfigService cfgService;
/* 22 */   private String uploadDir = Paths.get(".", new String[0]).toAbsolutePath().normalize().toString() + "/uploads/";
/*    */ 
/*    */   
/*    */   public void uploadFile(MultipartFile file, String username) throws Exception {
/*    */     try {
/* 27 */       String fileName = getFileNameForUser(username);
/* 28 */       Path copyPath = Paths.get(this.uploadDir + fileName, new String[0]).normalize();
/* 29 */       Files.copy(file.getInputStream(), copyPath, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 30 */     } catch (Exception e) {
/* 31 */       throw new Exception("Could not store file.");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getFile(String filename) throws Exception {
/*    */     try {
/* 38 */       Path filePath = Paths.get(this.uploadDir, new String[] { filename }).normalize();
/*    */       
/* 40 */       byte[] file = Files.readAllBytes(filePath);
/* 41 */       return file;
/* 42 */     } catch (Exception e) {
/* 43 */       throw new Exception("No image for user");
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getFileNameForUser(String username) throws Exception {
/* 50 */     MessageDigest md = MessageDigest.getInstance("SHA-1");
/* 51 */     byte[] bytes = md.digest(username.getBytes("UTF-8"));
/* 52 */     return (new BigInteger(1, bytes)).toString(16);
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/services/FileService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */