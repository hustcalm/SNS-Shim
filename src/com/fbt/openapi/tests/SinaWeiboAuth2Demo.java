package com.fbt.openapi.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.fbt.openapi.utils.Log;

import weibo4j.Oauth;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.util.BareBonesBrowserLaunch;

public class SinaWeiboAuth2Demo {
	public static void main(String[] args) throws WeiboException, IOException {
		Oauth oauth = new Oauth();
		BareBonesBrowserLaunch.openURL(oauth.authorize("code", null));

		System.out.println(oauth.authorize("code", null));
		System.out.print("Hitenter when it's done.[Enter]:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();
		Log.logInfo("code:" + code);

		AccessToken accessToken = null;
		try {
			accessToken = oauth.getAccessTokenByCode(code);
		} catch (WeiboException e) {
			if (401 == e.getStatusCode()) {
				Log.logInfo("Unableto get the access token.");
			} else {
				e.printStackTrace();
			}
		}

		String access_token = accessToken.getAccessToken();
		String uid = accessToken.getUid();
		Users um = new Users();
		um.setToken(access_token);
		try {
			User user = um.showUserById(uid);
			// 获取当前用户的信息
			System.out.println("当前用户的名字：" + user.getName());
			System.out.println("当前用户所发的微博数：" + user.getStatusesCount());
			System.out.println("当前用户关注数：" + user.getFriendsCount());
			System.out.println("当前用户粉丝数：" + user.getFollowersCount());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
		// 测试发送微博
		Timeline tm = new Timeline();
		tm.setToken(access_token);
		System.out.print("请输入所要发的微博的文本内容:[Enter]:");
		br = new BufferedReader(new InputStreamReader(System.in));
		String newStatus = br.readLine();
		tm.UpdateStatus(newStatus);
	}

}
