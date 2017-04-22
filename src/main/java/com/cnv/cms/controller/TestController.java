package com.cnv.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cnv.cms.config.CmsConfig;
import com.cnv.cms.model.Group;

@Controller
@RequestMapping("/test")
public class TestController {
	@RequestMapping(value="/a",method=RequestMethod.GET)
	public  void test1(Model model){
		System.out.println("controller testing.........");
		System.out.println("user:"+CmsConfig.getFtpUser());
		System.out.println("password:"+CmsConfig.getFtpPassword());
		System.out.println("file path:"+CmsConfig.getFilePath());
		System.out.println("Is Debug:"+CmsConfig.getDebug());
		//return "test";
	}
	@RequestMapping(value="/b",method=RequestMethod.GET)
	public @ResponseBody Group testGroup(){
		Group g = new Group();
		g.setId(11);
		g.setName("testg");
		g.setDescr("desrc");
		return g;
		
	}
	
	@RequestMapping(value="/user/{file}.html",method=RequestMethod.GET)
	public String userInterceptro(@PathVariable("file") String file){
		return "user/"+file;
		
	}
	@RequestMapping(value="/admin/{file}.html",method=RequestMethod.GET)
	public String adminInterceptro(@PathVariable("file") String file){
		return "admin/"+file;
		
	}
}
