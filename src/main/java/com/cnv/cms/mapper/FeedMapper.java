package com.cnv.cms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cnv.cms.model.Feed;
import com.cnv.cms.model.Message;

public interface FeedMapper {
	String TABLE_NAME = "t_feed";
    String INSERT_FIELDS = " userId, type, content, createDate,status ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;
	 
	@Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") "
			+ "values(#{id},#{uesrId},#{type},#{content},#{createDate},#{status});"})
	public int add(Feed feed);
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
	public Feed getById(int id);
	
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where uesrId=#{userId} order by create_date desc limit #{num};"})
	public List<Feed>  listByUserId(@Param("userId")int userId,@Param("num")int num);
		

	@Delete({"delete from ",TABLE_NAME," where id=#{id}"})
	public int deleteById(int id);
	
	@Delete({"update from ",TABLE_NAME,"set status=#{status} where id=#{id}"})
	public int updateStatus(@Param("id")int id, @Param("status")int status);
}
