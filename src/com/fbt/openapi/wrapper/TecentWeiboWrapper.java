package com.fbt.openapi.wrapper;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.weibo.UserInfo;
import com.qq.connect.api.weibo.Weibo;
import com.qq.connect.javabeans.weibo.UserInfoBean;
import com.qq.connect.javabeans.weibo.WeiboBean;

public class TecentWeiboWrapper {
	/**
	 * 
	 * @param accessToken
	 * @param openID
	 * @return
	 * @throws QQConnectException
	 */
	public UserInfoBean getUserInfoFromTecentWeibo(String accessToken, String openID) throws QQConnectException {
		UserInfo userInfo = new UserInfo(accessToken, openID);
		return userInfo.getUserInfo();
	}
	
	/**
	 * 
	 * @param accessToken
	 * @param openID
	 * @param newWeibo
	 * @return
	 * @throws QQConnectException
	 */
	public WeiboBean addWeibo(String accessToken, String openID, String newWeibo) throws QQConnectException {
		Weibo weibo = new Weibo(accessToken, openID);
		return weibo.addWeibo(newWeibo);
	}
}
