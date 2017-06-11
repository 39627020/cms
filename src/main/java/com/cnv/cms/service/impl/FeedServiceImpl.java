package com.cnv.cms.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Ids;

import com.alibaba.fastjson.JSON;
import com.cnv.cms.config.CmsConfig;
import com.cnv.cms.event.EventType;
import com.cnv.cms.mapper.FeedMapper;
import com.cnv.cms.model.Feed;
import com.cnv.cms.service.CacheService;
import com.cnv.cms.service.CommentService;
import com.cnv.cms.service.FeedService;
import com.cnv.cms.service.FollowService;
import com.cnv.cms.service.UserService;
import com.cnv.cms.util.RedisKeyUtil;

@Service
public class FeedServiceImpl implements FeedService, InitializingBean{

	@Autowired
	private FeedMapper feedMapper;
	@Autowired
	private CommentService commentService;
	//@Autowired
	//private UserService userService;
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private CacheService cacheService;
	
	ZSetOperations<String,String> zsetOps = null;
	HashOperations<String,String,Long> hashOps=null;
	
	@Autowired
	private FollowService followService;
	
	@Override
	public boolean add(Feed feed) {
		// TODO Auto-generated method stub
		return feedMapper.add(feed)>0;
	}

	@Override
	public Feed getById(int id) {
		// TODO Auto-generated method stub
	    Feed feed = cacheService.getCache("feedCache", "getById"+id, Feed.class);
		if(feed !=null)
			return feed;
		feed = feedMapper.getById(id);
		cacheService.addCache("feedCache", "getById"+id, 300, feed);
	    return feed;
	}

	@Override
	public List<Feed> listByUserId(int userId, int offset, int num) {
		// TODO Auto-generated method stub
		return feedMapper.listByUserId(userId, offset, num);
	}
	/*
	 *@Param date 查询date日期之后的feed
	 *@Param userId 用户id
	 *@Param offset 起始位置
	 *@Param num 查询的数量
	 * @see com.cnv.cms.service.FeedService#listByUserId(int, java.util.Date, int, int)
	 */ 
	@Override
	public List<Feed> listByUserId(int userId, Date date, int offset, int num) {
		List<Feed> feeds = feedMapper.listByUserIdAfter(userId, date, offset, num);
		//TODO 加入缓存
		return feeds;
	}

	@Override
	public boolean deleteById(int id) {
		// TODO Auto-generated method stub
		cacheService.removeCache("feedCache", "getById"+id);
		return feedMapper.updateStatus(id, 0)>0;
	}

	@Override
	public boolean updateStatus(int id, int status) {
		// TODO Auto-generated method stub
		return feedMapper.updateStatus(id, status)>0;
	}
	@Override
	public List<Feed> listByIds(Set<String> ids) {
		// TODO Auto-generated method stub
		List<Feed> feeds = new ArrayList<>();
		//查询feed
		for(String id : ids){
			Feed feed = this.getById(Integer.valueOf(id));
			if(feed.getType()== EventType.COMMENT.getValue()){
				Integer commentId = (Integer) feed.get("commentId");
				if(commentId!=null)
					feed.addContent("comment", commentService.selectById(commentId).getContent());
			}
			feeds.add(feed);
		}
		//排序
		Collections.sort(feeds, Collections.reverseOrder());
		
		return feeds;
	}
	@Override
	public List<Feed> pollFeeds(int userId, int offset, int num) {
		Set<String> followIds = followService.getFollows(userId);
		return this.selectFromUserList(followIds, offset, num);
	}
	@Override
	public List<Feed> selectFromUserList(Set<String> userIds, int offset, int num) {
		// TODO Auto-generated method stub
		List<String> userList = new ArrayList<>(userIds);
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("users", userList);
		params.put("offset", offset);
		params.put("num", num);
		List<Feed> list = feedMapper.selectFromUserList(params);
		for(Feed feed : list){
			if(feed.getType()== EventType.COMMENT.getValue()){
				Integer id = (Integer) feed.get("commentId");
				if(id!=null)
					feed.addContent("comment", commentService.selectById(id).getContent());
			}
		}
		return list;
	}
	@Override
	public void updateTimeline(int userId) {
		Set<String> followIds = followService.getFollows(userId);
		//遍历每个关注的账号
		for(String folloId : followIds){
			Set<String> fanIds = followService.getFans(Integer.valueOf(folloId));
			//关注的人如果是大号,查询更新时间表，poll出最新的feed
			if(fanIds.size()>=CmsConfig.getPushBound()){
				//根据更新时间表，poll最新的feed
				Date date = this.getFeedUpdatetime(String.valueOf(userId), folloId);
				List<Feed> pollFeeds = this.listByUserId(Integer.valueOf(folloId), date, 0, CmsConfig.getTimelineLen());
				//push到自己的timeline
				this.pushFeedsToUser(pollFeeds, userId);
				//设置timeline更新时间
				this.setFeedUpdateTime(userId, folloId, date);
			}
		}
		//删除多余的feed，timeline中只保留100个
		this.retainFeedsInTimeline(userId, CmsConfig.getTimelineLen());
	}
	@Override
	public List<Feed> getFeeds(int userId, int offset, int len) {
		//只在拉取第一页时更新timeline
		if(offset==0 &&  len>0){
			this.updateTimeline(userId);
		}
		List<Feed> feeds = null;
		
		//从timeline中查询feed流
		String key  = RedisKeyUtil.getUserFeedsQueueKey(userId);
		
		Set<String> ids = zsetOps.reverseRange(key, offset, offset+len-1);
		//如果timeline中没有足够的数据
		if(ids==null || ids.size()<len){
			feeds = this.pollFeeds(userId, offset, len);
			//poll到的数据存到timeline
			//超出timeline范围的，暂时缓存到timeline，下次刷新时在updateTimeline中删除
			this.pushFeedsToUser(feeds, userId);
		}else{
			//根据feed流读取feed
			feeds = this.listByIds(ids);
		}
		
/*		//如果数据在timeline中
		if(offset+len <= CmsConfig.getTimelineLen()){

		}else{
			//否则从数据库poll数据
			feeds = this.pollFeeds(userId, offset, len);
			this.pushFeedsToUser(feeds, userId);
		}*/

		return feeds;
	}
	@Override
	public void saveAndPushFeed(Feed feed) {
		//保存feed到数据库
		this.add(feed);
		//获得用户follower
		int userId = feed.getUserId();
		Set<String> folloIds = followService.getFans(userId);
		
		if(folloIds.size()<CmsConfig.getPushBound()){
			//关注者人数少，推送给所有订阅用户
			this.pushFeedToUsers(feed, folloIds);
		}else{
			//关注者人数多
			// TODO 
		}
	}
	/*
	 * 推送给所有订阅用户(non-Javadoc)
	 * @see com.cnv.cms.service.FeedService#pushFeed(com.cnv.cms.model.Feed)
	 */
	@Override
	public void pushFeed(Feed feed) {
		// TODO Auto-generated method stub
		int userId = feed.getUserId();
		Set<String> folloIds = followService.getFollows(userId);
		
		//推送给所有订阅用户
		this.pushFeedToUsers(feed, folloIds);

	}
	@Override
	public void retainFeedsInTimeline(int userId, int len) {
		//ZSetOperations<String,String> zsetOps=redisTemplate.opsForZSet();
		String key  = RedisKeyUtil.getUserFeedsQueueKey(userId);
		Long timeLineLen = zsetOps.size(key);
		if(timeLineLen != null && timeLineLen>len){
			zsetOps.removeRange(key, 0, timeLineLen-len-1);
		}
	}
	@Override
	public int getTimelineSize(int userId) {
		//ZSetOperations<String,String> zsetOps=redisTemplate.opsForZSet();
		String key  = RedisKeyUtil.getUserFeedsQueueKey(userId);
		Long timeLineLen = zsetOps.size(key);
		return timeLineLen.intValue();
	}
	private void pushFeedToUsers(Feed feed,Set<String> folloIds){
		//ZSetOperations<String,String> zsetOps=redisTemplate.opsForZSet();
		//存储到zset, 以feedId为key， 时间戳为score
		 for(String userId : folloIds){
			String key  = RedisKeyUtil.getUserFeedsQueueKey(Integer.valueOf(userId));
			zsetOps.add(key, String.valueOf(feed.getId()), feed.getCreateDate().getTime());
	
		 }
		 
	}
	private void pushFeedsToUser(List<Feed> feeds, int targetUserId){
		//ZSetOperations<String,String> zsetOps=redisTemplate.opsForZSet();
		String key  = RedisKeyUtil.getUserFeedsQueueKey(targetUserId);
		//存储到zset, 以feedId为key， 时间戳为score
		for(Feed feed : feeds){
			zsetOps.add(key, String.valueOf(feed.getId()), feed.getCreateDate().getTime());
		}
		
		 
	}	
	private Date getFeedUpdatetime(String userId, String targetUserId){
		//HashOperations<String,String,Long> hashOps=redisTemplate.opsForHash();
		String  key = RedisKeyUtil.getFeedUpdateTimeKey(Integer.valueOf(userId));
		//hashOps.put(key, folloId, 0);
		Long timeLong = hashOps.get(key, targetUserId);
		if(timeLong==null)
			timeLong = 1L;
		Date date = new Date(timeLong);
		return date;
	}
	@Override
	public  void setFeedUpdateTime(int userId, String targetUserId, Date date){
		//HashOperations<String,String,Long> hashOps=redisTemplate.opsForHash();
		String  key = RedisKeyUtil.getFeedUpdateTimeKey(Integer.valueOf(userId));
		hashOps.put(key, targetUserId, date.getTime());
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		zsetOps = redisTemplate.opsForZSet();
		hashOps = redisTemplate.opsForHash();
	}
}
