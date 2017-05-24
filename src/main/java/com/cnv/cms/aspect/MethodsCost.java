package com.cnv.cms.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventProducer;
import com.cnv.cms.event.EventType;
import com.cnv.cms.model.HostHolder;
import com.cnv.cms.util.RedisKeyUtil;

@Component
@Aspect
public class MethodsCost implements InitializingBean {
	@Autowired
	private EventProducer eventProducer;
	@Autowired
	private HostHolder hostHolder;
	@Autowired  
	private RedisTemplate redisTemplate; 
	private ValueOperations<String,String> valOps=null;
	
	@Pointcut("execution(* com.cnv.cms.controller.SaticResourcesController.*(..))")
	public void controllerMethod(){}
	@Pointcut("execution(* com.cnv.cms.service.*.*(..))")
	public void serviceMethod(){}
	@Pointcut("execution(* com.cnv.cms.mapper.*.*(..))")
	public void mapperMethod(){}
	@Pointcut("within(com.cnv.cms.controller.api.*)")
	public void apicontrollerMethod(){}
	
	//@Around("controllerMethod() || serviceMethod() || mapperMethod()")
	@Around("controllerMethod() || serviceMethod() || mapperMethod()")
	public Object methodCost(ProceedingJoinPoint jp){
		try {
			String url = hostHolder.getUrl();
			if (url==null)
				return jp.proceed(jp.getArgs());
			long tin = System.currentTimeMillis();
			Signature sig = jp.getSignature();
			Object retVal = jp.proceed(jp.getArgs());
			
			String cname = sig.getDeclaringTypeName();
			String method = cname.substring(cname.lastIndexOf('.')+1, cname.length())
							+"."+sig.getName();

			long tcost = System.currentTimeMillis() - tin;
			eventProducer.addEvent(new EventModel(EventType.TIME_COUNT,-1)
					.addExtData("url", url)
					.addExtData("cost", tcost)
					.addExtData("method", method));

			/*String key = RedisKeyUtil.getTimeCostKey(url,method);
			valOps.increment(key, tcost);
			valOps.increment(key+":pv",1);*/
			
			return retVal;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		valOps = redisTemplate.opsForValue();		
	}
}
