package com.cnv.cms.interceptor;

import java.net.URL;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventProducer;
import com.cnv.cms.event.EventType;
import com.cnv.cms.model.HostHolder;
import com.cnv.cms.model.LoginSession;
import com.cnv.cms.service.impl.SessionServiceImpl;

@Component
public class HostHolderInterceptor implements HandlerInterceptor {
	
	private final Logger logger = LoggerFactory.getLogger(HostHolderInterceptor.class);
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private SessionServiceImpl sessionService;
	
	@Autowired
	private EventProducer eventProducer;
	
	private ThreadLocal<Long> tin = new ThreadLocal<>();

	//SessionRepositoryFilter t;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		tin.set(System.currentTimeMillis());
		
		// TODO Auto-generated method stub
		String url = request.getRequestURI();
		if(logger.isDebugEnabled()){
			
		}
		logger.info("HostHolderInterceptor interceptror :"+url);
		LoginSession loginSession = sessionService.getLoginSession(request);
		if(loginSession != null){
			hostHolder.setLoginSession(loginSession);
		}
		if(url.equals("") || url.equals("/"))
			url = "/index.html";
		if(!url.startsWith("/api"))
			hostHolder.setUrl(url);
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String url = hostHolder.getUrl();
		if(logger.isDebugEnabled()){
			logger.info("HostHolderInterceptor interceptror post:"+url);
		}

		if(url!=null){
			//统计耗费时间
			Long tcost = System.currentTimeMillis() - tin.get();
			eventProducer.addEvent(new EventModel(EventType.TIME_COUNT,-1)
					.addExtData("url", url)
					.addExtData("cost", tcost)
					.addExtData("method", "hostholder"));
		}

		hostHolder.clear();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
	}

}
