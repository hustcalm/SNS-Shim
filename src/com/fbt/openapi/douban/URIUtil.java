package com.fbt.openapi.douban;

import com.fbt.openapi.callback.GlobalConfig;

public class URIUtil {
	// https://www.douban.com/service/auth2/auth?
	// client_id=0b5405e19c58e4cc21fc11a4d50aae64&
	// redirect_uri=https://www.example.com/back&
	// response_type=code&
	// scope=shuo_basic_r,shuo_basic_w,douban_basic_common
	
	public static final String DOUBAN_ADDRESS = "https://www.douban.com/service/auth2/auth";

	/**
	 * 
	 * @return
	 */
	public static String wrappRedirectUri() {

		StringBuffer sb = new StringBuffer(DOUBAN_ADDRESS);
		sb.append("?response_type=code&client_id=")
				.append(GlobalConfig.getValue("douban.Apikey")).append("&redirect_uri=")
				.append(GlobalConfig.getCallBack("douban.cb"));
		return sb.toString();
	}
	
	/**
	 * 
	 * @param appID
	 * @param redirectURL
	 * @return
	 */
	public static String wrappRedirectUri(String appID, String redirectURL) {

		StringBuffer sb = new StringBuffer(DOUBAN_ADDRESS);
		sb.append("?response_type=code&client_id=")
				.append(appID).append("&redirect_uri=")
				.append(redirectURL);
		return sb.toString();
	}
}
