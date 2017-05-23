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

import com.cnv.cms.config.CmsConfig;
import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventProducer;
import com.cnv.cms.event.EventType;
import com.cnv.cms.interceptor.HostHolderInterceptor;
import com.cnv.cms.model.HostHolder;
import com.cnv.cms.service.ArticleService;
import com.cnv.cms.service.ChannelService;
import com.cnv.cms.service.PVService;

@Controller
//@RequestMapping("/")
public class SaticResourcesController {
	
	private final Logger logger = LoggerFactory.getLogger(SaticResourcesController.class);
	
	
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private ArticleService articleService;
	
	
	
	@Autowired
	private EventProducer eventProducer;

	
	
	
	
	//---------------------/user/*.html-------------------------------------------------
	
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
		model.addAttribute("topChannels", channelService.selectTopChannels());
		return "user/article_add";
	}
	@RequestMapping(value="/user/mynews.html",method=RequestMethod.GET)
	public String usernews(Model model,HttpServletRequest request){
		model.addAllAttributes(this.getCommontInfo(request));
		model.addAttribute("articles", articleService.selectByUserId(hostHolder.getUserId()));
		return "user/mynews";
	}
	
	//-----------------------------------------------------------------
	
	@RequestMapping(value="/admin/{file}.html",method=RequestMethod.GET)
	public String adminInterceptro(@PathVariable("file") String file){
		return "admin/"+file;
		
	}
	
	
	//------------------------/*.html---------------------------------------
    @RequestMapping(path={"/","index","index.html"})
    public String index(Model model,HttpServletRequest request) {
    	//统计耗费时间
    	long tin = System.currentTimeMillis();
    	
    	model.addAllAttributes(this.getCommontInfo(request));
    	model.addAttribute("articles", articleService.selectTopRead(15));
    	eventProducer.addEvent(getEvent("index",-1));
    	
    	long tcost = System.currentTimeMillis() - tin;
		eventProducer.addEvent(new EventModel(EventType.TIME_COUNT,-1)
				.addExtData("url", hostHolder.getUrl())
				.addExtData("cost", tcost));
        return "/index";
    }
    @RequestMapping(value="/article/{id}",method=RequestMethod.GET)
    public String article(Model model,HttpServletRequest request, @PathVariable int id){    	
    	model.addAllAttributes(this.getCommontInfo(request));
    	model.addAttribute("aid", id);
    	//model.addAttribute("article", articleService.selectById(id));
    	model.addAttribute("imgPath", "http://"+CmsConfig.getFtpServer()+"/"+CmsConfig.getFilePath());
    	
    	eventProducer.addEvent(getEvent("article",id));
    	logger.debug("访问Article : "+id);
    	
    	return "/article";
    }
    @RequestMapping("/article_list/{id}")
    public String articlelist(Model model,HttpServletRequest request, @PathVariable int id){    	
    	model.addAllAttributes(this.getCommontInfo(request));
    	model.addAttribute("articles", articleService.selectByChannel(id));
    	model.addAttribute("channelname", channelService.selectById(id).getName());
    	
    	eventProducer.addEvent(getEvent("article_list",id));
    	
    	return "/article_list";
    }
    @RequestMapping("/login.html")
    public String login(Model model,HttpServletRequest request){    	
    	model.addAllAttributes(this.getCommontInfo(request));
    	
    	return "/login";
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

    //*********************************************************************
    public void setChannelAndUser(Model model){
    	logger.info("hostHolder username: "+hostHolder.getUserName());
    	logger.info("hostHolder userid: "+hostHolder.getUserId());
    	model.addAttribute("user", hostHolder.getUserName());
    	model.addAttribute("userid", hostHolder.getUserId());
    	model.addAttribute("channels", channelService.selectAll());
    }
    public  Map<String, Object> getCommontInfo(HttpServletRequest request){
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("user", hostHolder.getUserName());
    	map.put("userid", hostHolder.getUserId());
    	map.put("channels", channelService.selectAll());
    	map.put("contextPath", request.getContextPath());
    	return map;
    }
    public EventModel getEvent(String page, int id){
    	return new EventModel()
    			.setEventType(EventType.PV_COUNT)
    			.addExtData("page", page)
    			.addExtData("id", id);
    }
}
