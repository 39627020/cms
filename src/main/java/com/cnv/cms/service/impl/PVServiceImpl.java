package com.cnv.cms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.cnv.cms.controller.AdminResourcesController;
import com.cnv.cms.service.PVService;
import com.cnv.cms.util.RedisKeyUtil;

@Service
public class PVServiceImpl implements PVService , InitializingBean{
	private final Logger logger = LoggerFactory.getLogger(PVServiceImpl.class);
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
		String key1 = RedisKeyUtil.getPVKey("index", -1);
		map.put("index", valOps.get(key1));
		
		String key2 = RedisKeyUtil.getPVKey("article*", -1);
		Set<String> newskeys = redisTemplate.keys(key2);
		long pvnews = 0;
		for(String key: newskeys){
			pvnews += Long.parseLong(valOps.get(key));
		}
		map.put("news", String.valueOf(pvnews));
		logger.info("overall info : "+map.toString());
		return map;
	}
	@Override
	public Map<String,Object> getTimeCost() {
		Map<String,Object> map = new HashMap<>();
		Map<String,Object>  pageTimeMap = new HashMap<>();
		Map<String,Object> methodTimemap = new HashMap<>();
		Map<String,Object> methodAveTimemap = new HashMap<>();
		String keyregx = RedisKeyUtil.getTimeCostKey("*", "hostholder");
		//得到所有的页面
		Set<String> pageKeys = redisTemplate.keys(keyregx);
		List<Object> xList = new ArrayList<>();
		List<Object> yList = new ArrayList<>();
		int i=0;
		for(String pkey : pageKeys){

			String page = pkey.split(":")[1];
			
			if(page.startsWith("/admin"))
				continue;
			
			List<Object> mList = new ArrayList<>();
			List<Object> mAveList = new ArrayList<>();
			String funregx = RedisKeyUtil.getTimeCostKey(page, "*:pv");
			Set<String> funKeys = redisTemplate.keys(funregx);
			//List<String> vals = valOps.multiGet(funKeys);
			for(String fkey : funKeys){
				String method = method = fkey.split(":")[2];
				long mcnt = Long.parseLong(valOps.get(fkey));
				String mtime = valOps.get(fkey.substring(0, fkey.lastIndexOf(':')));
				mList.add(new String[]{method,mtime});
				long mtave = Long.parseLong(mtime)/mcnt;
				mAveList.add(new String[]{method,String.valueOf(mtave)});
			}
			methodTimemap.put(page, mList);
			methodAveTimemap.put(page, mAveList);
			
			long mcnt = Long.parseLong(valOps.get(pkey+":pv"));
			long mtime = Long.parseLong(valOps.get(pkey));
			
			xList.add(new long[]{mtime/mcnt,i});
			yList.add(new String[]{String.valueOf(i),page});
			i++;
		}
		pageTimeMap.put("xdata", xList);
		pageTimeMap.put("ydata", yList);
		map.put("pages", pageTimeMap);
		map.put("methods", methodTimemap);
		map.put("methodsAve", methodAveTimemap);
		return map;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		valOps = redisTemplate.opsForValue();
	}
}
