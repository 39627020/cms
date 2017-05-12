package com.cnv.cms.authority;

import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

@Component
public class InitAuthority implements  ServletContextInitializer  {
	
	@Autowired
	private AuthUtil authUtil;
	
	private Logger logger  = LoggerFactory.getLogger(AuthUtil.class);
	
	@Override
	public void onStartup(ServletContext arg0) throws ServletException {
		//初始化权限信息
		Map<String,Set<String>> auths = authUtil.initAuth("com.cnv.cms.controller");
		arg0.setAttribute("allAuths", auths);
		logger.info("Authority Init : " + auths);
		logger.info("Authority Init 权限注解初始化完成: com.cnv.cms.controller");
	}

}
