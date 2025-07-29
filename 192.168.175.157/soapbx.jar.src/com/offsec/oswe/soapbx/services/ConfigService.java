/*    */ package com.offsec.oswe.soapbx.services;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.UUID;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class ConfigService
/*    */ {
/* 20 */   private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);
/*    */   
/*    */   private UUID instanceId;
/*    */   
/*    */   private UUID apiKey;
/*    */   
/*    */   private void initializeInstance() {
/*    */     try {
/* 28 */       Path currPath = Paths.get(".", new String[0]);
/* 29 */       String localFile = currPath.toAbsolutePath().normalize().toString() + "/conf/instanceid";
/* 30 */       BufferedReader br = new BufferedReader(new FileReader(localFile));
/*    */       
/* 32 */       String txt = br.readLine();
/* 33 */       br.close();
/* 34 */       this.instanceId = UUID.fromString(txt);
/* 35 */     } catch (IOException ioe) {
/* 36 */       logger.error("IOException retreiving instanceid.txt file. Regenerating instanceId.");
/* 37 */       logger.error(ioe.getMessage());
/* 38 */       this.instanceId = UUID.randomUUID();
/*    */       try {
/* 40 */         writeInstanceId();
/* 41 */       } catch (Exception e) {
/* 42 */         logger.error("Failed to persist instance id. Proceeding with ephemeral id: " + this.instanceId.toString());
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void initializeAPI() {
/*    */     try {
/* 49 */       Path currPath = Paths.get(".", new String[0]);
/* 50 */       String localFile = currPath.toAbsolutePath().normalize().toString() + "/conf/apikey";
/* 51 */       BufferedReader br = new BufferedReader(new FileReader(localFile));
/*    */       
/* 53 */       String txt = br.readLine();
/* 54 */       br.close();
/* 55 */       this.apiKey = UUID.fromString(txt);
/* 56 */     } catch (IOException ioe) {
/* 57 */       logger.error("IOException retreiving apikey.txt file. Regenerating apiKey.");
/* 58 */       logger.error(ioe.getMessage());
/* 59 */       this.apiKey = UUID.randomUUID();
/*    */       try {
/* 61 */         writeApiKey();
/* 62 */       } catch (Exception e) {
/* 63 */         logger.error("Failed to persist apiKey. Proceeding with ephemeral apiKey: " + this.apiKey.toString());
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void writeInstanceId() throws Exception {
/* 69 */     Path localFile = Paths.get(Paths.get(".", new String[0]).toAbsolutePath().normalize().toString(), new String[] { "/conf/instanceid" });
/* 70 */     Files.write(localFile, this.instanceId.toString().getBytes(), new java.nio.file.OpenOption[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeApiKey() throws Exception {
/* 75 */     Path localFile = Paths.get(Paths.get(".", new String[0]).toAbsolutePath().normalize().toString(), new String[] { "/conf/apikey" });
/* 76 */     Files.write(localFile, this.apiKey.toString().getBytes(), new java.nio.file.OpenOption[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getInstanceId() {
/* 81 */     if (this.instanceId == null) {
/* 82 */       initializeInstance();
/*    */     }
/* 84 */     String id = this.instanceId.toString();
/* 85 */     return id;
/*    */   }
/*    */   
/*    */   public String getApiKey() {
/* 89 */     if (this.apiKey == null) {
/* 90 */       initializeAPI();
/*    */     }
/* 92 */     String key = this.apiKey.toString();
/* 93 */     return key;
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/services/ConfigService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */