package com.cnv.cms.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by nowcoder on 2016/7/24.
 */
public class Feed implements Comparable<Feed>{
    private int id;
    private int userId;
    private int type;
    private String content;
    private Date createDate;
    private int status=1;
    //private JSONObject dataJSON = null;
    private Map<String,Object> map = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
        return content;
    }

	public Map<String,Object> getMap() {
        return map;
    }
    @SuppressWarnings("unchecked")
	public void setContent(String content) {
        this.content = content;
        //dataJSON = JSONObject.parseObject(content);
        if(map==null)
        	map = JSONObject.parseObject(content, Map.class);
        else
        	map.putAll(JSONObject.parseObject(content, Map.class));
       
    }
    public void addContent(String key, Object val) {
    	if(map==null)
    		map = new HashMap<>();
    	map.put(key, val);
       
    }
    public Object get(String key) {
        return map == null ? null : map.get(key);
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:MM:SS",timezone = "GMT+8")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    @Override
    public int compareTo(Feed feed){
    	return this.createDate.compareTo(feed.getCreateDate());
    }
}
