package com.cnv.cms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cnv.cms.model.Comment;
import com.cnv.cms.model.Message;

public interface MessageMapper {
	String TABLE_NAME = "t_message";
    String INSERT_FIELDS = " from_id, to_id, create_date, conversation_id, content,  status ";
    String SELECT_FIELDS = " id, from_id as fromId, to_id as toId, create_date as createdDate,  conversation_id as conversationId, content, status ";
	 
	@Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") "
			+ "values(#{fromId},#{toId},#{createdDate},#{conversationId},#{content},#{status});"})
	public int add(Message message);
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
	public Message getById(int id);
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where conversation_id=#{id} order by create_date;"})
	public List<Message>  listByConversation(String id);
	

	@Select({"select ",SELECT_FIELDS," from ","(select ",SELECT_FIELDS," from ",TABLE_NAME," where from_id=#{id} order by create_date) as tc group by conversation_id;"})
	public List<Message>  listConversationsByUserId(int id);
	

	@Delete({"delete from ",TABLE_NAME," where id=#{id}"})
	public int deleteById(int id);
	
	@Delete({"update from ",TABLE_NAME,"set status=#{status} where id=#{id}"})
	public int updateStatus(@Param("id")int id, @Param("status")int status);
}
