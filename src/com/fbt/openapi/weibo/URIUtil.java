package com.fbt.openapi.weibo;

import com.fbt.openapi.callback.GlobalConfig;
import com.renren.api.client.RenrenApiConfig;

public class URIUtil {
	// https://api.weibo.com/oauth2/authorize?client_id=YOUR_APP_KEY&
	// response_type=code&
	// redirect_uri=YOUR_REGISTERED_REDIRECT_URI
	
	public static final String SINA_WEIBO_ADDRESS = "https://api.weibo.com/oauth2/authorize";

	/**
	 * 
	 * @return
	 */
	public static String wrappRedirectUri() {

		StringBuffer sb = new StringBuffer(SINA_WEIBO_ADDRESS);
		sb.append("?response_type=code&client_id=")
				.append(GlobalConfig.getValue("weibo4j.appid")).append("&redirect_uri=")
				.append(GlobalConfig.getCallBack("weibo.cb"));
		return sb.toString();
	}
	
	/**
	 * 
	 * @param appID
	 * @param redirectURL
	 * @return
	 */
	public static String wrappRedirectUri(String appID, String redirectURL) {

		StringBuffer sb = new StringBuffer(SINA_WEIBO_ADDRESS);
		sb.append("?response_type=code&client_id=")
				.append(appID).append("&redirect_uri=")
				.append(redirectURL);
		return sb.toString();
	}
}
