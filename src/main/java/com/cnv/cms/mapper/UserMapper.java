package com.cnv.cms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cnv.cms.model.User;
@Mapper
public interface UserMapper {
	/*@Insert({"insert into t_user",
        "values(null,#{username},#{password},#{nickname},",
        	"#{email},#{phone},#{status},#{createDate});  "})*/
	void addUser(User user);
	//@Select({"select * from t_user where username like #{username};"})
	List<User> selectUsers(String username);
	//@Select({"select * from t_user where id=#{id};"})
	User selectUserByID(int id);
	//@Select({" select * from t_user where username=#{username};"})
	User selectUserByName(String username);
	void updateUser(User user);
	void deleteUser(int id);
	void deleteUserLargerByID(int id);
	
}
