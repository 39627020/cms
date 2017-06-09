package com.cnv.cms.event.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventType;
import com.cnv.cms.model.Article;
import com.cnv.cms.model.Feed;
import com.cnv.cms.model.type.EntityType;
import com.cnv.cms.service.ArticleService;
import com.cnv.cms.service.FeedService;
import com.cnv.cms.service.UserService;

@Component
public class FeedEventHandler implements EventHandler {
	private Logger logger = LoggerFactory.getLogger(FeedEventHandler.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private FeedService feedService;
	
	@Override
	public void handle(EventModel event) {

		EventType type = event.getEventType();
		Feed feed = new Feed();
		feed.setUserId(event.getOwnerId());
		feed.setCreateDate(new Date());
		feed.setContent(this.buildFeedContent(event));
		feed.setType(type.getValue());
		feed.setStatus(1);
		feedService.add(feed);
	}

	@Override
	public EventType[] getSupposortEventTypes() {
		return new EventType[]{EventType.FOLLOW,EventType.UNFOLLOW,
				EventType.COMMENT,EventType.NEWS_PUBLISH};
	}

	private String buildFeedContent(EventModel event){
		Map<String,Object> map = new HashMap<>();
		int actorUserId = event.getOwnerId();
		String actroUserName = userService.selectById(actorUserId).getUsername();
		
		map.put("actorUserId", actorUserId);
		map.put("actroUserName", actroUserName);
	
		EventType type = event.getEventType();
		
		if(type==EventType.NEWS_PUBLISH){
			int aid = (int) event.getExtData("articleId");
			//Article art = articleService.selectById(aid);
			map.put("articleId", aid);
			map.put("articleTitle", event.getExtData("articleTitle"));
			
		}else if(type==EventType.FOLLOW || type==EventType.UNFOLLOW){
			int receiverId = (int) event.getExtData("userId");
			String receiverUserName = userService.selectById(receiverId).getUsername();
			map.put("receiverId", receiverId);
			map.put("receiverUserName", receiverUserName);
		}else if(type==EventType.COMMENT){
			//System.out.println(event);
			int commentType = (int)event.getExtData("commentType");
			if(commentType == EntityType.ENTITY_NEWS){
				int aid = (int) event.getExtData("articleId");
				Article art = articleService.selectById(aid);
				map.put("articleId", aid);
				map.put("articleTitle", art.getTitle());
				map.put("commentId", event.getExtData("commentId"));
			}
		}
		String content  = JSON.toJSONString(map);
		return content;
	}
}
