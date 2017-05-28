package com.cnv.cms.service;

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
import com.cnv.cms.model.User;
import com.cnv.cms.util.JsonUtil;
import com.cnv.cms.util.RedisKeyUtil;

public interface SessionService {

   
	String getSessionId(HttpServletRequest request);
	LoginSession getLoginSession(HttpServletRequest request);
	LoginSession getLoginSession(String sessionid);

	void removeLoginCookies(HttpServletRequest request,HttpServletResponse response);
	void addLoginSession(String sessionid, User user, boolean isAdmin);
	
	void removeLoginSession(String sessionid);
	

}
