package com.cnv.cms.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.cnv.cms.event.handler.EventHandler;
import com.cnv.cms.util.JsonUtil;
import com.cnv.cms.util.RedisKeyUtil;

@Component
public class EventConsumer implements InitializingBean , ApplicationContextAware{
	private final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
	
	@Autowired  
    private RedisTemplate redisTemplate;  
    
    private ListOperations<String,String> listOps=null;
    
    private Map<EventType,List<EventHandler>> handlerMap = new HashMap<>();
    
    private ApplicationContext applicationContext;

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		listOps = redisTemplate.opsForList();	
		
		Map<String,EventHandler> handlers = applicationContext.getBeansOfType(EventHandler.class);
		logger.info("EventHandlers : "+ handlers.keySet());
		
		for(Entry<String, EventHandler> hdEntry :  handlers.entrySet()){
			EventHandler handler = hdEntry.getValue();
			for(EventType type :handler.getSupposortEventTypes()){
				if(!handlerMap.containsKey(type)){
					handlerMap.put(type, new ArrayList<EventHandler>());
				}
				this.handlerMap.get(type).add(handler);
			}
		}
		
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					String key = RedisKeyUtil.getEventQueueKey();
					String eveStr = listOps.leftPop(key,30, TimeUnit.SECONDS);
					
					if(eveStr != null){
						EventModel event = JsonUtil.readValue(eveStr,EventModel.class);
						List<EventHandler> handlers = handlerMap.get(event.getEventType());
						if(handlers != null){
							for( EventHandler handler : handlers){
								handler.handle(event);
								//logger.info("event handle---"+event);
							}
						}
						else{
							logger.error("EventHandler for "+event.getEventType()+" 未定义");
						}
					}
				}
			}
			
		});
		thread.start();
		logger.info("EventConsumer  init");
	}

	@Override
	public void setApplicationContext(ApplicationContext app) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = app;
		logger.info("EventConsumer of  setApplicationContext");
	}
}
