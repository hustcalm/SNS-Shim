package com.fbt.openapi.wrapper;

import java.io.IOException;

import com.dongxuexidu.douban4j.model.app.AccessToken;
import com.dongxuexidu.douban4j.model.app.DoubanException;
import com.dongxuexidu.douban4j.model.app.RequestGrantScope;
import com.dongxuexidu.douban4j.model.shuo.DoubanShuoAttachementObj;
import com.dongxuexidu.douban4j.model.user.DoubanUserObj;
import com.dongxuexidu.douban4j.provider.OAuthDoubanProvider;
import com.dongxuexidu.douban4j.service.DoubanShuoService;
import com.dongxuexidu.douban4j.service.DoubanUserService;

public class DoubanWrapper {
	
	OAuthDoubanProvider oauth = new OAuthDoubanProvider();
	
	static final String ApiKey = "yourApiKey";
	static final String ApiSecret = "yourApiSecret";
	static final String redirectURL = "http://hustcalm.me";
	static final String userScopes = "";
	
	/**
	 * 
	 */
	public DoubanWrapper() {
		oauth.setApiKey(ApiKey).setSecretKey(ApiSecret);
		oauth.addScope(RequestGrantScope.BASIC_COMMON_SCOPE)
		.addScope(RequestGrantScope.SHUO_READ_SCOPE)
		.addScope(RequestGrantScope.SHUO_WRITE_SCOPE);
		oauth.setRedirectUrl(redirectURL);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUserAuthorizeURL() {
		return oauth.getGetCodeRedirectUrl();
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 * @throws DoubanException
	 */
	public AccessToken getUserAccessTokenByCode(String code) throws DoubanException {
		return oauth.tradeAccessTokenWithCode(code);
	}
	
	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	public String getUserAccessToken(AccessToken accessToken) {
		return accessToken.getAccessToken();
	}
	
	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	public String getUserID(AccessToken accessToken) {
		return accessToken.getDoubanUserId();
	}
	
	/**
	 * 
	 * @param accessToken
	 * @param uid
	 * @return
	 * @throws DoubanException
	 * @throws IOException
	 */
	public DoubanUserObj getUserInfo(String accessToken, String uid) throws DoubanException, IOException {
		DoubanUserService doubanUserService = new DoubanUserService(accessToken);
		return doubanUserService.getUserProfileByUid(uid);
	}
	
	/**
	 * 
	 * @param newStatus
	 * @param att
	 * @param appKey
	 * @param accessToken
	 * @return
	 * @throws DoubanException
	 * @throws IOException
	 */
	public boolean postNewStatus (String newStatus, DoubanShuoAttachementObj att, String appKey, String accessToken) throws DoubanException, IOException {
		DoubanShuoService doubanShuoService = new DoubanShuoService(accessToken);
		
		if(appKey == null) {
			appKey = ApiKey;
		}
		
		return doubanShuoService.postNewStatus(newStatus, att, appKey, accessToken);
	}
}
