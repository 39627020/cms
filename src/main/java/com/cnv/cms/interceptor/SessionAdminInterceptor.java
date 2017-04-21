package com.cnv.cms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cnv.cms.config.CmsConfig;
import com.cnv.cms.model.LoginSession;
import com.cnv.cms.util.LoginSessionUtil;

/*
 *Adminl目录静态资源拦截，只有管理员用户可以访问
 */
@Component
public class SessionAdminInterceptor extends HandlerInterceptorAdapter {
	private final Logger logger = LoggerFactory.getLogger(SessionAdminInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		
		String url = request.getRequestURI();
		
		logger.info("Admin Interceptor:",url);
		
		//session中是否保存了登录信息
		LoginSession loginSession = LoginSessionUtil.getLoginSession(request);
				
		if(loginSession==null) {
			logger.info("未登录，跳转到：",request.getContextPath(),"/login.html");

			//删除cookie
			LoginSessionUtil.removeLoginCookies(request, response);
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
