package com.cnv.cms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnv.cms.mapper.MessageMapper;
import com.cnv.cms.model.HostHolder;
import com.cnv.cms.model.Message;
import com.cnv.cms.service.MessageService;
import com.cnv.cms.service.UserService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private HostHolder hostHolder;
	
	@Override
	public boolean add(Message t) {
		return messageMapper.add(t) > 0;
	}

	@Override
	public boolean delete(int id) {
		return messageMapper.updateStatus(id, 0)>0;
	}

	@Override
	public boolean update(Message t) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Message selectById(int id) {
		return messageMapper.getById(id);
	}

	@Override
	public int getUnReadCount() {
		return messageMapper.getUnReadCount();
	}

	@Override
	public List<Message> listByConversation(String id) {
		return messageMapper.listByConversation(id);
	}

	@Override
	public List<Object> listConversationsByUserId(int id) {
		List<Object> list = new ArrayList();
		List<Message> msgs = messageMapper.listConversationsByUserId(id);
		for(Message m : msgs) {
			Map<String,Object> oneMsg = new HashMap<>();
			oneMsg.put("message", m);
			int userId = hostHolder.getUserId();
			int fromid = m.getFromId();
			if(fromid!=userId){
				userId = fromid;
			}else{
				userId = m.getToId();
			}
			oneMsg.put("username", userService.selectById(userId).getNickname());
			oneMsg.put("userid",userId);
			list.add(oneMsg);
		}
		return list;
	}


	@Override
	public int updateStatus(int id, int status) {
		// TODO Auto-generated method stub
		return messageMapper.updateStatus(id, status);
	}

}
