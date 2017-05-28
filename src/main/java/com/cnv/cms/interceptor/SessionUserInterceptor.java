package com.cnv.cms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cnv.cms.config.CmsConfig;
import com.cnv.cms.model.LoginSession;
import com.cnv.cms.service.impl.SessionServiceImpl;

/*
 *user目录静态资源拦截
 */
@Component
public class SessionUserInterceptor extends HandlerInterceptorAdapter {
	
	private final Logger logger = LoggerFactory.getLogger(SessionUserInterceptor.class);
	@Autowired
	private SessionServiceImpl sessionService;
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String url = request.getRequestURI();
		if(logger.isDebugEnabled()){
			logger.info("User Home Interceptor: "+url);
		}
		//session中是否保存了登录信息
		LoginSession loginSession = sessionService.getLoginSession(request);
		
		if(loginSession==null) {
			//删除cookie
			sessionService.removeLoginCookies(request, response);
			//如果未登录就跳转到登录页面
			response.sendRedirect(request.getContextPath()+"/login.html");
			//response.sendError(403, "无权访问");
			return false;
		} 
			
		return super.preHandle(request, response, handler);
		
				
	}
}
