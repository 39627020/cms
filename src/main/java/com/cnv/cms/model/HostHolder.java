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

    
}
