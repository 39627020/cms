package com.cnv.cms.service;

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
}
