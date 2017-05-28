package com.cnv.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnv.cms.mapper.CommentMapper;
import com.cnv.cms.model.Comment;
import com.cnv.cms.service.CommentService;
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;
	
	@Override
	public boolean add(Comment t) {
		return commentMapper.add(t) > 0;
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
