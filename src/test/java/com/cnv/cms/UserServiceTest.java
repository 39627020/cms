package com.cnv.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cnv.cms.service.FollowService;
import com.cnv.cms.service.UserService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={CmsSpringbootApplication.class},webEnvironment=SpringBootTest.WebEnvironment.NONE)
public class UserServiceTest {

    @Autowired  
    private UserService userService;
	@Autowired
	private FollowService followService;

	
	@Test
	public void testRedis() throws Exception{
		System.out.println("----Redis Template Test-----");
		followService.addFollow(26, 42);
	}
	

}
