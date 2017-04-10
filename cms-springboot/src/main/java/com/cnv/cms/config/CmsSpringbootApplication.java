package com.cnv.cms.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
//@ServletComponentScan//扫描Servlet组件
@EnableConfigurationProperties({CmsConfig.class}) //可以在配置文件中配置的类 
@ComponentScan(basePackages={"com.cnv.cms"})//component扫描包
@MapperScan(basePackages="com.cnv.cms.mapper")//mybatis Mapper 扫描包
@EnableTransactionManagement//启用事务管理
public class CmsSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmsSpringbootApplication.class, args);
	}
}
