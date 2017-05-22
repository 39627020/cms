package com.cnv.cms.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnv.cms.event.EventModel;
import com.cnv.cms.event.EventType;
import com.cnv.cms.service.PVService;

@Component
public class PVEventHandler implements EventHandler {
	private Logger logger = LoggerFactory.getLogger(PVEventHandler.class);
	@Autowired
	private PVService pvService;
	
	@Override
	public void handle(EventModel event) {
		
		String page  = (String) event.getExtData("page");
		int id = page.equals("index")? -1 : (int) event.getExtData("id");
		pvService.addPVCount(page, id);
		logger.info("PVEventHandler do handle :" + page);
	}

	@Override
	public EventType[] getSupposortEventTypes() {
		return new EventType[]{EventType.PV_COUNT};
	}


}
