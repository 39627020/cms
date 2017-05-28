package com.cnv.cms.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventType;
import com.cnv.cms.service.PVService;
import com.cnv.cms.util.RedisKeyUtil;

@Component
public class TimeCountEventHandler implements EventHandler , InitializingBean{
	private Logger logger = LoggerFactory.getLogger(TimeCountEventHandler.class);
	@Autowired  
	private RedisTemplate redisTemplate; 
	private ValueOperations<String,String> valOps=null;
	
	@Override
	public void handle(EventModel event) {
		
		String url  = (String) event.getExtData("url");
		if(url==null)
			return;
		/*if(!url.endsWith(".html")){
			url = url.substring(0, url.lastIndexOf('/'));
		}*/
		Integer cost = (Integer) event.getExtData("cost");
		String method =(String) event.getExtData("method");
		String key = RedisKeyUtil.getTimeCostKey(url,method);
		valOps.increment(key, cost);
		valOps.increment(key+":pv",1);
		//logger.info("TimeCountEventHandler do handle :" + url+":"+method+", cost: "+cost);
		
	}

	@Override
	public EventType[] getSupposortEventTypes() {
		return new EventType[]{EventType.TIME_COUNT};
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		valOps = redisTemplate.opsForValue();
	}

}
