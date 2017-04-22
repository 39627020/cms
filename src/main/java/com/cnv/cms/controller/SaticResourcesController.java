package com.cnv.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    @RequestMapping("/")
    public String index() {
        return "forward:/index.html";
    }
}
