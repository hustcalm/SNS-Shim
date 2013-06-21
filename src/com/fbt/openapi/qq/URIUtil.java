/*
Copyright (c) 2012, Weikexin eeplat.com
Released under the MIT license.
http://www.opensource.org/licenses/mit-license.php
 */
package com.fbt.openapi.qq;

import com.fbt.openapi.callback.GlobalConfig;

public class URIUtil {

	public static final String QQ_BASE = "https://graph.qq.com/oauth2.0/";

	public static final String QQ_AUTH = "https://graph.qq.com/oauth2.0/authorize";

	public static final String QQ_TOKEN = "https://graph.qq.com/oauth2.0/token";

	public static final String QQ_GET_USER_INFO = "https://graph.qq.com/user/get_user_info";
	


	/**
	 * QQ's redirectUrl is combination of response_type,display,client_id and
	 * redirect_uri.
	 * 
	 * @param redirectUri
	 * @return wrapped uri
	 */
	public static String wrappRedirectUri() {

		// https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=[YOUR_APPID]&redirect_uri=[YOUR_REDIRECT_URI]&scope=[THE_SCOPE]

		StringBuffer sb = new StringBuffer(QQ_AUTH);
		sb.append("?response_type=code&display=page&client_id=")
				.append(GlobalConfig.getValue("qq.appid"))
				.append("&redirect_uri=")
				.append(GlobalConfig.getValue("qq.cb"));
		return sb.toString();
	}
	
	/**
	 * 
	 * @param appID
	 * @param redirectURL
	 * @return
	 */
	public static String wrappRedirectUri(String appID, String redirectURL) {

		// https://graph.qq.com/oauth2.0/authorize?response_type=token&client_id=[YOUR_APPID]&redirect_uri=[YOUR_REDIRECT_URI]&scope=[THE_SCOPE]

		StringBuffer sb = new StringBuffer(QQ_AUTH);
		sb.append("?response_type=token&display=page&client_id=")
				.append(appID)
				.append("&redirect_uri=")
				.append(redirectURL);
		return sb.toString();
	}

	/**
	 * 
	 * @param appID
	 * @param redirectURL
	 * @param scope
	 * @return
	 */
	public static String wrappRedirectUri(String appID, String redirectURL, String scope) {

		// https://graph.qq.com/oauth2.0/authorize?response_type=token&client_id=[YOUR_APPID]&redirect_uri=[YOUR_REDIRECT_URI]&scope=[THE_SCOPE]

		StringBuffer sb = new StringBuffer(QQ_AUTH);
		sb.append("?response_type=token&display=page&client_id=")
				.append(appID)
				.append("&redirect_uri=")
				.append(redirectURL)
				.append("&scope=")
				.append(scope);
		return sb.toString();
	}
	
	/**
	 * .发送请求到如下地址（请将参数值替换为你自己的，参数解释详见这里）：
	 * https://graph.qq.com/oauth2.0/token?grant_type
	 * =authorization_code&client_id
	 * =[YOUR_APP_ID]&client_secret=[YOUR_APP_Key]&code
	 * =[The_AUTHORIZATION_CODE]&
	 * state=[The_CLIENT_STATE]&redirect_uri=[YOUR_REDIRECT_URI]
	 * 
	 * @param authCode
	 * @return
	 */

	public static String wrappAuthCodeUri(String authCode) {

		StringBuffer sb = new StringBuffer(QQ_TOKEN);
		sb.append("?grant_type=authorization_code&client_id=")
				.append(GlobalConfig.getValue("qq.appid"))
				.append("&client_secret=")
				.append(GlobalConfig.getValue("qq.appkey")).append("&code=")
				.append(authCode).append("&state=").append("qq")
				.append("&redirect_uri=")
				.append(GlobalConfig.getValue("qq.cb"));
		return sb.toString();
	}

	/**
	 * 发送请求到如下地址（请将access_token等参数值替换为你自己的）：
	 * https://graph.qq.com/oauth2.0/me?access_token=YOUR_ACCESS_TOKEN
	 * 
	 * @param accessToken
	 * @return
	 */

	public static String wrappAccessTokenUri(String accessToken) {

		StringBuffer sb = new StringBuffer(QQ_BASE);
		sb.append("me?access_token=").append(accessToken);
		return sb.toString();
	}

	/**
	 * 发送请求到get_user_info的URL（请将access_token，appid等参数值替换为你自己的）：
	 * https://graph.qq.
	 * com/user/get_user_info?access_token=YOUR_ACCESS_TOKEN&oauth_consumer_key
	 * =YOUR_APP_ID&openid=YOUR_OPENID
	 * 
	 * @param accessToken
	 * @return
	 */

	public static String wrappGetUserInfoUri(String accessToken, String openid) {

		StringBuffer sb = new StringBuffer(QQ_GET_USER_INFO);
		sb.append("?access_token=").append(accessToken)
				.append("&oauth_consumer_key=")
				.append(GlobalConfig.getValue("qq.appid")).append("&openid=")
				.append(openid);
		return sb.toString();
	}

}
