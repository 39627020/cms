package com.cnv.cms.event;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.cnv.cms.util.JsonUtil;
import com.cnv.cms.util.RedisKeyUtil;

@Component
public class EventProducer implements InitializingBean{
    @Autowired  
    private RedisTemplate redisTemplate;  
    
    private ListOperations<String,String> listOps=null;
    
	public void addEvent(EventModel event){
		String key  = RedisKeyUtil.getEventQueueKey();
		listOps.rightPush(key, JsonUtil.toJSon(event));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		listOps = redisTemplate.opsForList();		
	}
}
