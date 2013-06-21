package com.fbt.openapi.wrapper;

import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.Status;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class WeiboWrapper {
	/**
	 * 
	 * @param accessToken
	 * @param uid
	 * @return
	 * @throws WeiboException 
	 */
	public User getUserInfo(String accessToken, String uid) throws WeiboException {
		Users um = new Users();
		um.setToken(accessToken);
		return um.showUserById(uid);
	}
	
	public Status updateStatus(String accessToken, String newStatus) throws WeiboException {
		Timeline tm = new Timeline();
		tm.setToken(accessToken);
		return tm.UpdateStatus(newStatus);
	}
}
