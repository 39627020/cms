package com.cnv.cms.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cnv.cms.CmsSpringbootApplication;
import com.cnv.cms.model.User;

//@MapperScan(basePackages="com.cnv.cms.mapper")//mybatis Mapper 扫描包
@RunWith(SpringRunner.class)
//@SpringBootTest
@SpringBootTest(classes={CmsSpringbootApplication.class},webEnvironment=SpringBootTest.WebEnvironment.NONE)
public class UserMapperTest {

    @Autowired  
    private UserMapper userMapper;  

	
	@Test
	public void testRedis() throws Exception{
		System.out.println("----UserMapper Test-----");
		User user = userMapper.selectUserByID(27);
		
		System.out.println(user);
	}
	

}
