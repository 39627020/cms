package com.cnv.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cnv.cms.config.CmsConfig;
import com.cnv.cms.model.Group;

@RestController
@RequestMapping("/aaa")
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
	public Group testGroup(){
		Group g = new Group();
		g.setId(11);
		g.setName("testg");
		g.setDescr("desrc");
		return g;
		
	}
}
