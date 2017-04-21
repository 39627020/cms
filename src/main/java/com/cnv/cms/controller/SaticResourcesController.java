package com.cnv.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cnv.cms.config.CmsConfig;
import com.cnv.cms.model.Group;

@Controller
@RequestMapping("/")
public class SaticResourcesController {

	@RequestMapping(value="/user/{file}.html",method=RequestMethod.GET)
	public String userInterceptro(@PathVariable("file") String file){
		return "user/"+file;
		
	}
	@RequestMapping(value="/admin/{file}.html",method=RequestMethod.GET)
	public String adminInterceptro(@PathVariable("file") String file){
		return "admin/"+file;
		
	}
}
