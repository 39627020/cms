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
import com.cnv.cms.model.type.EntityType;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={CmsSpringbootApplication.class},webEnvironment=SpringBootTest.WebEnvironment.NONE)
public class CommentMapperTest {

    @Autowired  
    private CommentMapper commentMapper;  

	
	@Test
	public void testRedis() throws Exception{
		System.out.println("----CommentMapper Test-----");

		Comment c = new Comment();
		c.setUserId(12);
		c.setCreatedDate(new Date());
		c.setEntityId(123);
		c.setEntityType(EntityType.ENTITY_NEWS);
		c.setContent("haha");
		c.setStatus(1);
		
		commentMapper.add(c);
		
		Comment c1= commentMapper.getById(6);
		List<Comment> cs =commentMapper.listByEntity(123, EntityType.ENTITY_NEWS);
		System.out.println("get: "+cs.get(0).getUserId());
		//redisTemplate.delete("k1");
	}
	

}
