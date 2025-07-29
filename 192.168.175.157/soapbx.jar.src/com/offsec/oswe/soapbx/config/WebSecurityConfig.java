/*    */ package com.offsec.oswe.soapbx.config;
/*    */ 
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/*    */ import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
/*    */ import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/*    */ import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
/*    */ 
/*    */ @Configuration
/*    */ @EnableWebSecurity
/*    */ public class WebSecurityConfig
/*    */   extends WebSecurityConfigurerAdapter {
/*    */   protected void configure(HttpSecurity http) throws Exception {
/* 14 */     ((HttpSecurity)http.csrf().disable()).headers().frameOptions().sameOrigin();
/*    */     
/* 16 */     ((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)http.authorizeRequests().anyRequest()).anonymous().and()).httpBasic().disable();
/* 17 */     http.logout().disable();
/*    */   }
/*    */ }


/* Location:              /home/kali/Desktop/OSWEExam/exam-connection/soapbx.jar!/com/offsec/oswe/soapbx/config/WebSecurityConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */