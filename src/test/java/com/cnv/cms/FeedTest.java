package com.cnv.cms;

import java.util.Date;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={CmsSpringbootApplication.class},webEnvironment=SpringBootTest.WebEnvironment.NONE)
public class FeedTest {

   @Autowired  
   private RedisTemplate redisTemplate;  

	@Test
	public void contextLoads() throws Exception{
		
	}
	
	
	@Test
	public void testValueGet() throws Exception{
		System.out.println("---- Test-----");
		ZSetOperations<String,String> zsetOps=redisTemplate.opsForZSet();
		Date date = new Date();
		String key = "zset1";
		System.out.println("time: "+date.getTime());
		
		zsetOps.add(key, "val2", System.currentTimeMillis());
		zsetOps.add(key, "val1", date.getTime());
		zsetOps.add(key, "val3", 1);
		long d = (long) (double)zsetOps.score(key, "val1");
		
		System.out.println("time get: "+d);
		
		Set<String> vals = zsetOps.range(key, 0, -1);
		
		
		System.out.println(vals);
	}
}
