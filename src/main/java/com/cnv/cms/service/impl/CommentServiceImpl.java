package com.cnv.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventProducer;
import com.cnv.cms.event.EventType;
import com.cnv.cms.mapper.CommentMapper;
import com.cnv.cms.model.Comment;
import com.cnv.cms.service.CommentService;
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private EventProducer eventProducer;
	
	@Override
	public boolean add(Comment t) {
		int rs = commentMapper.add(t);
		
		
		//System.out.println("commentMapper Add return : "+rs);
		
/*	
		EventModel event = new EventModel();
		event.setOwnerId(t.getUserId())
			.setEventType(EventType.COMMENT)
			.addExtData("articleId", t.getEntityId())
			.addExtData("entityType", t.getEntityType())
			.addExtData("entityId", t.getEntityId());
		eventProducer.addEvent(event);*/
		return  true;
	}

	@Override
	public boolean delete(int id) {
		return commentMapper.updateStatus(id, 0)>0;
	}

	@Override
	public boolean update(Comment t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Comment selectById(int id) {
		return commentMapper.getById(id);
	}

	@Override
	public List<Comment> listByEntity(int entityId, int entityType) {	
		return commentMapper.listByEntity(entityId, entityType);
	}

}
