package com.cnv.cms.authority;

import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class InitAuthority implements  ServletContextInitializer  {

	@Override
	public void onStartup(ServletContext arg0) throws ServletException {
		// TODO Auto-generated method stub
		//初始化权限信息
		Map<String,Set<String>> auths = AuthUtil.initAuth("com.cnv.cms.controller");
		arg0.setAttribute("allAuths", auths);
		//this.getServletContext().setAttribute("baseInfo", BaseInfoUtil.getInstacne().read());
		System.out.println("------------------------系统初始化成功:-----------------------------\n"+auths);
	}

}
