package com.fbt.openapi.wrapper;

import org.json.JSONException;
import org.json.JSONObject;

import com.fbt.openapi.http.HttpClient;
import com.fbt.openapi.http.OpenApiException;
import com.fbt.openapi.http.Response;

public class QQWrapper {

	static final String QQ_AuthorizeURL = "https://graph.qq.com/oauth2.0/authorize";
	static final String AppID = "yourAppID";
	static final String AppKey = "yourAppKey";
	static final String userScopes = "get_user_info,add_topic,add_one_blog,add_album,upload_pic,list_album,add_share,check_page_fans,add_t,add_pic_t,del_t,get_repost_list,get_info,get_other_info,get_fanslist,get_idollist,add_idol,del_ido,get_tenpay_addr";
	static final String redirectURL = "http://hustcalm.me";
	static final String openIDURL = "https://graph.qq.com/oauth2.0/me";
	static final String baseURL = "https://graph.qq.com/";
	static final String getUserInfoURL = "https://graph.qq.com/user/get_user_info";
	static final String accessTokenURL = " https://graph.qq.com/oauth2.0/token";
	static final String authorizeURL = "https://graph.qq.com/oauth2.0/authorize";
	static final String getOpenIDURL = "https://graph.qq.com/oauth2.0/me";
	static final String addTopicURL = "https://graph.qq.com/shuoshuo/add_topic";
	static final String addBlogURL = "https://graph.qq.com/blog/add_one_blog";
	static final String addAlbumURL = "https://graph.qq.com/photo/add_album";
	static final String uploadPicURL = "https://graph.qq.com/photo/upload_pic";
	static final String listAlbumURL = "https://graph.qq.com/photo/list_album";
	static final String addShareURL = "https://graph.qq.com/share/add_share";
	static final String checkPageFansURL = "https://graph.qq.com/user/check_page_fans";
	static final String addTURL = "https://graph.qq.com/t/add_t";
	static final String addPicTURL = "https://graph.qq.com/t/add_pic_t";
	static final String delTURL = "https://graph.qq.com/t/del_t";
	static final String getWeiboUserInfoURL = "https://graph.qq.com/user/get_info";
	static final String getWeiboOtherUserInfoURL = "https://graph.qq.com/user/get_other_info";
	static final String getFansListURL = "https://graph.qq.com/relation/get_fanslist";
	static final String getIdolsListURL = "https://graph.qq.com/relation/get_idollist";
	static final String addIdolURL = "https://graph.qq.com/relation/add_idol";
	static final String delIdolURL = "https://graph.qq.com/relation/del_idol";
	static final String getTenpayAddrURL = "https://graph.qq.com/cft_info/get_tenpay_addr";
	static final String getRepostListURL = "https://graph.qq.com/t/get_repost_list";

	/**
	 * For the Authorize URL, please refer to
	 * http://wiki.connect.qq.com/%e4%bd%bf
	 * %e7%94%a8implicit_grant%e6%96%b9%e5%bc%8f%e8%8e%b7%e5%8f%96access_token
	 * 
	 * @return
	 */
	public String getUserAuthorizeURL() {
		// https://graph.qq.com/oauth2.0/authorize?response_type=token
		// &client_id=[YOUR_APPID]
		// &redirect_uri=[YOUR_REDIRECT_URI]
		// &scope=[THE_SCOPE]
		return QQ_AuthorizeURL + "?response_type=" + "token" + "&client_id="
		+ AppID + "&redirect_uri=" 
		+ redirectURL + "&scope="
		+ userScopes;
	}

	/**
	 * For scopes, please refer to
	 * http://wiki.connect.qq.com/api%e5%88%97%e8%a1%a8
	 * 
	 * @param scopes
	 * @return
	 */
	public String getUserAuthorizeURL(String scopes) {
		// https://graph.qq.com/oauth2.0/authorize?response_type=token
		// &client_id=[YOUR_APPID]
		// &redirect_uri=[YOUR_REDIRECT_URI]
		// &scope=[THE_SCOPE]
		return QQ_AuthorizeURL + "?response_type=" + "token" + "&client_id="
				+ AppID + "&redirect_uri=" + redirectURL + "&scope=" + scopes;
	}

	/**
	 * 
	 * @param userAccessToken
	 * @return
	 */
	public String getUserOpenIDURL(String userAccessToken) {
		return openIDURL + "?access_token=" + userAccessToken;
	}

	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws OpenApiException
	 */
	public String getUserOpenID(String accessToken) throws OpenApiException {

		String getOpenIdUri = getUserOpenIDURL(accessToken);

		HttpClient http = new HttpClient();
		Response res = http.getMethod(getOpenIdUri);
		String ret = res.asString();
		System.out.println("Response is: " + ret);
		// callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
		String retJsonStr = ret.substring(10, ret.length() - 2);
		try {
			JSONObject retJson = new JSONObject(retJsonStr);
			return retJson.getString("openid");
		} catch (JSONException e) {

		}
		return ret.substring(ret.indexOf("openid") + 9, ret.length() - 5);
	}

	/**
	 * 
	 * @param accessToken
	 * @param openID
	 * @return
	 */
	public String getUserInfoURL(String accessToken, String openID) {
		return getUserInfoURL + "?access_token=" + accessToken
				+ "&oauth_consumer_key=" + AppID + "&openid=" + openID;
	}
	
	/**
	 * 
	 * @param accessToken
	 * @param openID
	 * @return
	 * @throws OpenApiException
	 */
	public JSONObject getUserInfo(String accessToken, String openID)
			throws OpenApiException {
		String getUserInfoUri = getUserInfoURL(accessToken, openID);
		System.out.println("getUserInfoURL is: " + getUserInfoUri);
		
		HttpClient http = new HttpClient();
		Response res = http.getMethod(getUserInfoUri);
		return res.asJSONObject();
	}
}
