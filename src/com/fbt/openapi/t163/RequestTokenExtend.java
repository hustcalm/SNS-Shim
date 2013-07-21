/*
Copyright (c) 2012, Weikexin eeplat.com
Released under the MIT license.
http://www.opensource.org/licenses/mit-license.php
 */
package com.fbt.openapi.t163;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import t4j.TBlogException;
import t4j.http.RequestToken;

/**
 * Extend t4j.http.RequestToken to add the callback uri
 * @author Weikexin
 * @author CALM
 *
 */
public class RequestTokenExtend  {
	
	private RequestToken  requestToken ;

	public  RequestTokenExtend(RequestToken  rt)
			throws TBlogException {
		this.requestToken = rt;
	
	}
	
	/**
	 * 
	 * @param callBackUrl
	 * @return
	 */
    public String getAuthenticationURL(String callBackUrl) {
        try {
			return  requestToken.getAuthenticationURL() + "&oauth_callback=" + URLEncoder.encode(callBackUrl,"utf-8");
		} catch (UnsupportedEncodingException e) {
			return  requestToken.getAuthenticationURL();
		}
    }


}
