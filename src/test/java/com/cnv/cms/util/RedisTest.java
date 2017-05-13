/*package com.cnv.cms.util;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.cnv.cms.CmsSpringbootApplication;
import com.cnv.cms.model.LoginSession;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={CmsSpringbootApplication.class})
public class RedisTest {

    @Autowired  
    private RedisTemplate<String,String> redisTemplate;  
    
    @Resource(name="redisTemplate")
    private ListOperations<String, LoginSession> listOps;
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps2;
    
    @Resource(name="redisTemplate")
    private SetOperations<String, String> setOps;
    
    @Before
    public void tearUp() {
       //redisTemplate.setKeySerializer(new StringRedisSerializer());
    }  
    
    @After
    public void tearDown() {
       redisTemplate.getConnectionFactory().getConnection().flushDb();
    }    
	@Test
	public void testRedisTemplate() {
		System.out.println("---------RedisTemplate Test----------");
		ValueOperations<String, String> vr = redisTemplate.opsForValue();
		
		ListOperations<String, String>lops = redisTemplate.opsForList();
		
		lops.leftPush("list1", "val1");
		vr.set("t1", "aaa");
		vr.set("t2", "bbb");
		Set<String> set = redisTemplate.keys("*");
		System.out.println("Keys:"+set.toString());
		//redisTemplate.delete("t1");
		
	}
	@Test
	public void testListOperations() {
		System.out.println("---------Redis List Ops Test----------");
		LoginSession ls = new LoginSession();
		ls.setId(1111);
		listOps.leftPush("login1", ls);
		ls.setUserid(2222);
		listOps.leftPush("login1", ls);
		
		List<LoginSession> list =  listOps.range("login1", 0, -1);
		System.out.println("Read value:"+list);
		
	}
	@Test
	public void testListOperations2() {
		System.out.println("---------Redis List Ops Test2----------");
		listOps2 = redisTemplate.opsForList();
		LoginSession ls = new LoginSession();
		ls.setId(1111);
		listOps2.leftPush("login2", "hahha");
		listOps2.leftPush("login2", "jjjjjj");
		Set<String> set = redisTemplate.keys("*");
		System.out.println("Keys:"+set);
		
		List<String> list =  listOps2.range("login2", 0, -1);
		System.out.println("Read value:"+list);
		//redisTemplate.delete("t1");
		
	}
	@Test
	public void testSetOperations() {
		System.out.println("---------Redis Set Ops Test----------");
		setOps.add("set1", "valset1");
		setOps.add("set1", "valset2");
		//setOps.members("set1");
		System.out.println("Set:"+setOps.members("set1"));

		
	}
}
*/