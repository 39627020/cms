package com.cnv.cms.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnv.cms.config.CmsConfig;
import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventProducer;
import com.cnv.cms.event.EventType;
import com.cnv.cms.model.HostHolder;
import com.cnv.cms.service.ArticleService;
import com.cnv.cms.service.ChannelService;
import com.cnv.cms.service.UserService;

@Controller
//@RequestMapping("/")
public class IndexResourcesController {
	
	private final Logger logger = LoggerFactory.getLogger(IndexResourcesController.class);
	
	
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private EventProducer eventProducer;

	
	
	
	//------------------------/*.html---------------------------------------
    @RequestMapping(path={"/"})
    public String indexDefalut() {
        return "redirect:/index.html";
    }
    @RequestMapping(path={"index","index.html"})
    public String index(Model model,HttpServletRequest request,@RequestParam(defaultValue="1") int page) {
    	//System.out.println("page:"+page);
    	model.addAllAttributes(this.getCommontInfo(request));
    	model.addAttribute("articles", articleService.selectTopRead(15));
    	model.addAttribute("pageid", page);
    	eventProducer.addEvent(getEvent("index",-1));
    	

        return "/index";
    }
    @RequestMapping(path={"mysubscribe.html"})
    public String mySubscribe(Model model,HttpServletRequest request,@RequestParam(defaultValue="1") int page) {
    	int hostId = hostHolder.getUserId();
    	if(hostId<0) 
    		 return "redirect:/index.html";
    	model.addAllAttributes(this.getCommontInfo(request));
    	Set<String> userIds = userService.getFollows(hostId);
    	model.addAttribute("articles", articleService.selectFromUserList(userIds, 0, 15));
    	model.addAttribute("pageid", page);
    	eventProducer.addEvent(getEvent("mysubscribe",-1));
    	
        return "/mysubscribe";
    }
    @RequestMapping(path={"userdetail.html"})
    public String userDetail(Model model,HttpServletRequest request,@RequestParam(defaultValue="0") Integer userid) {
    	if(userid==null || userid==0) return "redirect:/index.html";
    	model.addAllAttributes(this.getCommontInfo(request));
    	model.addAttribute("articles", articleService.selectByUserId(userid));
    	model.addAttribute("fansNum", userService.getFollowNum(userid));
    	model.addAttribute("hasFollow", userService.isFollowedBy(userid, hostHolder.getUserId()));
    	model.addAttribute("userToFollow", userid);
    	//model.addAttribute("pageid", page);
    	eventProducer.addEvent(getEvent("userdetail",-1));
    	
        return "/userdetail";
    }
    @RequestMapping(value="/article.html",method=RequestMethod.GET)
    public String article(Model model,HttpServletRequest request, @RequestParam Integer id){  
    	if(id==null) return "redirect:/index.html";
    	model.addAllAttributes(this.getCommontInfo(request));
    	model.addAttribute("aid", id);
    	model.addAttribute("article", articleService.selectById(id));
    	model.addAttribute("imgPath", "http://"+CmsConfig.getFtpServer()+"/"+CmsConfig.getFilePath());
    	
    	eventProducer.addEvent(getEvent("article",id));
    	logger.debug("访问Article : "+id);
    	
    	return "/article";
    }
    @RequestMapping(path={"/article_list.html"})
    public String articlelist(Model model,HttpServletRequest request,  @RequestParam Integer id,@RequestParam(defaultValue="1") int page){    	
    	if(id==null) return "redirect:/index.html";
    	if (id<1) id=1;
    	model.addAllAttributes(this.getCommontInfo(request));
    	model.addAttribute("articles", articleService.selectPage(page, 10, id));
    	model.addAttribute("channelname", channelService.selectById(id).getName());
    	model.addAttribute("channelid", id);
    	model.addAttribute("pageid", page);
    	eventProducer.addEvent(getEvent("article_list",id));
    	
    	return "/article_list";
    }
    @RequestMapping("/login.html")
    public String login(Model model,HttpServletRequest request){    	
    	model.addAllAttributes(this.getCommontInfo(request));
    	
    	return "/login";
    }

//------------------------------------------------
    @RequestMapping(value="/test.html",method=RequestMethod.GET)
    public String test(Model model,HttpServletRequest request){  

    	model.addAttribute("article", articleService.selectById(14));
    	
    	return "/test";
    }
    //*********************************************************************

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