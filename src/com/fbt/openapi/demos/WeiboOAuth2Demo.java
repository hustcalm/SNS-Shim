package com.fbt.openapi.demos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.fbt.openapi.utils.Log;
import weibo4j.http.AccessToken;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.util.BareBonesBrowserLaunch;

import com.fbt.openapi.wrapper.WeiboWrapper;

public class WeiboOAuth2Demo {

	/**
	 * @param args
	 * @throws WeiboException,IOException 
	 */
	public static void main(String[] args) throws WeiboException,IOException {
		// TODO Auto-generated method stub
		WeiboWrapper  weiboWrapper = new WeiboWrapper();
		System.out.println("以下是新浪微博的用户授权URL:");
		String userAuthorizeURL = weiboWrapper.getUserAuthorizeURL("code", null);
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

		AccessToken accessToken = null;
		try {
			accessToken = weiboWrapper.getUserAccessTokenByCode(code);
		} catch (WeiboException e) {
			if (401 == e.getStatusCode()) {
				Log.logInfo("Unableto get the access token.");
			} else {
				e.printStackTrace();
			}
		}

		String access_token = weiboWrapper.getUserAccessToken(accessToken);
		
		// 关键，设置AccessToken，实质上是传给了HttpClient
		// 封装提供两种类型的接口，一种需要提供AccessToken作为参数，此时不必执行以下语句
		// 若调用不提供AccessToken作为参数的接口，则需要执行
		// 两种接口调用均通过了测试
		// weiboWrapper.setToken(access_token);
		
		
		// 下面进行得到用户信息和发表微博的测试
		String uid = accessToken.getUid();
		//Users um = new Users();
		//um.setToken(access_token);
		try {
			//User user = weiboWrapper.showUserById(uid);
			User user = weiboWrapper.getUserInfo(access_token, uid);
			// 获取当前用户的信息
			System.out.println("当前用户的名字：" + user.getName());
			System.out.println("当前用户所发的微博数：" + user.getStatusesCount());
			System.out.println("当前用户关注数：" + user.getFriendsCount());
			System.out.println("当前用户粉丝数：" + user.getFollowersCount());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
		// 检查一下系统的默认编码，Java读取用户输入的时候需要注意当前编码，向新浪微博post数据都是UTF-8
	    // 尤其是注意BufferedReader和InputStreamReader的编码
		System.out.println("系统默认编码为： " + System.getProperty("file.encoding"));
		 
		// 测试发送微博
		//System.out.print("请输入所要发的微博的文本内容:[Enter]:");
		//br = new BufferedReader(new InputStreamReader(System.in);
		//String newStatus = br.readLine();
		//weiboWrapper.updateStatus(access_token, newStatus);
		
		weiboWrapper.updateStatus(access_token, "这是一条测试信息！");
		System.out.print("The End!");
	}

}
