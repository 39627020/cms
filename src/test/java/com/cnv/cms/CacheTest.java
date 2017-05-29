package com.cnv.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.cnv.cms.CmsSpringbootApplication;
import com.cnv.cms.event.EventModel;
import com.cnv.cms.model.Channel;
import com.cnv.cms.service.ArticleService;
import com.cnv.cms.service.ChannelService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={CmsSpringbootApplication.class},webEnvironment=SpringBootTest.WebEnvironment.NONE)
public class CacheTest {

    @Autowired  
    private RedisTemplate redisTemplate;  
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ArticleService articleService;
	
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
	public void testCache() throws Exception{
		System.out.println("----Redis Template Test-----");
		redisTemplate.getConnectionFactory().getConnection().flushDb();
		ValueOperations<String,Long> valueOps=redisTemplate.opsForValue();
		String key = "pv";
		channelService.selectAll();
		Channel c = channelService.selectById(3);
		channelService.update(c);
		
		
		//articleService.selectTopRead(10);
	}

}
