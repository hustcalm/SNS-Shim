package com.fbt.openapi.wrapper;

import org.json.simple.JSONArray;

import com.renren.api.client.RenrenApiClient;
import com.renren.api.client.param.impl.AccessToken;

public class RenrenWrapper {
	
	RenrenApiClient renrenApiClient = RenrenApiClient.getInstance();
	
	public static final String RENREN_ADDRESS = "https://graph.renren.com/oauth/authorize";
	static final String renrenApiUrl = "http://api.renren.com/restserver.do";
	static final String renrenApiVersion = "1.0";
	static final String renrenApiKey = "yourApiKey";
	static final String renrenApiSecret = "yourApiSecret";
	static final String renrenAppID = "yourAppID";
	
	/**
	 * 
	 * @return
	 */
	public String getUserAuthorizeURL() {
		// https://graph.renren.com/oauth/authorize? client_id=...& 
		// redirect_uri=http://graph.renren.com/oauth/login_success.html& 
		// response_type=token& display=popup
		StringBuffer sb = new StringBuffer(RENREN_ADDRESS);
		sb.append("?client_id=")
		.append(renrenAppID)
		.append("&redirect_uri=http://graph.renren.com/oauth/login_success.html")
		.append("&response_type=token")
		.append("&display=popup");
		return sb.toString();
	}
	 
	
	public String getUserAuthorizeURL(String scopes) {
		// https://graph.renren.com/oauth/authorize? client_id=...& 
		// redirect_uri=http://graph.renren.com/oauth/login_success.html& 
		// response_type=token& display=popup
		StringBuffer sb = new StringBuffer(RENREN_ADDRESS);
		sb.append("?client_id=")
		.append(renrenAppID)
		.append("&redirect_uri=http://graph.renren.com/oauth/login_success.html")
		.append("&response_type=token")
		.append("&display=popup")
		.append("&scope=")
		.append(scopes);
		return sb.toString();
	}
	
	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	public String getUserID(String accessToken) {
		long userID = renrenApiClient.getUserService().getLoggedInUser(new AccessToken(accessToken));
		return String.valueOf(userID);
	}
	
	/**
	 * 
	 * @param userID
	 * @param accessToken
	 * @return
	 */
	public JSONArray getUserInfo(String userID, String accessToken){
		return renrenApiClient.getUserService().getInfo(userID, new AccessToken(accessToken));
	}
	
	/**
	 * 
	 * @param userID
	 * @param fields
	 * @param accessToken
	 * @return
	 */
	public JSONArray getUserInfo(String userID, String fields, String accessToken){
		return renrenApiClient.getUserService().getInfo(userID, fields, new AccessToken(accessToken));
	}
	
	/**
	 * 
	 * @param accessToken
	 * @param newStatus
	 * @return
	 */
	public boolean setStatus(String accessToken, String newStatus) {
		if(1 == renrenApiClient.getStatusService().setStatus(newStatus, new AccessToken(accessToken))) {
			return true;
		} else {
			return false;
		}
	}
}
