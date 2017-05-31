package com.cnv.cms.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by nowcoder on 2016/7/24.
 */
public class Feed {
    private int id;
    private int userId;
    private int type;
    private String content;
    private Date createdDate;
    private int status;

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
    }
    @JsonFormat(pattern="yyyy-MM-dd HH:MM:SS",timezone = "GMT+8")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
