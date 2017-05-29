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
    String INSERT_FIELDS = " from_id, to_id, create_date, conversation_id, content,  status, has_read ";
    String SELECT_FIELDS = " id, from_id as fromId, to_id as toId, create_date as createdDate,  conversation_id as conversationId, content, status, has_read as hasRead ";
	 
	@Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") "
			+ "values(#{fromId},#{toId},#{createdDate},#{conversationId},#{content},#{status},#{hasRead});"})
	public int add(Message message);
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
	public Message getById(int id);
	
	@Select({"select count(*) from ",TABLE_NAME," where has_read=0"})
	public int getUnReadCount();
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where conversation_id=#{id} order by create_date;"})
	public List<Message>  listByConversation(String id);
	

	@Select({"select ",SELECT_FIELDS,", count(if(has_read=0,true,null)) as count from ","(select * from ",TABLE_NAME," where from_id=#{id} order by create_date desc) as tc group by conversation_id;"})
	public List<Message>  listConversationsByUserId(int id);
	

	@Delete({"delete from ",TABLE_NAME," where id=#{id}"})
	public int deleteById(int id);
	
	@Delete({"update from ",TABLE_NAME,"set status=#{status} where id=#{id}"})
	public int updateStatus(@Param("id")int id, @Param("status")int status);
}
