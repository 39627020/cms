package com.cnv.cms.service;

import java.util.Set;

public interface FollowService {
	void addFollow(int userId, int followId);

	void removeFollow(int userId, int followId);

	long getFollowNum(int userId);

	boolean isFollowedBy(int followedId, int user);

	Set<String> getFollows(int userId);

	Set<String> getFans(int userId);
}
