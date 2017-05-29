package com.cnv.cms.controller.api;

import java.util.HashMap;
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
import com.cnv.cms.model.HostHolder;
import com.cnv.cms.model.Message;
import com.cnv.cms.service.MessageService;
import com.cnv.cms.service.UserService;

@AuthClass
@RestController
@RequestMapping("/api/message")
public class MessageController {
	
	private final Logger logger = LoggerFactory.getLogger(MessageController.class);
	

	@Autowired
	private HostHolder hostHolder;
    @Autowired  
    private MessageService messageService; 
    @Autowired
    private UserService userService;
    

	@AuthMethod(role="base")
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public   void  addMessage(String toId, String content){
		if(toId==null || content==null)
			return;
		System.out.println(content);
		int hostId = hostHolder.getUserId();
		int userId = Integer.parseInt(toId);
		Message message = new Message();
		
		message.setFromId(hostId);
		message.setToId(userId);
		message.setContent(content);
		String conversationId=null;
		
		if(hostId<userId){
			conversationId =  hostId+"_"+userId;
		}else{
			conversationId =  userId+"_"+hostId;
		}
		message.setConversationId(conversationId);
		messageService.add(message);
	
	}
	@AuthMethod(role="base")
	@RequestMapping(value="/update",method=RequestMethod.GET)
	public   @ResponseBody Map<String, Object>  update(@PathVariable int aid){
		
		Map<String, Object> map = new HashMap<String, Object>();

		return map;
	}
	
}
