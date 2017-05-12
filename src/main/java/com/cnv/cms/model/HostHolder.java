package com.cnv.cms.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    private static ThreadLocal<LoginSession> session = new ThreadLocal<LoginSession>();

    public LoginSession getLoginSession() {
        return session.get();
    }

    public void setLoginSession(LoginSession session) {
        this.session.set(session);
    }

    public void clear() {
    	session.remove();
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
