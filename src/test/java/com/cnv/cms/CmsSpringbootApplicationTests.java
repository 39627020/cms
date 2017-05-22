package com.cnv.cms;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cnv.cms.event.EventType;
import com.cnv.cms.service.ArticleService;
import com.cnv.cms.service.ChannelService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={CmsSpringbootApplication.class},webEnvironment=SpringBootTest.WebEnvironment.NONE)
public class CmsSpringbootApplicationTests {

	@Autowired
	private ChannelService channelService;
	@Autowired
	private ArticleService articleService;
	
	@Test
	public void contextLoads() throws Exception{
		System.out.println("测试测试测试测试测试测试测试this is a spring test");
	}
	
	//@Test
	public void testIndex(){
		System.out.println("----test index-------");
		int ntest = 1000;
		long t0 = System.currentTimeMillis();
		for(int i=0; i<ntest; i++){
			articleService.selectTopRead(15);
			channelService.selectAll();
		}
		long t1 = System.currentTimeMillis() - t0;
		
		System.out.println("cost: "+t1+" ms, "+t1/ntest+" ms per query");
	}
	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<>();
		map.put("k1", 1);
		map.put("k2", 2);
		System.out.println(EventType.PV_COUNT);
	}
}
