package com.fbt.openapi.demos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import weibo4j.util.BareBonesBrowserLaunch;

import com.fbt.openapi.utils.Log;
import com.fbt.openapi.wrapper.RenrenWrapper;
import com.renren.api.client.utils.JsonUtils;

public class RenrenOAuth2Demo {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// 授权流程参考http://wiki.dev.renren.com/wiki/%E6%A1%8C%E9%9D%A2%E5%AE%A2%E6%88%B7%E7%AB%AF%E6%8E%A5%E5%85%A5
		RenrenWrapper renrenWrapper = new RenrenWrapper();
		
		// 生成授权页面的URL
		System.out.println("以下是人人网的用户授权URL:");
		//String userAuthorizeURL = renrenWrapper.getUserAuthorizeURL();
		// 用scopes指定需要获取的用户权限，参考http://wiki.dev.renren.com/wiki/Authentication
		// 权限列表，参考http://wiki.dev.renren.com/wiki/%E6%9D%83%E9%99%90%E5%88%97%E8%A1%A8
		String scopes = "status_update";
		String userAuthorizeURL = renrenWrapper.getUserAuthorizeURL(scopes);
		System.out.println(userAuthorizeURL);
		
	    // 调用本地浏览器访问授权页面
		BareBonesBrowserLaunch.openURL(userAuthorizeURL);
		
		// 本Demo需要用户输入得到的AccessToken，在正确授权后浏览器返回的URL中
		// 注意正式开发的时候，解析出AccessToken后需要进行urldecode
		// 比如，返回的AccessToken为
		// 231253%7C6.9971d9e24fc190d3506ee10d94f27d61.2592000.1376586000-253332769
		// 经过urldecode后为
		// 231253|6.9971d9e24fc190d3506ee10d94f27d61.2592000.1376586000-253332769
		System.out.print("请输入urldecode后的AccessToken值，然后敲回车.[Enter]:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String userAccessToken = br.readLine();
		Log.logInfo("userAccessToken:" + userAccessToken);
		
		String  userID = renrenWrapper.getUserID(userAccessToken);
		System.out.println("UserID: " + userID);
		
		// 以下测试得到用户基本信息以及发布新状态的接口
		JSONArray users = renrenWrapper.getUserInfo(userID, userAccessToken);
        JSONObject u = JsonUtils.getIndexJSONObject(users, 0);
        String userName = JsonUtils.getValue(u, "name", String.class);
		System.out.println("userName: " + userName);
		
		// 测试更新状态
		//System.out.print("请输入所要发的状态的文本内容:[Enter]:");
		//br = new BufferedReader(new InputStreamReader(System.in));
		//String newStatus = br.readLine();
		//renrenWrapper.setStatus(userAccessToken, newStatus);
		
		renrenWrapper.setStatus(userAccessToken, "这是一条测试信息！");
		
		System.out.print("The End!");
	}

}
