package com.cnv.cms.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.cnv.cms.model.LoginSession;

public interface LoginSessionMapper {
	String TABLE_NAME = "t_loginsession";
	String FIDLD1 = "id";
	String FIDLD2 = "sessionid";
	String FIDLD3 = "userid";
	String FIDLD4 = "admin";
	String FIDLD5 = "expired";
	
	@Insert({"insert into ",TABLE_NAME," values(null,#{",FIDLD2,"},#{",FIDLD3,"},#{",FIDLD4,"},#{",FIDLD5,"});"})
	public void add(LoginSession session);
	
	@Select({"select * from ",TABLE_NAME," where userid=#{id}"})
	public LoginSession selsetByUser(int id);

	@Select({"select * from ",TABLE_NAME," where sessionid=#{id}"})
	public LoginSession selsetBySession(String id);
	
	@Delete({"delete from ",TABLE_NAME," where userid=#{id}"})
	public void deleteByUser(int id);
	
	@Delete({"delete from ",TABLE_NAME," where sessionid=#{id}"})
	public void deleteBySession(String id);
}
