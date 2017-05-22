package com.cnv.cms.event.handler;

import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventType;

public interface EventHandler {
	public void handle(EventModel event);
	public EventType[] getSupposortEventTypes();
}
