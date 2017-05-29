 package com.cnv.cms.controller;

 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.cnv.cms.CmsSpringbootApplication;
import com.cnv.cms.service.ArticleService;

 @RunWith(SpringRunner.class)
 @SpringBootTest(classes={CmsSpringbootApplication.class},webEnvironment=SpringBootTest.WebEnvironment.NONE)
public class PageAndMapperTest2 {

	//@Autowired
	//private TestRestTemplate restTemplate;
	@Autowired
	private ArticleService articleService;


    
	@Test
	public void exampleTest() {
		System.out.println("----------test1----------");
		long t0 = System.currentTimeMillis();
		int n=1;
		for(int i=0; i<n; i++){
			//String body = this.restTemplate.getForObject("/test.html", String.class);
		}
		long t1 = System.currentTimeMillis();
		
		for(int i=0; i<n; i++){
			articleService.selectById(14);
		}
		long t2 = System.currentTimeMillis();
		System.out.println("t1:" + (t1-t0));
		System.out.println("t2:" + (t2-t1));
		
		
		//System.out.println(body);
		//assertThat(body).isEqualTo("Hello World");
	}
	
    @Test
    public void testExample() throws Exception {

    	System.out.println("----------test2----------");
    	
        //this.mvc.perform(get("/test.html").accept(MediaType.TEXT_PLAIN));

    }

}