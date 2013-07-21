package com.fbt.openapi.demos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.fbt.openapi.utils.Log;
import com.fbt.openapi.wrapper.T163Wrapper;

import t4j.TBlogException;
import t4j.data.User;
import t4j.http.OAuth2AccessToken;
import weibo4j.util.BareBonesBrowserLaunch;

public class T163OAuth2Demo {

	/**
	 * @param args
	 * @throws TBlogException
	 * @throws IOException 
	 */
	public static void main(String[] args) throws TBlogException, IOException {
		// TODO Auto-generated method stub

		// OAuth 2.0版本，默认是OAuth 1，FBT使用2.0接口
		T163Wrapper t163Wrapper = new T163Wrapper();
		String userAhthorizeURL = t163Wrapper.getUserAhthorizeURL();
		System.out.println("用户授权URL：" + userAhthorizeURL);

		// 打开浏览器引导用户授权
		BareBonesBrowserLaunch.openURL(userAhthorizeURL);

		// 根据浏览器的跳转URL解析出用户授权的结果，成功的情况下解析出code，
		// 不正确的情况需要根据返回URL中的status code做相应处理
		// 本Demo需要用户手动输入code
		System.out.print("Hitenter when it's done.[Enter]:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();
		Log.logInfo("code:" + code);
		
		OAuth2AccessToken accessToken = t163Wrapper.getUserOAuth2AccessTokenByCode(code);
		String userAccessToken = accessToken.getAccess_token();
		
		// 拿到用户的AccessToken后进行接口测试
		User userInfo = t163Wrapper.getUserInfo(userAccessToken);
		System.out.println(userInfo.getId());
		System.out.println(userInfo.getName());
		System.out.println(userInfo.getGender());
		System.out.println(userInfo.getScreenName());
		
		t163Wrapper.updateStatus(userAccessToken, "测试T163的OAuth2.0");
		
		System.out.println("Then End!");
	}

}
