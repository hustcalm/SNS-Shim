package com.fbt.openapi.demos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import weibo4j.util.BareBonesBrowserLaunch;

import com.fbt.openapi.http.OpenApiException;
import com.fbt.openapi.utils.Log;
import com.fbt.openapi.wrapper.QQWrapper;
import com.fbt.openapi.wrapper.QzoneWrapper;
import com.fbt.openapi.wrapper.TecentWeiboWrapper;
import com.qq.connect.QQConnectException;

public class QQOAuth2Demo {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws OpenApiException 
	 * @throws JSONException 
	 * @throws QQConnectException 
	 */
	public static void main(String[] args) throws IOException, OpenApiException, JSONException, QQConnectException {
		// TODO Auto-generated method stub
		QQWrapper qqWrapper = new QQWrapper();
		System.out.println("以下是QQ互联的用户授权URL:");
		String userAuthorizeURL = qqWrapper.getUserAuthorizeURL();
		System.out.println(userAuthorizeURL);

		// 打开浏览器引导用户授权
		BareBonesBrowserLaunch.openURL(userAuthorizeURL);

		// 根据浏览器的跳转URL解析出用户授权的结果，成功的情况下解析出AccessToken，
		// 不正确的情况需要根据返回URL中的status code做相应处理
		// 本Demo需要用户手动输入AccessToken
		System.out.print("请输入浏览器返回URL中的AccessToken.Hitenter when it's done.[Enter]:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String userAccessToken = br.readLine();
		Log.logInfo("accessToken:" + userAccessToken);
		
		// 根据拿到的AccessToken获取用户的OpenID
		// 参考链接：http://wiki.connect.qq.com/%e5%bc%80%e5%8f%91%e6%94%bb%e7%95%a5_client-side
		String userOpenID = qqWrapper.getUserOpenID(userAccessToken);
		Log.logInfo("userOpenID:" + userOpenID);
		
		// 得到QQ用户的相关信息需要构造URL后，再发起http请求
		JSONObject qqUserInfo = qqWrapper.getUserInfo(userAccessToken, userOpenID);
		System.out.println("Your QQ user info: " + qqUserInfo.toString());
		
		// Qzone用户信息以及发布状态
		QzoneWrapper qzoneWrapper = new QzoneWrapper();
		 com.qq.connect.javabeans.qzone.UserInfoBean qzoneUserInfo = qzoneWrapper.getUserInfo(userAccessToken, userOpenID);
		System.out.println("Your Qzone user info: " + qzoneUserInfo.toString());
		
		qzoneWrapper.addTopic(userAccessToken, userOpenID, "这是一条测试信息！");
		
		// TecentWeibo用户信息以及发布状态
		TecentWeiboWrapper tecentWeiboWrapper = new TecentWeiboWrapper();
		com.qq.connect.javabeans.weibo.UserInfoBean tecentWeiboUserInfo = tecentWeiboWrapper.getUserInfoFromTecentWeibo(userAccessToken, userOpenID);
		System.out.println("Your Tecentweibo user info: " + tecentWeiboUserInfo.toString());
		
		tecentWeiboWrapper.addWeibo(userAccessToken, userOpenID, "这是一条测试信息！");
		
		System.out.print("The End!");
	}

}
