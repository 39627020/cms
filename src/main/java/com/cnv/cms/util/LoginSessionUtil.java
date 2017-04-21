package com.cnv.cms.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnv.cms.mapper.LoginSessionMapper;
import com.cnv.cms.model.LoginSession;
import com.cnv.cms.service.UserService;

@Component
public class LoginSessionUtil {
	@Autowired
	private static LoginSessionMapper loginMapper;
	@Autowired
	private static UserService userService;
	
	public LoginSessionUtil(){
		System.out.println("SessionUtil 初始化");
	}
	
	public static String getSessionId(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		String sessionid = null;
		for(Cookie ck : cookies){
			if (ck.getName().equals("LOGINSESSIONID")){
				sessionid = ck.getValue();
			}
		}
		if(sessionid == null){
			sessionid = request.getSession().getId();
		}
		return sessionid;
		
	}public static LoginSession getLoginSession(HttpServletRequest request){
		String sessionid = LoginSessionUtil.getSessionId(request);
		LoginSession loginSession = loginMapper.selsetBySession(sessionid);
		return loginSession;
	}
	public static void init(){
		if(loginMapper == null){
			loginMapper = (LoginSessionMapper) SpringContextUtil.getBean(LoginSessionMapper.class);
			userService = (UserService) SpringContextUtil.getBean(UserService.class);
		}
		
	}
	public static LoginSessionMapper getLoginMapper() {
		return loginMapper;
	}
	public static void setLoginMapper(LoginSessionMapper loginMapper) {
		LoginSessionUtil.loginMapper = loginMapper;
	}
	public static UserService getUserService() {
		return userService;
	}
	public static void setUserService(UserService userService) {
		LoginSessionUtil.userService = userService;
	}
	
	public static void removeLoginCookies(HttpServletRequest request,HttpServletResponse response){
		Cookie[] cookies = request.getCookies();
		for(Cookie ck : cookies){
			if(ck.getName().equals("LOGINSESSIONID") || ck.getName().equals("loginUser") || ck.getName().equals("isAdmin")){
				ck.setValue(null);
				ck.setPath("/");
				ck.setMaxAge(0);
				response.addCookie(ck);
			}
		}
	}
}
