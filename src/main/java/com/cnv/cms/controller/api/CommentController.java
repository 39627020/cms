package com.cnv.cms.controller.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cnv.cms.authority.AuthClass;
import com.cnv.cms.authority.AuthMethod;
import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventProducer;
import com.cnv.cms.event.EventType;
import com.cnv.cms.model.Article;
import com.cnv.cms.model.Comment;
import com.cnv.cms.model.HostHolder;
import com.cnv.cms.model.SystemUser;
import com.cnv.cms.model.type.EntityType;
import com.cnv.cms.service.ArticleService;
import com.cnv.cms.service.CommentService;
import com.cnv.cms.service.UserService;

@AuthClass
@RestController
@RequestMapping("/api/comment")
public class CommentController {
	
	private final Logger logger = LoggerFactory.getLogger(CommentController.class);
	

	@Autowired
	private HostHolder hostHolder;
    @Autowired  
    private CommentService commentService; 
    @Autowired
    private UserService userService;
	
	@Autowired
	private EventProducer eventProducer;

	@AuthMethod(role="base")
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public   void  addComment(int articleId, String content){
		Comment omment = new Comment();
		omment.setUserId(hostHolder.getUserId());
		omment.setCreatedDate(new Date());
		omment.setEntityId(articleId);
		omment.setEntityType(EntityType.ENTITY_NEWS);
		omment.setContent(content);
		omment.setStatus(1);
		
		commentService.add(omment);
		
		eventProducer.addEvent(new EventModel()
    			.setEventType(EventType.COMMENT)
    			.setOwnerId(hostHolder.getUserId())
    			.addExtData("articleId", articleId)
    			);
		//return null;
	}
	@AuthMethod(role="customer")
	@RequestMapping(value="/comments/{aid}",method=RequestMethod.GET)
	public   @ResponseBody Map<String, Object>  listComment(@PathVariable int aid){
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Comment> comlist = commentService.listByEntity(aid, EntityType.ENTITY_NEWS);
		List<Map<String, Object>>  datalist = new ArrayList<>();
		for(Comment c : comlist){
			Map<String, Object> mapc = new HashMap<String, Object>();
			mapc.put("user", userService.selectById(c.getUserId()).getUsername());
			mapc.put("comment", c);
			datalist.add(mapc);
		}
		
		map.put("data", datalist);
		return map;
	}
	
}
