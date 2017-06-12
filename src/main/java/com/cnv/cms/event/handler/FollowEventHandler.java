package com.cnv.cms.event.handler;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cnv.cms.config.CmsConfig;
import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventProducer;
import com.cnv.cms.event.EventType;
import com.cnv.cms.service.FeedService;
import com.cnv.cms.service.UserService;
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
			
			String watchkey = RedisKeyUtil.getFeedUpdateTimeKey(ownerId);
			ZSetOperations<String,String> zsetOps = redisTemplate.opsForZSet();
			HashOperations<String,String,Long> hashOps = redisTemplate.opsForHash();
			

			//redisTemplate.watch(watchkey);
			//redisTemplate.multi();
			
			//查询全部timeline
			String tiemlienKey  = RedisKeyUtil.getUserFeedsQueueKey(ownerId);
			Set<String> userAndIds = zsetOps.range(tiemlienKey, 0, -1);
			for(String msg : userAndIds){
				JSONObject feedObj = JSON.parseObject(msg);
				//如果是被取消关注的用户，从timelie中删除
				if(feedObj.getInteger("user")==userId){
					System.out.println("删除: key:"+tiemlienKey+",val:"+msg);
					zsetOps.remove(tiemlienKey, msg);
				}

			}
			//String  pollTable = RedisKeyUtil.getFeedUpdateTimeKey(ownerId);
			hashOps.delete(watchkey, String.valueOf(userId));
			
/*			
			List<Object> rs = redisTemplate.exec();
			//如果失败，event重新放进队列
			if(rs==null || rs.isEmpty()){
				eventProducer.addEvent(event);
				return;
			};*/
			
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
