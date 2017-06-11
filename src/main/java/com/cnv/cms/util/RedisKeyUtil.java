package com.cnv.cms.util;

public class RedisKeyUtil {
	private static String SPLIT = ":";
	private static String KEY_SESSION = "SESSION";
	private static String KEY_EVENT_QUEUE = "EVENT_QUEUE";
	private static String KEY_PV = "PV_COUNT";
	private static String KEY_TIME = "TIMECOST";
	private static String KEY_USER_FOLLOW = "USER_FOLLOW";
	private static String KEY_USER_FANS = "USER_FANS";
	private static String KEY_FEED_QUEUE = "USER_FEED_QUEUE";
	private static String KEY_FEED_POLL_QUEUE = "USER_FEED_POLL_QUEUE";
	private static String KEY_TIMELINE_UPDATE_TIME = "TIMELINE_UPDATE_TIME";
	private static String KEY_CACHE = "DB_CACHE";
	public static String getSessionKey(String sessionid){
		return KEY_SESSION + SPLIT + sessionid;
	}
	public static String getEventQueueKey(){
		return KEY_EVENT_QUEUE;
	}
	public static String getPVKey(String page, int id){
		if(id<0)
			return KEY_PV + SPLIT +page;
		else
			return KEY_PV + SPLIT +page + SPLIT + id;
	}
	public static String getTimeCostKey(String url, String method){
		return KEY_TIME + SPLIT + url + SPLIT + method;
	}
	public static String getUserFollowKey(int id){
		return KEY_USER_FOLLOW + SPLIT + id;
	}
	public static String getUserFansKey(int id){
		return KEY_USER_FANS + SPLIT + id;
	}
	public static String getUserFeedsQueueKey(int id){
		return KEY_FEED_QUEUE + SPLIT + id;
	}
	public static String getUserFeedsPollKey(int id){
		return KEY_FEED_POLL_QUEUE + SPLIT + id;
	}
	public static String getFeedUpdateTimeKey(int id){
		return KEY_TIMELINE_UPDATE_TIME + SPLIT + id;
	}
	public static String getCacheKey(String cacheValue, String key){
		return KEY_CACHE + SPLIT + cacheValue + SPLIT + key;
	}
}
