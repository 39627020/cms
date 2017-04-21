package com.cnv.cms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnv.cms.authority.AuthClass;
import com.cnv.cms.authority.AuthMethod;
import com.cnv.cms.config.CmsConfig;
import com.cnv.cms.exception.CmsException;
import com.cnv.cms.mapper.LoginSessionMapper;
import com.cnv.cms.model.LoginSession;
import com.cnv.cms.model.Role;
import com.cnv.cms.model.RoleType;
import com.cnv.cms.model.User;
import com.cnv.cms.service.UserService;
import com.cnv.cms.util.LoginSessionUtil;

@AuthClass
@Controller
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	 @Qualifier("userServiceImpl")
	private UserService userService;
	
	@Autowired
	private  LoginSessionMapper loginMapper;
	
	@AuthMethod(role="customer")
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public  @ResponseBody Map<String, Object>  loginIn(@RequestBody User  userForm,
			HttpServletRequest request,HttpServletResponse response){
		if(CmsConfig.isDebug){
			System.out.println("received userform:");
			System.out.println(userForm);
		}
		HttpSession httpSession = request.getSession();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try{
			User user = userService.login(userForm.getUsername(), userForm.getPassword());
			//是否是管理员
			boolean isAdmin = this.isAdmin(user.getId());
/*			
			httpSession.setAttribute("loginUser", user); 
			httpSession.setAttribute("isAdmin", isAdmin);
			
			//该用户所有可以访问的方法
			Set<String> allActions = new HashSet<String>();
			Map<String,Set<String>> auths = (Map<String, Set<String>>) httpSession.getServletContext().getAttribute("allAuths");
			//contomer方法和base方法都可以访问
			Set<String> customAuths = auths.get("customer");
			allActions.addAll(customAuths);
			Set<String> baseAuths = auths.get("base");
			allActions.addAll(baseAuths);
			
			//查询每个角色对应的权限
			for(int rid : user.getRoleIDs()){
				//数据库中role id 从1开始
				String rname =RoleType.values()[rid-1].toString();
				Set<String> roleAuths = auths.get(rname);
				if(roleAuths != null){
					allActions.addAll(roleAuths);	
				}
			}*/
			//把该用户能访问的权限放到session中
			//httpSession.setAttribute("allActions", allActions);
			
			String sessionid = LoginSessionUtil.getSessionId(request);
			LoginSession loginSession = loginMapper.selsetBySession(sessionid);
			if(loginSession==null || loginSession.getExpired().before(new Date())) {
				userService.addLoginSession(sessionid, user.getId());
			}
			
			
			//设置cookie记录登录信息
			Cookie[] cookies = new Cookie[3];
			
			cookies[0] = new Cookie("LOGINSESSIONID", sessionid);
			cookies[1] = new Cookie("loginUser", user.getUsername());
			cookies[2] = new Cookie("isAdmin", Boolean.toString(isAdmin));
			for(Cookie ck : cookies){
				ck.setPath("/");
				ck.setMaxAge(3600);
				response.addCookie(ck);
			}
			
			//返回的数据
			map.put("login", "success");
			
			
			if(CmsConfig.isDebug){
				//System.out.println(user.getUsername()+" 's authority:");
				//System.out.println(allActions);
			}
		}catch(CmsException ce){
			map.put("login", "failure");
			map.put("error", ce.getMessage());
			System.out.println("cms error:"+ce.getMessage());
			
		}			
		return map;
	}
	
	@AuthMethod(role="customer")
	@RequestMapping(value="/login.check",method=RequestMethod.GET)
	public  @ResponseBody Map<String, Object>  loginCheck(HttpServletRequest request,HttpServletResponse response){
		System.out.println("login get");
		Map<String, Object> map = new HashMap<String, Object>();
		
		LoginSession loginSession = LoginSessionUtil.getLoginSession(request);
		
		
		if(loginSession==null){
			//删除cookie
			LoginSessionUtil.removeLoginCookies(request, response);
			map.put("flag", "failure");
		}else{
			if(CmsConfig.isDebug){
				System.out.println("login user sessionid : "+loginSession.getSessionid());
			}
			//重新获取用户身份，并写入session
			boolean isAdmin = loginSession.getAdmin();
			//重新读取，而不是直接用session里是数据，防止数据库被修改
			User loginUser = userService.selectById(loginSession.getUserid());
			//返回的数据
			map.put("flag", "success");
			map.put("loginUser", loginUser.getUsername());
			map.put("isAdmin", isAdmin);
		}	
		
		return map;
	}
	@AuthMethod(role="base")
	@RequestMapping(value="/selfinfo",method=RequestMethod.GET)
	public  @ResponseBody Map<String, Object>  selfInfo(HttpSession httpSession){
		
	
		Map<String, Object> map = new HashMap<String, Object>();
		
		User loginUser = (User) httpSession.getAttribute("loginUser");
		
		if(CmsConfig.isDebug){
			System.out.println("---------self info  query---------");
			System.out.println("user :" + loginUser.getId());
		}
		if(loginUser == null){
			map.put("data", "not login");
		}else{
			//重新读取，防止数据库被修改
			loginUser = userService.selectById(loginUser.getId());
			map.put("data", loginUser);
			map.put("flag", "success");
		}	
		
		return map;
	}	
	
	@AuthMethod()
	@RequestMapping(value="/login.out",method=RequestMethod.GET)
	public   @ResponseBody Map<String, Object>   loginOut(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", "false");
/*		HttpSession httpSession = request.getSession();
		httpSession.removeAttribute("loginUser");
		httpSession.removeAttribute("isAdmin");
		httpSession.removeAttribute("allActions");*/
		String sessionid = LoginSessionUtil.getSessionId(request);
		loginMapper.deleteBySession(sessionid);
		//删除cookie
		LoginSessionUtil.removeLoginCookies(request, response);
		
		if(CmsConfig.isDebug){
			System.out.println("login out");
		}
		map.put("flag", "success");
		return map;
	}
	
	
	private boolean isRole(List<Role> rs,RoleType rt) {
		for(Role r:rs) {
			if(r.getRoleType()==rt) return true;
		}
		return false;
	}
	private boolean isAdmin(int id) {
		List<Role> roles =  userService.listUserRoles(id);
		//是否是管理员
		boolean isAdmin = isRole(roles,RoleType.ROLE_ADMIN);
		return isAdmin;
	}
}
