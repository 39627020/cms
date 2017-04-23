package com.cnv.cms.util;

public class RedisKeyUtil {
	private static String SPLIT = ":";
	private static String KEY_SESSION = "SESSION";
	public static String getSessionKey(String sessionid){
		return KEY_SESSION + SPLIT + sessionid;
	}
}
