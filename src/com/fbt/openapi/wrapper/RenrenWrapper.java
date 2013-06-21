package com.fbt.openapi.wrapper;

import org.json.simple.JSONArray;

import com.renren.api.client.RenrenApiClient;
import com.renren.api.client.param.impl.AccessToken;

public class RenrenWrapper {
	/**
	 * 
	 * @param userID
	 * @param accessToken
	 * @return
	 */
	public JSONArray getUserInfo(String userID, String accessToken){
		RenrenApiClient renrenApiClient = RenrenApiClient.getInstance();
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
		RenrenApiClient renrenApiClient = RenrenApiClient.getInstance();
		return renrenApiClient.getUserService().getInfo(userID, fields, new AccessToken(accessToken));
	}
	
	/**
	 * 
	 * @param accessToken
	 * @param newStatus
	 * @return
	 */
	public boolean setStatus(String accessToken, String newStatus) {
		RenrenApiClient renrenApiClient = RenrenApiClient.getInstance();
		if(1 == renrenApiClient.getStatusService().setStatus(newStatus, new AccessToken(accessToken))) {
			return true;
		} else {
			return false;
		}
	}
}
