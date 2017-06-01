package com.cnv.cms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnv.cms.mapper.FeedMapper;
import com.cnv.cms.model.Article;
import com.cnv.cms.model.Feed;
import com.cnv.cms.service.FeedService;

@Service
public class FeedServiceImpl implements FeedService{

	@Autowired
	private FeedMapper feedMapper;
	@Override
	public boolean add(Feed feed) {
		// TODO Auto-generated method stub
		return feedMapper.add(feed)>0;
	}

	@Override
	public Feed getById(int id) {
		// TODO Auto-generated method stub
		return feedMapper.getById(id);
	}

	@Override
	public List<Feed> listByUserId(int userId, int offset, int num) {
		// TODO Auto-generated method stub
		return feedMapper.listByUserId(userId, offset, num);
	}

	@Override
	public boolean deleteById(int id) {
		// TODO Auto-generated method stub
		return feedMapper.updateStatus(id, 0)>0;
	}

	@Override
	public boolean updateStatus(int id, int status) {
		// TODO Auto-generated method stub
		return feedMapper.updateStatus(id, status)>0;
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
		return list;
	}

}
