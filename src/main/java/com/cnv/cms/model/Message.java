package com.cnv.cms.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by nowcoder on 2016/7/24.
 */
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private Date createdDate = new Date();
    private int status=1;
    private int hasRead;
    private int count;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getFromId() {
		return fromId;
	}

	public void setFromId(int fromId) {
		this.fromId = fromId;
	}

	public int getToId() {
		return toId;
	}

	public void setToId(int toId) {
		this.toId = toId;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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

	public int getHasRead() {
		return hasRead;
	}

	public void setHasRead(int hasRead) {
		this.hasRead = hasRead;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", fromId=" + fromId + ", toId=" + toId + ", conversationId=" + conversationId
				+ ", content=" + content + ", createdDate=" + createdDate + ", status=" + status + ", hasRead="
				+ hasRead + ", count=" + count + "]";
	}

    
    
}
