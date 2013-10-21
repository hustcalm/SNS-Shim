package com.fbt.openapi.wrapper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import weibo4j.Weibo;
import weibo4j.http.AccessToken;
import weibo4j.model.PostParameter;
import weibo4j.model.Status;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class WeiboWrapper extends Weibo {
	
	private static final long serialVersionUID = -3170640319660127937L;
	
	private String client_ID = "yourClientID";
	private String client_SERCRET = "yourClientSecret";
	static final String redirect_URI = "http://hustcalm.me";
	static final String baseURL = "https://api.weibo.com/2/";
	static final String accessTokenURL = "https://api.weibo.com/oauth2/access_token";
	static final String authorizeURL = "https://api.weibo.com/oauth2/authorize";
	static final String rmURL = "https://rm.api.weibo.com/2/";
	
	/**
	 * 
	 * @param clientID
	 */
	public void setClientID(String clientID) {
		this.client_ID = clientID;
	}
	
	/**
	 * 
	 * @param clientSecret
	 */
	public void setClientSecret(String clientSecret) {
		this.client_SERCRET = clientSecret;
	}
	
	
	/**
	 * 
	 * @param response_type
	 * @param state
	 * @return
	 * @throws WeiboException
	 */
	public String getUserAuthorizeURL(String response_type,String state) throws WeiboException {
		return authorizeURL + "?client_id="
				+ client_ID + "&redirect_uri="
				+ redirect_URI
				+ "&response_type=" + response_type
				+ "&state="+state;
	}
	
	/**
	 * 
	 * @param redirectURL
	 * @throws WeiboException
	 */
	public String getUserAuthorizedCode(String redirectURL) throws WeiboException {
		return redirectURL.substring(redirectURL.lastIndexOf("code=") + 5);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 * @throws WeiboException
	 */
	public AccessToken getUserAccessTokenByCode(String code) throws WeiboException {
		return new AccessToken(client.post(
				accessTokenURL,
				new PostParameter[] {
						new PostParameter("client_id", client_ID),
						new PostParameter("client_secret", client_SERCRET),
						new PostParameter("grant_type", "authorization_code"),
						new PostParameter("code", code),
						new PostParameter("redirect_uri", redirect_URI) }, false));
	}
	
	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws WeiboException
	 */
	public String getUserAccessToken(AccessToken accessToken) throws WeiboException {
		return accessToken.getAccessToken();
	}
	
	/**
	 * 
	 * @param uid
	 * @return
	 * @throws WeiboException
	 */
	public User showUserById(String uid) throws WeiboException {
		return new User(client.get(
				baseURL + "users/show.json",
				new PostParameter[] { new PostParameter("uid", uid) })
				.asJSONObject());
	}
	
	/**
	 * 
	 * @param accessToken
	 * @param uid
	 * @return
	 * @throws WeiboException
	 */
	public User getUserInfo(String accessToken, String uid) throws WeiboException {
		this.setToken(accessToken);
		return showUserById(uid);
	}
	
	/**
	 * 
	 * @param status
	 * @return
	 * @throws WeiboException
	 * @throws UnsupportedEncodingException 
	 */
	public Status updateUserStatus(String userStatus) throws WeiboException, UnsupportedEncodingException {
		return new Status(client.post(baseURL
				+ "statuses/update.json",
				new PostParameter[] { new PostParameter("status", userStatus) }));
	}
	
	/**
	 * 
	 * @param accessToken
	 * @param newStatus
	 * @return
	 * @throws WeiboException
	 * @throws UnsupportedEncodingException 
	 */
	public Status updateStatus(String accessToken, String newStatus) throws WeiboException, UnsupportedEncodingException {
		this.setToken(accessToken);
		return updateUserStatus(newStatus);
	}
	
	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws WeiboException
	 * @throws JSONException 
	 */
	public boolean validateAccessToken(String accessToken) throws WeiboException, JSONException {
		// Refer http://open.weibo.com/wiki/Oauth2/get_token_info
		
		Response requestRes = client.post(
				getTokenInfoURL, 
				new PostParameter[] {
						new PostParameter("access_token",accessToken) 
				});
		JSONObject retJSON = requestRes.asJSONObject();
		String expire_in_seconds = retJSON.getString("expire_in");
		if(Integer.parseInt(expire_in_seconds) <= 0)
			return false;
		else
			return true;
		
	}
	
	/**
	 * 
	 * @param uid 需要查询的用户UID
	 * @return
	 * @throws WeiboException
	 */
	public UserWapper getFollowersById(String accessToken, String uid) throws WeiboException {
		this.setToken(accessToken);
		return User.constructWapperUsers(client.get(
				baseURL + "friendships/followers.json",
				new PostParameter[] { new PostParameter("uid", uid) }));
	}
	
	/**
	 * 
	 * @param uid 需要查询的用户UID
	 * @param count 单页返回的记录条数，默认为50，最大不超过200。
	 * @param cursor 返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
	 * @return
	 * @throws WeiboException
	 */
	public UserWapper getFollowersById(String accessToken, String uid, Integer count, Integer cursor)
			throws WeiboException {
		this.setToken(accessToken);
		return User.constructWapperUsers(client.get(
				baseURL + "friendships/followers.json",
				new PostParameter[] { new PostParameter("uid", uid),
						new PostParameter("count", count.toString()),
						new PostParameter("cursor", cursor.toString()) }));
	}
	
	/**
	 * 
	 * @param uid 需要查询的用户UID
	 * @return
	 * @throws WeiboException
	 */
	public String[] getFollowersIdsById(String accessToken, String uid) throws WeiboException {
		this.setToken(accessToken);
		return User.constructIds(client.get(
				baseURL + "friendships/followers/ids.json",
				new PostParameter[] { new PostParameter("uid", uid) }));
	}
	
	/**
	 * 
	 * @param uid 需要查询的用户UID
	 * @param count 单页返回的记录条数，默认为50，最大不超过200。
	 * @param cursor 返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
	 * @return
	 * @throws WeiboException
	 */
	public String[] getFollowersIdsById(String accessToken, String uid, Integer count,
			Integer cursor) throws WeiboException {
		this.setToken(accessToken);
		return User.constructIds(client.get(
				baseURL + "friendships/followers/ids.json",
				new PostParameter[] { new PostParameter("uid", uid),
						new PostParameter("count", count.toString()),
						new PostParameter("cursor", cursor.toString()) }));
	}
}
