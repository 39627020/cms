package com.cnv.cms.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.cnv.cms.service.PVService;
import com.cnv.cms.util.RedisKeyUtil;

@Service
public class PVServiceImpl implements PVService , InitializingBean{
	
	@Autowired  
	private RedisTemplate redisTemplate; 
	private ValueOperations<String,String> valOps=null;
	
	@Override
	public void addPVCount(String page, int id) {
		// TODO Auto-generated method stub
		String key = RedisKeyUtil.getPVKey(page, id);
		valOps.increment(key, 1);
	}

	@Override
	public long getPVCount(String page, int id) {
		// TODO Auto-generated method stub
		String key = RedisKeyUtil.getPVKey(page, id);
		String pv = valOps.get(key);
		return pv==null? 0 : Long.parseLong(pv);
		
	}
	@Override
	public Map<String,String> getOverall() {
		Map<String,String> map = new HashMap<>();
		// TODO 在线用户数，效率太低，要改进
		long userOnline = redisTemplate.keys(RedisKeyUtil.getSessionKey("*")).size();
		map.put("userOnline", String.valueOf(userOnline));
		String key = RedisKeyUtil.getPVKey("index", -1);
		map.put("index", valOps.get(key));
		return map;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		valOps = redisTemplate.opsForValue();
	}
}
