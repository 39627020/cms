package com.cnv.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cnv.cms.interceptor.SessionAdminInterceptor;
import com.cnv.cms.interceptor.SessionAuthInterceptor;
import com.cnv.cms.interceptor.SessionUserInterceptor;
import com.cnv.cms.interceptor.TestInterceptor;
/*
 * springmvc配置
 * 对应于springmvc4-servlet.xml
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private SessionAdminInterceptor adminInterceptor;
	@Autowired
	private SessionAuthInterceptor authInterceptor;
	@Autowired
	private SessionUserInterceptor userInterceptor;
	
	//Spring Boot 默认配置的/**映射到/static（或/public ，/resources，/META-INF/resources）
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/*.html")
 				.addResourceLocations("classpath:/static/html/");

        super.addResourceHandlers(registry);
    }	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/**");
		registry.addInterceptor(authInterceptor).addPathPatterns("/api/**");
		registry.addInterceptor(userInterceptor).addPathPatterns("/user/**");
		registry.addInterceptor(new TestInterceptor()).addPathPatterns("/test/*");
		super.addInterceptors(registry);
	}
/*	
	@Override  
	public void addViewControllers(ViewControllerRegistry registry){  
        registry.addViewController("/test/*").setViewName("test/");  
    }  
	*/
}
