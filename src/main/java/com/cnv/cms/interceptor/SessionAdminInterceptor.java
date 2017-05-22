package com.cnv.cms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cnv.cms.model.LoginSession;
import com.cnv.cms.service.impl.SessionServiceImpl;

/*
 *Adminl目录静态资源拦截，只有管理员用户可以访问
 */
@Component
public class SessionAdminInterceptor extends HandlerInterceptorAdapter {
	private final Logger logger = LoggerFactory.getLogger(SessionAdminInterceptor.class);
	
	@Autowired
	private SessionServiceImpl sessionService;
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String url = request.getRequestURI();
		
		logger.info("Admin Interceptor:",url);
		
		//session中是否保存了登录信息
		LoginSession loginSession = sessionService.getLoginSession(request);
				
		if(loginSession==null) {
			logger.info("未登录，跳转到：",request.getContextPath(),"/login.html");

			//删除cookie
			sessionService.removeLoginCookies(request, response);
			//如果未登录就跳转到登录页面
			response.sendRedirect(request.getContextPath()+"/login.html");

			return false;
		} else {
			boolean isAdmin = loginSession.getAdmin();
			if(!isAdmin) {
				logger.info("用户无权限访问:",url,",跳转到：",request.getContextPath(),"/login.html");
				response.sendRedirect(request.getContextPath()+"/user/home.html");
				return false;
			}
		}
	
		return super.preHandle(request, response, handler);
		
				
	}
}
