package com.cnv.cms.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/*
 * springmvc配置
 * 对应于springmvc4-servlet.xml
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	
	//Spring Boot 默认配置的/**映射到/static（或/public ，/resources，/META-INF/resources）
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("*.html")
 				.addResourceLocations("classpath:/static/html/");
 /*       registry.addResourceHandler(CmsConfig.filePath)
 				.addResourceLocations("file:F:/Ftp-Server/files/");*/
    }	
       
}
