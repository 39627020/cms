package com.cnv.cms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;



//@EnableAutoConfiguration
//@ServletComponentScan("com.cnv.cms.listener")//扫描Servlet组件
//@EnableConfigurationProperties({CmsConfig.class}) //可以在配置文件中配置的类 
//@ComponentScan(basePackages={"com.cnv.cms"})//component扫描包,注：同级包和子包可以自动扫描
@SpringBootApplication
@MapperScan(basePackages="com.cnv.cms.mapper")//mybatis Mapper 扫描包
@EnableTransactionManagement//启用事务管理
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class CmsSpringbootApplication extends SpringBootServletInitializer{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CmsSpringbootApplication.class);
    }
	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(CmsSpringbootApplication.class, args);
		
	}
	
}
