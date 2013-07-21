/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
 * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.fbt.openapi.http;


import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.AccessControlException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


import com.fbt.openapi.callback.GlobalConfig;
import com.fbt.openapi.qq.URIUtil;

/**
 * A utility class to handle HTTP request/response.
 */
public class HttpClient implements java.io.Serializable {

	private static final long serialVersionUID = 1819132301156935237L;

	private static final int OK = 200; // OK: Success!
	private static final int NOT_MODIFIED = 304; // Not Modified: There was no
													// new data to return.
	private static final int BAD_REQUEST = 400; // Bad Request: The request was
												// invalid. An accompanying
												// error message will explain
												// why. This is the status code
												// will be returned during rate
												// limiting.
	private static final int NOT_AUTHORIZED = 401; // Not Authorized:
													// Authentication
													// credentials were missing
													// or incorrect.
	private static final int FORBIDDEN = 403; // Forbidden: The request is
												// understood, but it has been
												// refused. An accompanying
												// error message will explain
												// why.
	private static final int NOT_FOUND = 404; // Not Found: The URI requested is
												// invalid or the resource
												// requested, such as a user,
												// does not exists.
	private static final int NOT_ACCEPTABLE = 406; // Not Acceptable: Returned
													// by the Search API when an
													// invalid format is
													// specified in the request.
	private static final int INTERNAL_SERVER_ERROR = 500;// Internal Server
															// Error: Something
															// is broken. Please
															// post to the group
															// so the Weibo team
															// can investigate.
	private static final int BAD_GATEWAY = 502;// Bad Gateway: Weibo is down or
												// being upgraded.
	private static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable:
														// The Weibo servers are
														// up, but overloaded
														// with requests. Try
														// again later. The
														// search and trend
														// methods use this to
														// indicate when you are
														// being rate limited.

	private static boolean isJDK14orEarlier = false;

	private int connectionTimeout = GlobalConfig.getQQConnectionTimeout();
	private int readTimeout = GlobalConfig.getQQReadTimeout();

	static {
		try {
			String versionStr = System
					.getProperty("java.specification.version");
			if (null != versionStr) {
				isJDK14orEarlier = 1.5d > Double.parseDouble(versionStr);
			}
		} catch (AccessControlException ace) {
			isJDK14orEarlier = true;
		}
	}

	public HttpClient() {

	}
	
	public Response getMethod(String url) throws OpenApiException{
		return httpRequest(url,null,"GET");
	}
	
	
	public Response postMethod(String url,Map<String,String> postParams) throws OpenApiException{
		return httpRequest(url,postParams,"POST");
	}

	public Response httpRequest(String url, Map<String,String> postParams, String httpMethod)
			throws OpenApiException {

		Response res = null;

		int responseCode = -1;
		try {
			HttpURLConnection con = null;
			OutputStream osw = null;
			try {
				con = getConnection(url);
				con.setDoInput(true);

				if (null != postParams || "POST".equals(httpMethod)) {
					con.setRequestMethod("POST");
					con.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded");
					con.setDoOutput(true);
					String postParam = "";
					if (postParams != null) {
						postParam = encodeParameters(postParams);
					}

					byte[] bytes = postParam.getBytes("UTF-8");

					con.setRequestProperty("Content-Length",
							Integer.toString(bytes.length));

					osw = con.getOutputStream();
					osw.write(bytes);
					osw.flush();
					osw.close();
				} else if ("DELETE".equals(httpMethod)) {
					con.setRequestMethod("DELETE");
				} else {
					con.setRequestMethod("GET");
				}

				res = new Response(con);
				responseCode = con.getResponseCode();

				if (responseCode != OK) {
					if (responseCode < INTERNAL_SERVER_ERROR) {
						throw new OpenApiException(getCause(responseCode),
								responseCode);
					}
					// will retry if the status code is INTERNAL_SERVER_ERROR
				}
			} finally {
				try {
					osw.close();
				} catch (Exception ignore) {
				}
			}
		} catch (IOException ioe) {

			throw new OpenApiException(ioe.getMessage(), ioe, responseCode);

		}

		return res;
	}

	private HttpURLConnection getConnection(String url) throws IOException {

		HttpURLConnection con = (HttpURLConnection) new URL(url)
				.openConnection();

		if (connectionTimeout > 0 && !isJDK14orEarlier) {
			con.setConnectTimeout(connectionTimeout);
		}
		if (readTimeout > 0 && !isJDK14orEarlier) {
			con.setReadTimeout(readTimeout);
		}
		return con;
	}

	public static String encodeParameters(Map<String, String> postParams) {
		StringBuffer buf = new StringBuffer();

		for (Iterator<Entry<String, String>> itE = postParams.entrySet()
				.iterator(); itE.hasNext();) {
			Entry<String, String> e = itE.next();
			if (itE.hasNext()) {
				buf.append("&");
			}
			try {
				buf.append(URLEncoder.encode(e.getKey(), "UTF-8")).append("=")
						.append(URLEncoder.encode(e.getValue(), "UTF-8"));
			} catch (java.io.UnsupportedEncodingException neverHappen) {
			}
		}
		return buf.toString();

	}



	private static String getCause(int statusCode) {
		String cause = null;
		switch (statusCode) {
		case NOT_MODIFIED:
			break;
		case BAD_REQUEST:
			cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
			break;
		case NOT_AUTHORIZED:
			cause = "Authentication credentials were missing or incorrect.";
			break;
		case FORBIDDEN:
			cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
			break;
		case NOT_FOUND:
			cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
			break;
		case NOT_ACCEPTABLE:
			cause = "Returned by the Search API when an invalid format is specified in the request.";
			break;
		case INTERNAL_SERVER_ERROR:
			cause = "Something is broken.  Please post to the group so the Weibo team can investigate.";
			break;
		case BAD_GATEWAY:
			cause = "Weibo is down or being upgraded.";
			break;
		case SERVICE_UNAVAILABLE:
			cause = "Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
			break;
		default:
			cause = "";
		}
		return statusCode + ":" + cause;
	}
	
	public static void main(String[] args) throws OpenApiException{
		
	    HttpClient http = new HttpClient();
	  //  Response res = http.getMethod(URIUtil.wrappRedirectUrl());
	    System.out.println("Response as String::" + URIUtil.wrappRedirectUri());
	    
		
		
	}

}
