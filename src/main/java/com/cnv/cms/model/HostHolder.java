package com.cnv.cms.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    private static ThreadLocal<LoginSession> session = new ThreadLocal<LoginSession>();
    private  ThreadLocal<String> url = new ThreadLocal<>();


    public LoginSession getLoginSession() {
        return session.get();
    }

    public void setLoginSession(LoginSession session) {
        this.session.set(session);
    }
    
    public String getUrl(){
    	return url.get();
    }
    public void setUrl(String url){
    	this.url.set(url);
    }

    public void clear() {
    	session.remove();
    	url.remove();
    }

    public String getUserName(){
    	LoginSession ls = this.getLoginSession();
    	if(ls != null){
    		return ls.getUsername();
    	}
    	return null;
    }
    public int getUserId(){
    	LoginSession ls = this.getLoginSession();
    	if(ls != null){
    		return ls.getUserid();
    	}
    	return -1;
    }
}
