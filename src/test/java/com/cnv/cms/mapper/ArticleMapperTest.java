package com.cnv.cms.mapper;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cnv.cms.CmsSpringbootApplication;
import com.cnv.cms.model.Article;
import com.cnv.cms.model.Comment;
import com.cnv.cms.model.type.EntityType;
import com.cnv.cms.service.ArticleService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={CmsSpringbootApplication.class},webEnvironment=SpringBootTest.WebEnvironment.NONE)
public class ArticleMapperTest {

    @Autowired
    private ArticleMapper articleMapper;

	
	@Test
	public void test1() throws Exception{
		System.out.println("----ArticleMapperTest Test-----");

		Article a = articleMapper.selectById(14);
		System.out.println("get: "+a.getTitle());
		//redisTemplate.delete("k1");
	}
	

}
