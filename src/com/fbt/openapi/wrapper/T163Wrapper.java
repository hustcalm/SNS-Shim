package com.fbt.openapi.wrapper;

import t4j.OAuthVersion;
import t4j.TBlog;
import t4j.TBlogException;
import t4j.data.Status;
import t4j.data.User;
import t4j.http.OAuth2AccessToken;

public class T163Wrapper {
	
	public static final String CONSUMER_KEY = "yourConsumerKey";
	public static final String CONSUMER_SECRET = "yourConsumerSecret";
	public static final String REDIRECT_URL = "http://hustcalm.me";
	
	TBlog tblog = new TBlog(OAuthVersion.V2);
	
	/**
	 * 
	 * @return
	 * @throws TBlogException
	 */
	public String getUserAhthorizeURL() throws TBlogException {
		return tblog.getOAuth2AuthorizeURL();
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 * @throws TBlogException
	 */
	public OAuth2AccessToken getUserOAuth2AccessTokenByCode(String code) throws TBlogException {
		return tblog.getOAuth2AccessTokenByCode(code);
	}
	
	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws TBlogException
	 */
	public User getUserInfo (String accessToken) throws TBlogException {
		tblog.setOAuth2AccessToken(accessToken);
		return tblog.verifyCredentials();
	}
	
	/**
	 * 
	 * @param newStatus
	 * @param accessToken
	 * @return
	 * @throws TBlogException
	 */
	public Status updateStatus(String accessToken, String newStatus) throws TBlogException {
		tblog.setOAuth2AccessToken(accessToken);
		return tblog.updateStatus(newStatus);
	}
}
