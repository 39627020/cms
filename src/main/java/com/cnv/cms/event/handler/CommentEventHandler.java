package com.cnv.cms.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventType;
import com.cnv.cms.model.Article;
import com.cnv.cms.model.Message;
import com.cnv.cms.model.SystemUser;
import com.cnv.cms.service.ArticleService;
import com.cnv.cms.service.MessageService;
import com.cnv.cms.service.UserService;

@Component
public class CommentEventHandler implements EventHandler {
	private Logger logger = LoggerFactory.getLogger(CommentEventHandler.class);
	@Autowired
	private MessageService messageService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private UserService userService;
	
	@Override
	public void handle(EventModel event) {
		
		int userid = event.getOwnerId();
		String userName = userService.selectById(userid).getUsername();
		int articleId = (int) event.getExtData("articleId");
		int fromId = SystemUser.NOTIFICATION;
		Article art = articleService.selectById(articleId);
		int toId = art.getUserId();
		String conversationId=MessageService.getConversationId(fromId, toId);
		Message message = new Message();
		message.setFromId(SystemUser.NOTIFICATION);
		message.setToId(toId);
		message.setConversationId(conversationId);
		message.setContent("用户 "+userName+" 评论了您的文章  <a href='../article.html?id="+articleId+"'>"+art.getTitle()+"</a>, 赶快去看看吧 ^_^");
		messageService.add(message);
		
		logger.info("CommentEventHandler do handle :" + articleId);
	}

	@Override
	public EventType[] getSupposortEventTypes() {
		return new EventType[]{EventType.COMMENT};
	}


}
