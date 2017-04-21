package com.cnv.cms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class LoginSession {
	private int id;
	private String sessionid;
	private int userid;
	private boolean admin;
	private Date expired;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public boolean getAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	//@JsonFormat(pattern="yyyy-MM-dd HH-mm-ss",timezone = "GMT+8")
	public Date getExpired() {
		return expired;
	}
	public void setExpired(Date expired) {
		this.expired = expired;
	}
	
	
	
}
