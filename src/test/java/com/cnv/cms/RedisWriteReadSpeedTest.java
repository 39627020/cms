package com.cnv.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventType;
import com.cnv.cms.util.JsonUtil;
import com.cnv.cms.util.RedisKeyUtil;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={CmsSpringbootApplication.class},webEnvironment=SpringBootTest.WebEnvironment.NONE)
public class RedisWriteReadSpeedTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

	@Test
	public void testValueGet() throws Exception{
		System.out.println("----Redis Template Test-----");
		ListOperations listOps=stringRedisTemplate.opsForList();
		ValueOperations valueOps = stringRedisTemplate.opsForValue();
		
		EventModel event = new EventModel(EventType.TIME_COUNT,-1)
		.addExtData("url", "index")
		.addExtData("cost", "5")
		.addExtData("method", "hostholder");
		
		long t10 = System.currentTimeMillis();
		String key  = "testqueue";
		for(int i=0; i<10000; i++){
			listOps.rightPush(key, JsonUtil.toJSon(event));
		}
		long t11 = System.currentTimeMillis() - t10;
		System.out.println("cost: "+t11 +" ms");
		stringRedisTemplate.delete(key);
		long t20 = System.currentTimeMillis();
		String key1 = "test";
		for(int i=0; i<10000; i++){
			valueOps.increment(key1, 1);
		}
		long t21 = System.currentTimeMillis() - t20;
		
		System.out.println("cost: "+t21 +" ms");
		//Object pv =  valueOps.get(key1);
		//System.out.println("test="+pv);
		//stringRedisTemplate.getConnectionFactory().getConnection().flushDb();
	}
}
