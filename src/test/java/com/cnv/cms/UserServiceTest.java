package com.cnv.cms;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cnv.cms.model.User;
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
	public void testUser() throws Exception{
		System.out.println("----User Test-----");
		User user = userService.selectByIdWithoutRoleGroup(26);
/*		for(int i=20;i<50;i++){
			user.setUsername("test"+i);
			userService.add(user);
		}*/
		
		List<User> users = userService.listUsers();
		for(int i=10;i<users.size();i++){
			User u = users.get(i);
			followService.addFollow(u.getId(), 27);
		}
		
		int len = users.size();
		Random rand = new Random();
		for(int i=0;i<300;i++){
			int ui1= rand.nextInt(len);
			int ui2= rand.nextInt(len);
			followService.addFollow(users.get(ui1).getId(), users.get(ui2).getId());
		}
		
	}
	

}
