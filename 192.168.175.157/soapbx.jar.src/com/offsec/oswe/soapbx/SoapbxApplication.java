/*    */ package com.offsec.oswe.soapbx;
/*    */ 
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.boot.CommandLineRunner;
/*    */ import org.springframework.boot.SpringApplication;
/*    */ import org.springframework.boot.autoconfigure.SpringBootApplication;
/*    */ import org.springframework.jdbc.core.JdbcTemplate;
/*    */ 
/*    */ @SpringBootApplication
/*    */ public class SoapbxApplication
/*    */   implements CommandLineRunner
/*    */ {
/*    */   @Autowired
/*    */   JdbcTemplate jdbcTemplate;
/*    */   
/*    */   public static void main(String[] args) {
/* 17 */     SpringApplication.run(SoapbxApplication.class, args);
/*    */   }
/*    */   
/*    */   public void run(String... args) throws Exception {}
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/SoapbxApplication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */