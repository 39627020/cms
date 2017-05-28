package com.cnv.cms.service;

import java.util.List;

import com.cnv.cms.model.Article;
import com.cnv.cms.model.Comment;

public interface MessageService extends BaseService<Comment>{
	List<Comment> listByEntity(int entityId, int entityType);
}
