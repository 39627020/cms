package com.cnv.cms.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.cnv.cms.mapper.LoginSessionMapper;
import com.cnv.cms.util.SpringContextUtil;

//@WebListener
public class CmsSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		HttpSession ss = se.getSession();
	
		System.out.println("session 创建:" + ss.getId());
		
//		String sessionid = ss.getId();
//		LoginSessionMapper loginMapper = (LoginSessionMapper) SpringContextUtil.getBean(LoginSessionMapper.class);
		

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub

	}

}
