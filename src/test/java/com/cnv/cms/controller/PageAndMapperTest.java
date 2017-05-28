 package com.cnv.cms.controller;

 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.cnv.cms.CmsSpringbootApplication;

 @RunWith(SpringRunner.class)
 @SpringBootTest(classes={CmsSpringbootApplication.class})
public class PageAndMapperTest {



    @Autowired
    private MockMvc mvc;

	
    @Test
    public void testExample() throws Exception {

    	System.out.println("----------test2----------");
    	
        this.mvc.perform(get("/test.html").accept(MediaType.TEXT_PLAIN));

    }

}