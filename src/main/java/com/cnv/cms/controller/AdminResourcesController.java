package com.cnv.cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cnv.cms.model.HostHolder;
import com.cnv.cms.service.PVService;

@Controller
@RequestMapping("/admin")
public class AdminResourcesController {
	
	private final Logger logger = LoggerFactory.getLogger(AdminResourcesController.class);
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private PVService pvService;
	
	@RequestMapping(value="/statistic.html",method=RequestMethod.GET)
	public String adminPV(Model model,HttpServletRequest request){
		model.addAllAttributes(this.getCommontInfo(request));
		model.addAttribute("pvdata", pvService.getOverall());
		/*List<String[]> list1 = new ArrayList<>();
		list1.add(new String[]{"1","3"});
		list1.add(new String[]{"2","4"});
		model.addAttribute("list1", list1);*/
		return "admin/statistic";
	}
    public  Map<String, Object> getCommontInfo(HttpServletRequest request){
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("user", hostHolder.getUserName());
    	map.put("userid", hostHolder.getUserId());
    	//map.put("channels", channelService.selectAll());
    	map.put("contextPath", request.getContextPath());
    	return map;
    }
}
