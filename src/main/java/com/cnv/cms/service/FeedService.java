package com.cnv.cms.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cnv.cms.model.Article;
import com.cnv.cms.model.Feed;

public interface FeedService {
	public boolean add(Feed feed);
	
	public Feed getById(int id);
	
	
	public List<Feed>  listByUserId(int userId, int offset, int num);
		

	public boolean deleteById(int id);
	
	public boolean updateStatus(int id, int status);


	List<Feed> selectFromUserList(Set<String> userIds, int offset, int num);

	void pushFeed(Feed feed);

	List<Feed> listByUserId(int userId, Date date, int offset, int num);

	void saveAndPushFeed(Feed feed);

	List<Feed> getFeeds(int userId, int offset, int len);

	void retainFeedsInTimeline(int userId, int len);

	void setFeedUpdateTime(int userId, String targetUserId, Date date);

	void updateTimeline(int userId);

	List<Feed> listByIds(Set<String> ids);

	List<Feed> pollFeeds(int userId, int offset, int num);

	int getTimelineSize(int userId);

	void pushFeedToUsers(Feed feed, Set<String> folloIds);

	void pushFeedsToUser(List<Feed> feeds, int targetUserId);

	void pushFromUserToUser(int fromId, int toId);


	List<Feed> listByTimelineMsg(Set<String> usersAndIds);

	void pushToFans(int userId, int fanNum);


	
}
