package com.cnv.cms.event.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cnv.cms.config.CmsConfig;
import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventProducer;
import com.cnv.cms.event.EventType;
import com.cnv.cms.service.FeedService;
import com.cnv.cms.util.RedisKeyUtil;

@Component
public class FollowEventHandler implements EventHandler {
	private Logger logger = LoggerFactory.getLogger(FollowEventHandler.class);

	@Autowired
	private EventProducer eventProducer;

	@Autowired
	private FeedService feedService;
	@Autowired  
	private RedisTemplate<String,String> redisTemplate; 
	
	@Override
	public void handle(EventModel event) {

		EventType type = event.getEventType();
		int ownerId = event.getOwnerId();
		int userId = (int) event.getExtData("userId");
		Integer countBefore = (Integer) event.getExtData("countBefore");
		Integer countAfter =  (Integer) event.getExtData("countAfter");
		String key = RedisKeyUtil.getUserFansKey(userId);
		countAfter = redisTemplate.opsForSet().size(key).intValue();
		if(type == EventType.FOLLOW){

			//如果是小号, 大号不作处理
			if(countAfter < CmsConfig.getPushBound()){
				//取出被关注账号的feed，push到宿主用户
				feedService.pushFromUserToUser(userId, ownerId);
			}
			
			//二次验证粉丝数是否超过界限
			if(countBefore<CmsConfig.getPushBound() && countAfter>=CmsConfig.getPushBound()){
				//String watchkey = RedisKeyUtil.getFeedUpdateTimeKey(ownerId);
				//redisTemplate.watch(watchkey);
				feedService.pushToFans(userId, countAfter.intValue());
			}
		}else if(type == EventType.UNFOLLOW){

			//logger.info("handle unfollow");
			
			SessionCallback<List<Object>> sessionCallback = new SessionCallback<List<Object>>() {
				@Override
				public  List<Object> execute(RedisOperations operations) throws DataAccessException {
					
					String feedUpdateKey = RedisKeyUtil.getFeedUpdateTimeKey(ownerId);
					ZSetOperations<String,String> zsetOps =  operations.opsForZSet();
					HashOperations<String,String,Long> hashOps = operations.opsForHash();
					
					
					//查询全部timeline
					String tiemlienKey  = RedisKeyUtil.getUserFeedsQueueKey(ownerId);
					
					
					//logger.info("watchkey:"+feedUpdateKey+",tiemlienKey:"+tiemlienKey);
					
					Set<String> userAndIds = zsetOps.range(tiemlienKey, 0, -1);
					
					//watch key collection
					List<String> watchKeys = new ArrayList<>();
					watchKeys.add(feedUpdateKey);
					watchKeys.add(tiemlienKey);
					operations.watch(watchKeys);
					
					operations.multi();
					
					for(String msg : userAndIds){
						JSONObject feedObj = JSON.parseObject(msg);
						//如果是被取消关注的用户，从timelie中删除
						if(feedObj.getInteger("user")==userId){
							//System.out.println("删除: key:"+tiemlienKey+",val:"+msg);
							zsetOps.remove(tiemlienKey, msg);
						}

					}
					//String  pollTable = RedisKeyUtil.getFeedUpdateTimeKey(ownerId);
					hashOps.delete(feedUpdateKey, String.valueOf(userId));				
					
					List<Object> rs = operations.exec();
					return rs;
					
				}
			};
			
			List<Object> rs = redisTemplate.execute(sessionCallback);
			
			//如果失败，event重新放进队列
			if(rs==null || rs.isEmpty()){
				eventProducer.addEvent(event);
				return;
			};
			
			//二次验证粉丝数是否超过界限
			if(countBefore>=CmsConfig.getPushBound() && countAfter<CmsConfig.getPushBound()){
				feedService.pushToFans(userId, countAfter.intValue());
			}
		}
	}

	@Override
	public EventType[] getSupposortEventTypes() {
		return new EventType[]{EventType.FOLLOW,EventType.UNFOLLOW};
	}


}
