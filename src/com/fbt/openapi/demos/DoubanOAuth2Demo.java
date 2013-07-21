package com.fbt.openapi.demos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import weibo4j.util.BareBonesBrowserLaunch;

import com.dongxuexidu.douban4j.model.app.AccessToken;
import com.dongxuexidu.douban4j.model.app.DoubanException;
import com.fbt.openapi.utils.Log;
import com.fbt.openapi.wrapper.DoubanWrapper;

public class DoubanOAuth2Demo {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws DoubanException 
	 */
	public static void main(String[] args) throws IOException, DoubanException {
		DoubanWrapper doubanWrapper = new DoubanWrapper();
		System.out.println("以下是豆瓣的用户授权URL:");
		String userAuthorizeURL = doubanWrapper.getUserAuthorizeURL();
		System.out.println(userAuthorizeURL);

		// 打开浏览器引导用户授权
		BareBonesBrowserLaunch.openURL(userAuthorizeURL);

		// 根据浏览器的跳转URL解析出用户授权的结果，成功的情况下解析出code，
		// 不正确的情况需要根据返回URL中的status code做相应处理
		// 本Demo需要用户手动输入code
		System.out.print("Hitenter when it's done.[Enter]:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();
		Log.logInfo("code:" + code);
		
		AccessToken accessToken = doubanWrapper.getUserAccessTokenByCode(code);
		String userAccessToken = doubanWrapper.getUserAccessToken(accessToken);
		String userID = doubanWrapper.getUserID(accessToken);
		
		// 测试封装的接口
		System.out.println("User Info is: " + doubanWrapper.getUserInfo(userAccessToken, userID).toString());
		
		doubanWrapper.postNewStatus("这是一条测试信息！", null, null, userAccessToken);
		
		System.out.println("Then End!");
	}

}
