package com.cnv.cms.interceptor;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cnv.cms.config.CmsConfig;
import com.cnv.cms.model.HostHolder;
import com.cnv.cms.model.LoginSession;
import com.cnv.cms.model.RoleType;
import com.cnv.cms.service.UserService;
import com.cnv.cms.service.impl.SessionService;

import ch.qos.logback.core.util.LocationUtil;

/**
 * @author Administrator
 *
 * @description 拦截api/**,需要在springMVC配置文件中配置拦截器
 *
 */
@Component
public class SessionAuthInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = LoggerFactory.getLogger(SessionAuthInterceptor.class);
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@Autowired
	 @Qualifier("userServiceImpl")
	private UserService userService;
	/*
	 * 在方法执行前执行拦截器
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		
		String url = request.getRequestURI();
		if(CmsConfig.isDebug){
			logger.info("Authority Api Interceptor: "+url);
		}
		
		//获取包含包名和类名的方法名全称
		HandlerMethod hm = (HandlerMethod)handler;
		String aname = hm.getBean().getClass().getName()+"."+hm.getMethod().getName();
		//获取不受限制的方法集合
		Map<String,Set<String>> auths = (Map<String, Set<String>>) session.getServletContext().getAttribute("allAuths");
		Set<String>  customAuths = auths.get("customer");
		//如果是customer方法，则跳过，允许访问
		if(!customAuths.contains(aname)){
			
			LoginSession loginSession = sessionService.getLoginSession(request);
			
			if(loginSession==null || loginSession.getExpired().before(new Date())) {
				
				response.sendError(403, "无权访问");
				return false;
			} else {
				//设置线程独立的loginSession对象
				hostHolder.setLoginSession(loginSession);
				boolean isAdmin = loginSession.getAdmin();
				if(!isAdmin) {
					//不是超级管理人员，就需要判断是否有权限访问某些功能
					Set<String> actions = this.getAllActions(session, loginSession.getUserid());
					if(!actions.contains(aname)){
						if(CmsConfig.isDebug()){
							System.out.println("没有权限访问该功能");
						}
						response.sendError(403, "无权访问");
						return false;
					}
				}
			}
			
		}
	
		return super.preHandle(request, response, handler);

	} 
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		hostHolder.clear();
		super.postHandle(request, response, handler, modelAndView);
	}
	private Set<String> getAllActions(HttpSession httpSession,int userid){
		//该用户所有可以访问的方法
		Set<String> allActions = new HashSet<String>();
		Map<String,Set<String>> auths = (Map<String, Set<String>>) httpSession.getServletContext().getAttribute("allAuths");
		//contomer方法和base方法都可以访问
		//Set<String> customAuths = auths.get("customer");
		//allActions.addAll(customAuths);
		Set<String> baseAuths = auths.get("base");
		allActions.addAll(baseAuths);
		
		//UserService userService = (UserService) SpringContextUtil.getBean(UserService.class);
		//查询每个角色对应的权限
		for(int rid : userService.listUserRoleIds(userid)){
			//数据库中role id 从1开始
			String rname =RoleType.values()[rid-1].toString();
			Set<String> roleAuths = auths.get(rname);
			if(roleAuths != null){
				allActions.addAll(roleAuths);	
			}
		}
		return allActions;
	}
}
