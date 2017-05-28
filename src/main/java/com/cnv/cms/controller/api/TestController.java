package com.cnv.cms.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cnv.cms.authority.AuthClass;
import com.cnv.cms.authority.AuthMethod;
import com.cnv.cms.model.Article;
import com.cnv.cms.service.ArticleService;

@AuthClass
@RestController
@RequestMapping("/api/test")
public class TestController {
    
	@Autowired
	private ArticleService articleService;
	
	
	@AuthMethod(role="customer")
	@RequestMapping(value="/",method=RequestMethod.GET)
	public   @ResponseBody Map<String, Object>  test(){
		Map<String, Object> map = new HashMap<String, Object>();
		Article a = articleService.selectById(14);
		map.put("art", a);
		return map;
	}
}
