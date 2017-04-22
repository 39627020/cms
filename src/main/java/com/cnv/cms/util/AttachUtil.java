package com.cnv.cms.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnv.cms.mapper.UserGroupMapper;
import com.cnv.cms.model.UserGroup;
import com.cnv.cms.service.AttachmentService;
import com.cnv.cms.service.GroupService;
/*
 * 临时附件的操作
 */
@Component("attachUtil")  
public class AttachUtil implements InitializingBean, DisposableBean{
	private final Logger logger = LoggerFactory.getLogger(AttachUtil.class);
	@Autowired
	private AttachmentService attachService;
	
	@Autowired
	private GroupService groupService;
	@Autowired
	private UserGroupMapper userGroupMapper;
	
	private final ConcurrentMap<String,List<Integer>> tempAttachs = new ConcurrentHashMap<String,List<Integer>>();
	private static ScheduledThreadPoolExecutor exec = null;
	public AttachUtil(){

	}

	
	public void addTempAttachs(String clientid, int id){
		List<Integer> tempAtsList = tempAttachs.get(clientid);
		if(tempAtsList==null){
			synchronized (tempAttachs) {
				tempAtsList = tempAttachs.get(clientid);
				if(tempAtsList==null){
					tempAtsList = new CopyOnWriteArrayList<Integer>();
					tempAttachs.put(clientid, tempAtsList);
				}
				
			}
		}
		tempAttachs.get(clientid).add(id);
		
	}
	public List<Integer> getTempAttachs(String client){
		return tempAttachs.get(client);
	}
	public void removeTempAttachs(String client){
		synchronized (tempAttachs) {
			tempAttachs.remove(client);
		}
	}
	public  void stop(){
		if(exec!=null){
			exec.shutdown();
		}
		logger.info("附件清理线程停止");
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		Thread t =new Thread(new Runnable(){

			@Override
			public void run() {
				Calendar   c   =   Calendar.getInstance();  
				//得到2小时前的时间
				c.add(Calendar.HOUR_OF_DAY, -2); 
				Date date = c.getTime();
				attachService.deletetUnused(date);
				logger.info("附件清理线程执行");
				
			}
			
		});
		t.setDaemon(true);
		//t.start();
		exec = new ScheduledThreadPoolExecutor(1);
		//线程6个小时执行一次
		//exec.schedule(t, 20, TimeUnit.SECONDS);
		exec.scheduleAtFixedRate(t,2, 2*60, TimeUnit.MINUTES);
		logger.info("附件清理线程启动");

	}
	@Override
	public void destroy() throws Exception {
		this.stop();
	}
	
	public void testTrsaction(){
		
		//ApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
		Thread task =new Thread(new Runnable(){
			@Override
			public void run() {
				
				//UserService gs = context.getBean(UserService.class);	
				
				System.out.println("测试：------- 事务测试-------------");
				UserGroup ug = new UserGroup();
				int id=122;
				ug.setId(id);
				ug.setG_id(id);
				ug.setU_id(id);
				
				try{
					userGroupMapper.delete(ug.getId());
					userGroupMapper.add(ug);
					System.out.println("执行前:"+userGroupMapper.selectByUID(id));
					groupService.deleteUserGroup(id);
					//gs.delete(id);
				}catch(RuntimeException e){
					System.out.println(e.getMessage());
				}
				
				System.out.println("执行后:"+userGroupMapper.selectByUID(id));
			}
			
		});
		exec.schedule(task, 5, TimeUnit.SECONDS);  
	}	
}
