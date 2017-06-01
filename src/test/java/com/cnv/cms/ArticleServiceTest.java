package com.cnv.cms;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cnv.cms.CmsSpringbootApplication;
import com.cnv.cms.model.Article;
import com.cnv.cms.service.ArticleService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={CmsSpringbootApplication.class},webEnvironment=SpringBootTest.WebEnvironment.MOCK)
public class ArticleServiceTest {


    @Autowired
    private ArticleService articleService;
	
	@Test
	public void test1() throws Exception{
		System.out.println("----Timeline pull sql Test-----");
		Set<String> userIds = new HashSet<>();
		userIds.add("26");
		userIds.add("42");
		
		List<Article> list1 = articleService.selectByChannel(14);
		System.out.println(list1);
		
		List<Article> list = articleService.selectFromUserList(userIds, 0, 10);
		
		System.out.println(list);
	}
	


}
