package com.fbt.openapi.qq;


import org.json.JSONException;
import org.json.JSONObject;

import com.fbt.openapi.http.HttpClient;
import com.fbt.openapi.http.OpenApiException;
import com.fbt.openapi.http.Response;

public class AuthUtil {

	/**
	 * 
	 * 通过Authorization Code获取Access Token
	 * @param oauth_code
	 * @return
	 * @throws OpenApiException
	 */

	
	public static String getAccessToken(String oauth_code) throws OpenApiException{
		
		String authUri = URIUtil.wrappAuthCodeUri(oauth_code);
		
	    HttpClient http = new HttpClient();
	    Response res = http.getMethod(authUri);
	    String ret = res.asString();

	    System.out.println("AccessToken::" + ret);
	    return  ret.substring(13,ret.indexOf("&expires_in="));
	}
	
	/**
	 * 使用Access Token来获取用户的OpenID
	 * @param accessToken
	 * @return
	 * @throws OpenApiException
	 */
	public static String getOpenID(String accessToken) throws OpenApiException{

		String getOpenIdUri = URIUtil.wrappAccessTokenUri(accessToken);
		
	    HttpClient http = new HttpClient();
	    Response res = http.getMethod(getOpenIdUri);
	    String ret = res.asString();
	    //callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} ); 
	    String retJsonStr = ret.substring(10, ret.length()-2);
	    try {
			JSONObject  retJson = new JSONObject(retJsonStr);
			return retJson.getString("openid");
		} catch (JSONException e) {
			
		}
	    return  ret.substring(ret.indexOf("openid")+9, ret.length()-5);
	}
	
	
	/**
	 * 获取到用户数据
	 * @param accessToken
	 * @return
	 * @throws OpenApiException
	 */
	public static JSONObject getUserInfo(String accessToken,String openid) throws OpenApiException{

		String getOpenIdUri = URIUtil.wrappGetUserInfoUri(accessToken, openid);
		
	    HttpClient http = new HttpClient();
	    

	    Response res = http.getMethod(getOpenIdUri);
	    String ret = res.asString();
	    try {
			System.out.println("Return User Info::" + ret);
			return new JSONObject(ret);
		} catch (JSONException e) {
			 e.printStackTrace();
			 String nickName = 
					 ret.substring(ret.indexOf("nickname")+11,ret.indexOf("figureurl")-5);
			 JSONObject  jo = new JSONObject();
			 try {
				jo.put("nickname", nickName);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 return jo;
		}

	}
	
	
		
	public static void  main(String[] args){
		String ret = "access_token=3817413DB01EBC4D516FC8C60522B7D5&expires_in=7776000";
	    System.out.println("RET:::" +  ret.substring(13,ret.indexOf("&expires_in=")) );
	}

}
