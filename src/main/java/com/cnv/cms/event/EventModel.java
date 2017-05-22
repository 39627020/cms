package com.cnv.cms.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EventModel implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private EventType eventType;
	private int ownerId;
	private Map<String,Object> extData=null;
	
	
	public EventModel(){}
	public EventModel(EventType eventType, int ownerId) {
		super();
		this.eventType = eventType;
		this.ownerId = ownerId;
	}
	public EventType getEventType() {
		return eventType;
	}
	public EventModel setEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public EventModel setOwnerId(int ownerId) {
		this.ownerId = ownerId;
		return this;
	}
	public Map<String, Object> getExtData() {
		return extData;
	}
	public Object getExtData(String key) {
		if(extData == null)
			return null;
		return extData.get(key);
	}
	public EventModel setExtData(Map<String, Object> extData) {
		this.extData = extData;
		return this;
	}
	public EventModel addExtData(String key, Object value) {
		if(extData==null){
			extData = new HashMap<>();
		}
		extData.put(key, value);
		return this;
	}
	@Override
	public String toString() {
		return "EventModel [eventType=" + eventType + ", ownerId=" + ownerId + ", extData=" + extData + "]";
	}
	
}
