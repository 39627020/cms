package com.cnv.cms.model;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by nowcoder on 2016/7/24.
 */
public class Feed {
    private int id;
    private int userId;
    private int type;
    private String content;
    private Date createDate;
    private int status=1;
    private JSONObject dataJSON = null;

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

    public void setContent(String content) {
        this.content = content;
        dataJSON = JSONObject.parseObject(content);
    }
    
    public String get(String key) {
        return dataJSON == null ? null : dataJSON.getString(key);
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
}
