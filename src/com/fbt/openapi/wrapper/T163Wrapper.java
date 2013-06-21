package com.fbt.openapi.wrapper;

import t4j.TBlog;
import t4j.TBlogException;
import t4j.data.Status;
import t4j.data.User;

public class T163Wrapper {
	/**
	 * 
	 * @param accessToken
	 * @param tokenSecret
	 * @return
	 * @throws TBlogException
	 */
	public User getUserInfo (String accessToken, String tokenSecret) throws TBlogException {
		TBlog tBlog = new TBlog();
		tBlog.setToken(accessToken, tokenSecret);
		return tBlog.verifyCredentials();
	}
	
	/**
	 * 
	 * @param newStatus
	 * @param accessToken
	 * @param tokenSecret
	 * @return
	 * @throws TBlogException
	 */
	public Status updateStatus(String newStatus, String accessToken, String tokenSecret) throws TBlogException {
		TBlog tBlog = new TBlog();
		tBlog.setToken(accessToken, tokenSecret);
		return tBlog.updateStatus(newStatus);
	}
}
