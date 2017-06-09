package com.cnv.cms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cnv.cms.model.Comment;

public interface CommentMapper {
	String TABLE_NAME = "t_comment";
    String INSERT_FIELDS = " user_id, create_date, entity_id, entity_type, content, status ";
    String SELECT_FIELDS = " id, user_id as userId, create_date as createdDate, entity_id as entityId, entity_type as entityType, content, status ";
	 
	@Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") "
			+ "values(#{userId},#{createdDate},#{entityId},#{entityType},#{content},#{status});"})
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int add(Comment comment);
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
	public Comment getById(int id);

	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where entity_id=#{entityId} and entity_type=#{entityType};"})
	public List<Comment> listByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);
	
	@Delete({"delete from ",TABLE_NAME," where id=#{id}"})
	public int deleteById(int id);
	
	@Delete({"update from ",TABLE_NAME,"set status=#{status} where id=#{id}"})
	public int updateStatus(@Param("id")int id, @Param("status")int status);
}
