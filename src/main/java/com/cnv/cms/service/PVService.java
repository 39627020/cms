package com.cnv.cms.service;

import java.util.Map;

public interface PVService {
	/**
	 * @desription 页面PV统计
	 * @param page: 被访问的页面
	 * @param id: 页面id,无id的设为-1；
	 * @return void
	 */
	public void addPVCount(String page, int id);
	/**
	 * @desription 查询页面PV统计
	 * @param page 被访问的页面
	 * @param id 页面id,无id的设为-1；
	 * @return 访问量
	 */
	public long getPVCount(String page, int id);
	Map<String, String> getOverall();
}
