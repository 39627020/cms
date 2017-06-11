package com.cnv.cms.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cnv.cms.service.CacheService;
import com.cnv.cms.util.RedisKeyUtil;

@Service
public class CacheServiceImpl implements CacheService, InitializingBean{
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	private ValueOperations<String, String> valOps=null;
	@Override
	public void addCache(String cacheValue, String key,  long timeout, Object val) {
		// TODO Auto-generated method stub
		String redisKey = RedisKeyUtil.getCacheKey(cacheValue, key);
		valOps.set(redisKey, JSON.toJSONString(val), timeout, TimeUnit.SECONDS);
	}

	@Override
	public <T> T getCache(String cacheValue, String key,  Class<T> clazz) {
		// TODO Auto-generated method stub
		String redisKey = RedisKeyUtil.getCacheKey(cacheValue, key);
		String jsonVal =valOps.get(redisKey);
		T val = JSON.parseObject(jsonVal, clazz);
		return val;
	}

	@Override
	public <T> List<T> getCacheList(String cacheValue, String key, Class<T> clazz) {
		// TODO Auto-generated method stub
		String redisKey = RedisKeyUtil.getCacheKey(cacheValue, key);
		String jsonVal =valOps.get(redisKey);
		List<T> vals = JSON.parseArray(jsonVal, clazz);
		return vals;
	}
	@Override
	public void removeCache(String cacheValue, String key) {
		// TODO Auto-generated method stub
		String redisKey = RedisKeyUtil.getCacheKey(cacheValue, key);
		redisTemplate.delete(redisKey);
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		valOps = redisTemplate.opsForValue();
	}

}
