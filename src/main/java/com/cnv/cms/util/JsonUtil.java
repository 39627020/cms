package com.cnv.cms.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	
	private static  ObjectMapper objectMapper = new ObjectMapper();
	
	public static <T> T readValue(String jsonStr, Class<T> valueType) {
		try {
			return objectMapper.readValue(jsonStr, valueType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String toJSon(Object object) {

		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
