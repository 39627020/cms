package com.cnv.cms.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cnv.cms.util.LoginSessionUtil;
import com.cnv.cms.util.SpringContextUtil;


@SpringBootApplication
@ServletComponentScan("com.cnv.cms.listener")//扫描Servlet组件
@EnableConfigurationProperties({CmsConfig.class}) //可以在配置文件中配置的类 
@ComponentScan(basePackages={"com.cnv.cms"})//component扫描包
@MapperScan(basePackages="com.cnv.cms.mapper")//mybatis Mapper 扫描包
@EnableTransactionManagement//启用事务管理
public class CmsSpringbootApplication extends SpringBootServletInitializer{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CmsSpringbootApplication.class);
    }
	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(CmsSpringbootApplication.class, args);
		SpringContextUtil.setApplicationContext(app);
		LoginSessionUtil.init();
	}
}
