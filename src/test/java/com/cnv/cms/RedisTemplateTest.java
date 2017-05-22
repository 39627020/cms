package com.cnv.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.cnv.cms.event.EventModel;
import com.cnv.cms.util.RedisKeyUtil;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={CmsSpringbootApplication.class},webEnvironment=SpringBootTest.WebEnvironment.NONE)
public class RedisTemplateTest {

    @Autowired  
    private RedisTemplate redisTemplate;  
	
	@Test
	public void contextLoads() throws Exception{
		
	}
	
	//@Test
	public void testRedis() throws Exception{
		System.out.println("----Redis Template Test-----");
		ValueOperations<String,EventModel> valueOps=redisTemplate.opsForValue();
		valueOps.set("k1", new EventModel());
		EventModel rs = valueOps.get("k1");
		System.out.println("get: "+rs);
		//redisTemplate.delete("k1");
	}
	
	@Test
	public void testValueGet() throws Exception{
		System.out.println("----Redis Template Test-----");
		ValueOperations<String,Long> valueOps=redisTemplate.opsForValue();
		String key = "pv";
		//valOps.increment(key, 0);
		redisTemplate.getConnectionFactory().getConnection().flushDb();
		//redisTemplate.delete(key);
		//valueOps.set(key, (long) 5);
		valueOps.increment(key, 3);
		Object pv =  valueOps.get(key);
		System.out.println("pv="+pv);
	}
}
