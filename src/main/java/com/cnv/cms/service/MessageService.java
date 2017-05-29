package com.cnv.cms.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cnv.cms.model.Message;

public interface MessageService extends BaseService<Message>{
	
	public int getUnReadCount();
	
	public List<Message>  listByConversation(String id);
	

	public List<Object>  listConversationsByUserId(int id);

	
	public int updateStatus(@Param("id")int id, @Param("status")int status);
}
