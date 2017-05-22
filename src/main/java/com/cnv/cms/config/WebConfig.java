package com.cnv.cms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cnv.cms.controller.AdminResourcesController;
import com.cnv.cms.interceptor.HostHolderInterceptor;
import com.cnv.cms.interceptor.SessionAdminInterceptor;
import com.cnv.cms.interceptor.SessionAuthInterceptor;
import com.cnv.cms.interceptor.SessionUserInterceptor;
/*
 * springmvc配置
 * 对应于springmvc4-servlet.xml
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	private final Logger logger = LoggerFactory.getLogger(WebConfig.class);
	@Autowired
	private SessionAdminInterceptor adminInterceptor;
	@Autowired
	private SessionAuthInterceptor authInterceptor;
	@Autowired
	private SessionUserInterceptor userInterceptor;
	
	@Autowired
	private HostHolderInterceptor hostHolderInterceptor;
	
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
		registry.addInterceptor(hostHolderInterceptor);
		super.addInterceptors(registry);
	}
/*	
	@Override  
	public void addViewControllers(ViewControllerRegistry registry){  
        registry.addViewController("/test/*").setViewName("test/");  
    }  
	*/
	@Autowired
	public void setRedisTemplate(RedisTemplate redisTemplate) {
		
		//redisTemplate.setKeySerializer(new StringRedisSerializer());
	    RedisSerializer stringSerializer = new StringRedisSerializer();
	    redisTemplate.setKeySerializer(stringSerializer);
	    redisTemplate.setValueSerializer(stringSerializer);
	    redisTemplate.setHashKeySerializer(stringSerializer);
	    redisTemplate.setHashValueSerializer(stringSerializer);
	    logger.info("redisTemplate 初始化完成");
	}
}
