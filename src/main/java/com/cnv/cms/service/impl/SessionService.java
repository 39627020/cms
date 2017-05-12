package com.cnv.cms.service.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.cnv.cms.model.LoginSession;
import com.cnv.cms.util.JsonUtil;
import com.cnv.cms.util.RedisKeyUtil;

@Component
public class SessionService implements InitializingBean{

	private final Logger logger = LoggerFactory.getLogger(SessionService.class);
	
	//过期时间 s
	private final int EXPIRED_TIME = 3600;

    @Autowired  
    private RedisTemplate<String,String> redisTemplate;  
    
	//@Autowired  
    //private RedisTemplate<String,LoginSession> redisTemplate2;  
    
	private ValueOperations<String,String> valueOps=null;
    
    
    @Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
    	logger.info("SessionService 初始化");
    	//valueOps2 = redisTemplate2.opsForValue();
    	valueOps = redisTemplate.opsForValue();
    	
	}
	
	public  String getSessionId(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		String sessionid = null;
		for(Cookie ck : cookies){
			if (ck.getName().equals("LOGINSESSIONID")){
				sessionid = ck.getValue();
				break;
			}
		}
		if(sessionid == null){
			sessionid = request.getSession().getId();
		}
		return sessionid;
		
	}
	public  LoginSession getLoginSession(HttpServletRequest request){
		String sessionid = this.getSessionId(request);
		return this.getLoginSession(sessionid);
	}
	public  LoginSession getLoginSession(String sessionid){
		String sessionkey = RedisKeyUtil.getSessionKey(sessionid);
		String objStr = valueOps.get(sessionkey);
		LoginSession loginSession = null;
		if(objStr != null){
			loginSession = JsonUtil.readValue(objStr, LoginSession.class);
		}
		return loginSession;
	}



	
	public  void removeLoginCookies(HttpServletRequest request,HttpServletResponse response){
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
	public void addLoginSession(String sessionid, int userid, String username) {
		// TODO Auto-generated method stub
		Date date = new Date();
		date.setTime(date.getTime()+EXPIRED_TIME*1000);
		LoginSession loginSession = new LoginSession();
		loginSession.setSessionid(sessionid);
		loginSession.setUserid(userid);
		loginSession.setUsername(username);
		loginSession.setExpired(date);
		//loginMapper.deleteByUser(userid);
		String sessionkey = RedisKeyUtil.getSessionKey(sessionid);
		String loginSessionStr = JsonUtil.toJSon(loginSession);
		valueOps.set(sessionkey, loginSessionStr,EXPIRED_TIME,TimeUnit.SECONDS);
	}
	
	public void removeLoginSession(String sessionid){
		String sessionkey = RedisKeyUtil.getSessionKey(sessionid);
		redisTemplate.delete(sessionkey);
	}
	

}
