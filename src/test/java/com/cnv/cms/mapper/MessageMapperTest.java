package com.cnv.cms.mapper;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cnv.cms.CmsSpringbootApplication;
import com.cnv.cms.model.Comment;
import com.cnv.cms.model.Message;
import com.cnv.cms.model.type.EntityType;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={CmsSpringbootApplication.class},webEnvironment=SpringBootTest.WebEnvironment.NONE)
public class MessageMapperTest {

    @Autowired  
    private MessageMapper messageMapper;  

	
	@Test
	public void test1() throws Exception{
		System.out.println("----MessageMapper Test-----");

		Message m = new Message();
		m.setFromId(3);
		m.setToId(1);
		m.setConversationId("1_4");
		m.setContent("hh");
		
		messageMapper.add(m);
		
		Message c1= messageMapper.getById(2);
		System.out.println("get: "+c1.getContent());
		List<Message> cs = messageMapper.listConversationsByUserId(26);
		System.out.println("get 0: "+cs.get(0));
		System.out.println("get 1: "+cs.get(1));
		
		int count = messageMapper.getUnReadCount();
		System.out.println("un read: "+count);
		//redisTemplate.delete("k1");
	}
	

}
