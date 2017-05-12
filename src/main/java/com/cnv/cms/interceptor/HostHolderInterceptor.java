package com.cnv.cms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cnv.cms.model.HostHolder;
import com.cnv.cms.model.LoginSession;
import com.cnv.cms.service.impl.SessionService;

@Component
public class HostHolderInterceptor implements HandlerInterceptor {
	
	private final Logger logger = LoggerFactory.getLogger(HostHolderInterceptor.class);
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private SessionService sessionService;
	//SessionRepositoryFilter t;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		String url = request.getRequestURI();
		logger.info("HostHolderInterceptor interceptror :"+url);
		
		LoginSession loginSession = sessionService.getLoginSession(request);
		if(loginSession != null){
			hostHolder.setLoginSession(loginSession);
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		String url = request.getRequestURI();
		logger.info("HostHolderInterceptor interceptror post:"+url);
		hostHolder.clear();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
	}

}
