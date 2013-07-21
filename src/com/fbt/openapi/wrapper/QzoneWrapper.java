package com.fbt.openapi.wrapper;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.qzone.Topic;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.GeneralResultBean;
import com.qq.connect.javabeans.qzone.UserInfoBean;

public class QzoneWrapper {
	/**
	 * 
	 * @param accessToken
	 * @param openID
	 * @return
	 * @throws QQConnectException
	 */
	public UserInfoBean getUserInfo(String accessToken, String openID) throws QQConnectException {
		UserInfo userInfo = new UserInfo(accessToken, openID);
		return userInfo.getUserInfo();
	}
	
	/**
	 * 	
	 * @param accessToken
	 * @param openID
	 * @param newTopic
	 * @return
	 * @throws QQConnectException
	 */
	public GeneralResultBean addTopic(String accessToken, String openID, String newTopic) throws QQConnectException {
		Topic topic = new Topic(accessToken, openID);
		return topic.addTopic(newTopic);
	}
}
