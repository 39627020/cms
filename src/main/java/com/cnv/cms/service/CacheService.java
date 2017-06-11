package com.cnv.cms.service;

import java.util.List;

public interface CacheService {
	void addCache(String cacheValue, String key,  long timeout, Object val);
	<T> T getCache(String cacheValue, String key,  Class<T> clazz);
	<T> List<T> getCacheList(String cacheValue, String key, Class<T> clazz);
	void removeCache(String cacheValue, String key);
}
