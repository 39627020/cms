package com.cnv.cms.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cnv.cms.interceptor.HostHolderInterceptor;
import com.cnv.cms.model.HostHolder;
import com.cnv.cms.service.ChannelService;

@Controller
//@RequestMapping("/")
public class SaticResourcesController {
	
	private final Logger logger = LoggerFactory.getLogger(SaticResourcesController.class);
	
	
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private ChannelService channelService;
	

	@RequestMapping(value="/user/{file}.html",method=RequestMethod.GET)
	public String userInterceptro(@PathVariable("file") String file){
		return "user/"+file;
		
	}
	@RequestMapping(value="/user/home.html",method=RequestMethod.GET)
	public String userhome(Model model,HttpServletRequest request){
		model.addAllAttributes(this.getCommontInfo(request));
		return "user/home";
	}
	
	@RequestMapping(value="/user/article_add.html",method=RequestMethod.GET)
	public String userArticlAdd(Model model,HttpServletRequest request){
		model.addAllAttributes(this.getCommontInfo(request));
		return "user/article_add";
	}
	
	
	
	
	@RequestMapping(value="/admin/{file}.html",method=RequestMethod.GET)
	public String adminInterceptro(@PathVariable("file") String file){
		return "admin/"+file;
		
	}
    @RequestMapping("/")
    public String index() {
        return "forward:/index.html";
    }
    @RequestMapping("/test")
    public String test(Model model,HttpServletRequest request){
/*    	String p1 = request.getContextPath();
    	System.out.println(p1);
    	
    	this.setChannelAndUser(model);*/
    	
    	model.addAllAttributes(this.getCommontInfo(request));
    	
    	model.addAttribute("test", "test str");
    	return "/test";
    }
    @RequestMapping("/a")
    public String testa(Model model){
    	model.addAttribute("test", "test str");
    	return "/a";
    }
    
    public void setChannelAndUser(Model model){
    	logger.info("hostHolder username: "+hostHolder.getUserName());
    	logger.info("hostHolder userid: "+hostHolder.getUserId());
    	model.addAttribute("user", hostHolder.getUserName());
    	model.addAttribute("userid", hostHolder.getUserId());
    	model.addAttribute("channels", channelService.selectAll());
    }
    public Map<String, Object> getCommontInfo(HttpServletRequest request){
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("user", hostHolder.getUserName());
    	map.put("userid", hostHolder.getUserId());
    	map.put("channels", channelService.selectAll());
    	map.put("contextPath", request.getContextPath());
    	return map;
    }
}
