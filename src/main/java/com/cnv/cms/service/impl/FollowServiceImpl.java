package com.cnv.cms.service.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import com.cnv.cms.event.EventConsumer;
import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventProducer;
import com.cnv.cms.event.EventType;
import com.cnv.cms.service.FollowService;
import com.cnv.cms.util.RedisKeyUtil;

@Service
public class FollowServiceImpl implements FollowService {
	private final Logger logger = LoggerFactory.getLogger(FollowServiceImpl.class);
	
	@Autowired
	private EventProducer eventProducer;
	
	@Autowired  
	private RedisTemplate<String,String> redisTemplate; 
	
	
	@Override
	public void addFollow(int userId, int followId) {
		// TODO Auto-generated method stub
		SetOperations<String, String> opsSet = redisTemplate.opsForSet();
		String key1 = RedisKeyUtil.getUserFollowKey(userId);
		String key2 = RedisKeyUtil.getUserFansKey(followId);
		
		
		opsSet.add(key1, String.valueOf(followId));
		Long count1 = opsSet.size(key2);
		opsSet.add(key2, String.valueOf(userId));
		Long count2 = opsSet.size(key2);
		
		
		//发出关注事件
		EventModel event = new EventModel();
		event.setOwnerId(userId)
			.setEventType(EventType.FOLLOW)
			.addExtData("userId", followId)
			.addExtData("countBefore", count1)
			.addExtData("countAfter", count2);
		eventProducer.addEvent(event);
		
		//logger.info("add follow");
	}
	@Override
	public void removeFollow(int userId, int followId) {
		// TODO Auto-generated method stub
		SetOperations<String, String> opsSet = redisTemplate.opsForSet();
		String key1 = RedisKeyUtil.getUserFollowKey(userId);
		String key2 = RedisKeyUtil.getUserFansKey(followId);

		opsSet.remove(key1, String.valueOf(followId));
		Long count1 = opsSet.size(key2);
		opsSet.remove(key2, String.valueOf(userId));
		Long count2 = opsSet.size(key2);
		
		//发出取消关注事件
		EventModel event = new EventModel();
		event.setOwnerId(userId)
			.setEventType(EventType.UNFOLLOW)
			.addExtData("userId", followId)
			.addExtData("countBefore", count1)
			.addExtData("countAfter", count2);
		eventProducer.addEvent(event);
		
		
		//logger.info("remove follow");
	}
	@Override
	public long getFollowNum(int userId) {
		// TODO Auto-generated method stub
		SetOperations<String, String> opsSet = redisTemplate.opsForSet();

		String key1 = RedisKeyUtil.getUserFansKey(userId);
		Long size = opsSet.size(key1);
		return size==null ? 0 : size;
		
	}
	@Override
	public Set<String> getFollows(int userId) {
		// TODO Auto-generated method stub
		SetOperations<String, String> opsSet = redisTemplate.opsForSet();

		String key1 = RedisKeyUtil.getUserFollowKey(userId);
		return opsSet.members(key1);
		
	}
	@Override
	public Set<String> getFans(int userId) {
		// TODO Auto-generated method stub
		SetOperations<String, String> opsSet = redisTemplate.opsForSet();

		String key1 = RedisKeyUtil.getUserFansKey(userId);
		return opsSet.members(key1);
		
	}
	@Override
	public boolean isFollowedBy(int followedId, int userId) {
		// TODO Auto-generated method stub
		SetOperations<String, String> opsSet = redisTemplate.opsForSet();

		String key1 = RedisKeyUtil.getUserFansKey(followedId);
		return opsSet.isMember(key1, String.valueOf(userId));
		
	}
}
