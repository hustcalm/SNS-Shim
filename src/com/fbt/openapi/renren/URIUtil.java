/*
Copyright (c) 2012, Weikexin eeplat.com
Copyleft 2013 CALM FBT.com
Released under the MIT license.
http://www.opensource.org/licenses/mit-license.php
 */
package com.fbt.openapi.renren;

import com.fbt.openapi.callback.GlobalConfig;
import com.renren.api.client.RenrenApiConfig;

/**
 * Wrapper Redirect Url Helper Class for RenRen
 * 
 * @author Weikexin
 * @author CALM
 * 
 */
public class URIUtil {
	public static final String RENREN_ADDRESS = "https://graph.renren.com/oauth/authorize";

	/**
	 * RenRen's redirectUrl is combination of response_type,display,client_id
	 * and redirect_uri.
	 * 
	 * @param redirectUri
	 * @return wrapped uri
	 */
	public static String wrappRedirectUri() {

		StringBuffer sb = new StringBuffer(RENREN_ADDRESS);
		sb.append("?response_type=code&display=page&client_id=")
				.append(GlobalConfig.getValue("renrenAppID")).append("&redirect_uri=")
				.append(GlobalConfig.getCallBack("renren.cb"));
		return sb.toString();
	}
	
	/**
	 * 
	 * @param appID
	 * @return
	 */
	public static String wrappRedirectUri(String appID) {
	// https://graph.renren.com/oauth/authorize? client_id=...& 
	// redirect_uri=http://graph.renren.com/oauth/login_success.html& 
	// response_type=token& display=popup
		StringBuffer sb = new StringBuffer(RENREN_ADDRESS);
		sb.append("?client_id=")
		.append(appID)
		.append("&redirect_uri=http://graph.renren.com/oauth/login_success.html")
		.append("&response_type=token")
		.append("&display=popup");
		return sb.toString();
	}
	

}
