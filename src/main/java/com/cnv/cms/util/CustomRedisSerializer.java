package com.cnv.cms.util;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class CustomRedisSerializer implements RedisSerializer<Object>{

	RedisSerializer stringSerializer = new StringRedisSerializer();
	RedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
	
	
	@Override
	public byte[] serialize(Object t) throws SerializationException {
		// TODO Auto-generated method stub
		//System.out.println("序列化:"+t);
		if(t instanceof String){
			//System.out.println("string 序列化");
			return stringSerializer.serialize(t);
		}
			
		//System.out.println("Jdk 序列化");
		return jdkSerializer.serialize(t);
	}
	
	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		Object obj = null;
		try{
			
			obj = jdkSerializer.deserialize(bytes);
			//System.out.println("Jdk 反序列化");
		}catch(SerializationException e){
			//System.out.println("string 反序列化");
			obj = stringSerializer.deserialize(bytes);
			
		}
		return obj;
	}

}
