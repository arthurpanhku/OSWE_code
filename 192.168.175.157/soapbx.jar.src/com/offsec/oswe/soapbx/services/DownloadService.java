/*     */ package com.offsec.oswe.soapbx.services;
/*     */ 
/*     */ import com.offsec.oswe.soapbx.stories.DecoratedStory;
/*     */ import java.math.BigInteger;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.security.MessageDigest;
/*     */ import org.apache.pdfbox.pdmodel.PDDocument;
/*     */ import org.apache.pdfbox.pdmodel.PDDocumentInformation;
/*     */ import org.apache.pdfbox.pdmodel.PDPage;
/*     */ import org.apache.pdfbox.pdmodel.PDPageContentStream;
/*     */ import org.apache.pdfbox.pdmodel.font.PDFont;
/*     */ import org.apache.pdfbox.pdmodel.font.PDType1Font;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class DownloadService
/*     */ {
/*  22 */   private String downloadDir = Paths.get(".", new String[0]).toAbsolutePath().normalize().toString() + "/downloads/";
/*     */ 
/*     */   
/*     */   public String createPDFFromStory(DecoratedStory story) throws Exception {
/*  26 */     String filename = "";
/*  27 */     PDDocument document = new PDDocument();
/*  28 */     StringBuilder sb = new StringBuilder();
/*     */ 
/*     */     
/*     */     try {
/*  32 */       PDDocumentInformation pdi = document.getDocumentInformation();
/*  33 */       pdi.setAuthor(story.getOwnerName());
/*  34 */       pdi.setTitle(story.getTitle());
/*     */       
/*  36 */       pdi.setCreator("Created by Soapbx v0.4.2");
/*     */       
/*  38 */       PDPage page = new PDPage();
/*  39 */       document.addPage(page);
/*     */       
/*  41 */       PDPageContentStream contentStream = new PDPageContentStream(document, page);
/*  42 */       contentStream.setFont((PDFont)PDType1Font.HELVETICA, 12.0F);
/*  43 */       contentStream.beginText();
/*  44 */       contentStream.setLeading(12.0F);
/*  45 */       contentStream.newLineAtOffset(25.0F, 700.0F);
/*  46 */       contentStream.showText(story.getTitle());
/*  47 */       contentStream.newLine();
/*  48 */       contentStream.showText("Author: " + story.getOwnerName());
/*  49 */       contentStream.newLine();
/*  50 */       contentStream.newLine();
/*     */ 
/*     */       
/*  53 */       for (String word : story.getContent().split("\\s+")) {
/*  54 */         if (sb.length() < 90) {
/*  55 */           if (word.contains("\\n")) {
/*  56 */             String[] tmp = word.split("\\\\n");
/*  57 */             sb.append(tmp[0]);
/*  58 */             contentStream.showText(sb.toString());
/*  59 */             contentStream.newLine();
/*  60 */             sb.setLength(0);
/*  61 */             sb.append(tmp[1]);
/*  62 */             sb.append(" ");
/*  63 */           } else if (word.contains("\\r")) {
/*  64 */             String[] tmp = word.split("\\\\r");
/*  65 */             sb.append(tmp[0]);
/*  66 */             contentStream.showText(sb.toString());
/*  67 */             contentStream.newLine();
/*  68 */             sb.setLength(0);
/*  69 */             sb.append(tmp[1]);
/*  70 */             sb.append(" ");
/*     */           } else {
/*  72 */             sb.append(word);
/*  73 */             sb.append(" ");
/*     */           } 
/*     */         } else {
/*     */           
/*  77 */           contentStream.showText(sb.toString());
/*  78 */           contentStream.newLine();
/*     */           
/*  80 */           sb.setLength(0);
/*  81 */           sb.append(word);
/*  82 */           sb.append(" ");
/*     */         } 
/*     */       } 
/*     */       
/*  86 */       if (sb.length() > 0) {
/*  87 */         contentStream.showText(sb.toString());
/*  88 */         contentStream.newLine();
/*     */       } 
/*     */       
/*  91 */       contentStream.endText();
/*  92 */       contentStream.close();
/*     */       
/*  94 */       filename = getFilenameForStory(story);
/*  95 */       Path downloadPath = Paths.get(this.downloadDir + filename, new String[0]).normalize();
/*  96 */       document.save(downloadPath.toString());
/*  97 */       document.close();
/*     */       
/*  99 */       return filename;
/* 100 */     } catch (Exception e) {
/* 101 */       document.close();
/* 102 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public byte[] getPDF(String filename) throws Exception {
/* 107 */     Path filePath = Paths.get(this.downloadDir, new String[] { filename }).normalize();
/* 108 */     if (filePath.getFileName().toString().lastIndexOf(".") != -1) {
/* 109 */       throw new Exception("Invalid file requested");
/*     */     }
/* 111 */     byte[] file = Files.readAllBytes(filePath);
/* 112 */     return file;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFilenameForStory(DecoratedStory story) throws Exception {
/* 117 */     MessageDigest md = MessageDigest.getInstance("SHA-1");
/* 118 */     byte[] bytes = md.digest(story.getTitle().getBytes("UTF-8"));
/* 119 */     return (new BigInteger(1, bytes)).toString(16);
/*     */   }
/*     */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/services/DownloadService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */