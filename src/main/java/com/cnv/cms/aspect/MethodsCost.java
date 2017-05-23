package com.cnv.cms.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventProducer;
import com.cnv.cms.event.EventType;
import com.cnv.cms.model.HostHolder;

@Component
@Aspect
public class MethodsCost {
	@Autowired
	private EventProducer eventProducer;
	@Autowired
	private HostHolder hostHolder;
	
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
			long tin = System.currentTimeMillis();
			Signature sig = jp.getSignature();
			Object retVal = jp.proceed(jp.getArgs());
			long tcost = System.currentTimeMillis() - tin;
			String cname = sig.getDeclaringTypeName();
			String method = cname.substring(cname.lastIndexOf('.')+1, cname.length())
							+"."+sig.getName();
			//System.out.println("目标签名："+method);
			//System.out.println("cost："+tcost);
			eventProducer.addEvent(new EventModel(EventType.TIME_COUNT,-1)
					.addExtData("url", hostHolder.getUrl())
					.addExtData("cost", tcost)
					.addExtData("method", method));
			return retVal;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
