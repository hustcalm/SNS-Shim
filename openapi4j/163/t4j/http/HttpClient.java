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
package t4j.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.protocol.Protocol;

import t4j.Configuration;
import t4j.TBlogException;
import t4j.https.MySSLSocketFactory;

/**
 * A utility class to handle HTTP request/response.
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class HttpClient implements java.io.Serializable {
	
    private static final long serialVersionUID = 808018030183407996L;
	
    private static final int OK = 200;// OK: Success!
    private static final int NOT_MODIFIED = 304;// Not Modified: There was no new data to return.
    private static final int BAD_REQUEST = 400;// Bad Request: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.
    private static final int NOT_AUTHORIZED = 401;// Not Authorized: Authentication credentials were missing or incorrect.
    private static final int FORBIDDEN = 403;// Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
    private static final int NOT_FOUND = 404;// Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
    private static final int NOT_ACCEPTABLE = 406;// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    private static final int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken.  Please post to the group so the team can investigate.
    private static final int BAD_GATEWAY = 502;// Bad Gateway: server is down or being upgraded.
    private static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable: The servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.

    private final static boolean DEBUG = Configuration.getDebug();

    private String basic;
    private int retryCount = Configuration.getRetryCount();
    private int retryIntervalMillis = Configuration.getRetryIntervalSecs() * 1000;
    private String userId = Configuration.getUser();
    private String password = Configuration.getPassword();
    private String proxyHost = Configuration.getProxyHost();
    private int proxyPort = Configuration.getProxyPort();
    private String proxyAuthUser = Configuration.getProxyUser();
    private String proxyAuthPassword = Configuration.getProxyPassword();
    private int connectionTimeout = Configuration.getConnectionTimeout();
    private int readTimeout = Configuration.getReadTimeout();

    private static boolean isJDK14orEarlier = false;
    private Map<String, String> requestHeaders = new HashMap<String, String>();
    
    private OAuth oauth = null;
    
    private String requestTokenURL = "http://api.t.163.com/oauth/request_token";
    private String authenticationURL = "http://api.t.163.com/oauth/authenticate";
    private String accessTokenURL = "http://api.t.163.com/oauth/access_token";
    
    private OAuthToken oauthToken = null;
    
    private boolean isHttps = false;

    public boolean isHttps() {
		return isHttps;
	}

	public void setHttps(boolean isHttps) {
		this.isHttps = isHttps;
	}

	static {
        try {
            String versionStr = System.getProperty("java.specification.version");
            if (null != versionStr) {
                isJDK14orEarlier = 1.5d > Double.parseDouble(versionStr);
            }
        } catch (AccessControlException ace) {
            isJDK14orEarlier = true;
        }
    }

    public HttpClient(String userId, String password) {
        this();
        setUserId(userId);
        setPassword(password);
    }

    public HttpClient() {
        this.basic = null;
        setUserAgent(null);
        setOAuthConsumer(null, null);
        setRequestHeader("Accept-Encoding","gzip");
    }

    public void setUserId(String userId) {
        this.userId = userId;
        encodeBasicAuthenticationString();
    }

    public void setPassword(String password) {
        this.password = password;
        encodeBasicAuthenticationString();
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
    
    public boolean isAuthenticationEnabled(){
        return null != basic || null != oauth;
    }

    /**
     * Sets the consumer key and consumer secret.<br>
     * @param consumerKey Consumer Key
     * @param consumerSecret Consumer Secret
     */
    public void setOAuthConsumer(String consumerKey, String consumerSecret) {
        consumerKey = Configuration.getOAuthConsumerKey(consumerKey);
        consumerSecret = Configuration.getOAuthConsumerSecret(consumerSecret);
        if (null != consumerKey && null != consumerSecret
                && 0 != consumerKey.length() && 0 != consumerSecret.length()) {
            this.oauth = new OAuth(consumerKey, consumerSecret);
        }
    }
    
    public RequestToken setToken(String token, String tokenSecret) {
    	this.oauthToken = new RequestToken(token, tokenSecret);
        return (RequestToken)this.oauthToken;
    }

    /**
     *
     * @return request token
     * @throws TBlogException tw
     */
    public RequestToken getOAuthRequestToken() throws TBlogException {
        this.oauthToken = new RequestToken(httpRequest(requestTokenURL, null, true), this);
        return (RequestToken)this.oauthToken;
    }

    /**
     *
     * @param token request token
     * @return access token
     * @throws TBlogException
     */
    public AccessToken getOAuthAccessToken(RequestToken token) throws TBlogException {
        try {
            this.oauthToken = token;
            this.oauthToken = new AccessToken(httpRequest(accessTokenURL, new PostParameter[0], true));
        } catch (TBlogException te) {
            throw new TBlogException("The user has not given access to the account.", te, te.getStatusCode());
        }
        return (AccessToken) this.oauthToken;
    }
    
    /**
     * 
     * @param passport
     * @param password
     * @return
     * @throws TBlogException
     */
    public AccessToken getXAuthAccessToken(String passport, String password, boolean isMD5) throws TBlogException {
    	
    	String passtype = "1";
    	if(isMD5){
    		passtype = "0";
    	}
    	
    	PostParameter[] params = new PostParameter[]{
    		new PostParameter("x_auth_username", passport),
    		new PostParameter("x_auth_password", password),
    		new PostParameter("x_auth_mode", "client_auth"),
    		new PostParameter("x_auth_passtype", passtype),
    	};
        this.oauthToken = new AccessToken(httpRequest(accessTokenURL, params, true));
        return (AccessToken) this.oauthToken;
    }

    /**
     *
     * @param token request token
     * @return access token
     * @throws TBlogException
     */
    public AccessToken getOAuthAccessToken(RequestToken token, String pin) throws TBlogException {
        try {
            this.oauthToken = token;
            this.oauthToken = new AccessToken(httpRequest(accessTokenURL
                    , new PostParameter[]{new PostParameter("oauth_verifier", pin)}, true));
        } catch (TBlogException te) {
            throw new TBlogException("The user has not given access to the account.", te, te.getStatusCode());
        }
        return (AccessToken) this.oauthToken;
    }

    /**
     *
     * @param token request token
     * @param tokenSecret request token secret
     * @return access token
     * @throws TBlogException
     */
    public AccessToken getOAuthAccessToken(String token, String tokenSecret) throws TBlogException {
        try {
            this.oauthToken = new OAuthToken(token, tokenSecret) {};
            this.oauthToken = new AccessToken(httpRequest(accessTokenURL, new PostParameter[0], true));
        } catch (TBlogException te) {
            throw new TBlogException("The user has not given access to the account.", te, te.getStatusCode());
        }
        return (AccessToken) this.oauthToken;
    }

    /**
     *
     * @param token request token
     * @param tokenSecret request token secret
     * @param oauth_verifier oauth_verifier or pin
     * @return access token
     * @throws TBlogException
     */
    public AccessToken getOAuthAccessToken(String token, String tokenSecret
            , String oauth_verifier) throws TBlogException {
        try {
            this.oauthToken = new OAuthToken(token, tokenSecret) {};
            this.oauthToken = new AccessToken(httpRequest(accessTokenURL,
                    new PostParameter[]{new PostParameter("oauth_verifier", oauth_verifier)}, true));
        } catch (TBlogException te) {
            throw new TBlogException("The user has not given access to the account.", te, te.getStatusCode());
        }
        return (AccessToken) this.oauthToken;
    }

    /**
     * Sets the authorized access token
     * @param token authorized access token
     */
    public void setOAuthAccessToken(AccessToken token){
        this.oauthToken = token;
    }

    public void setRequestTokenURL(String requestTokenURL) {
        this.requestTokenURL = requestTokenURL;
    }

    public String getRequestTokenURL() {
        return requestTokenURL;
    }

    public String getAuthenticationRL() {
        return authenticationURL;
    }

    public void setAccessTokenURL(String accessTokenURL) {
        this.accessTokenURL = accessTokenURL;
    }

    public String getAccessTokenURL() {
        return accessTokenURL;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * Sets proxy host.
     * System property -Dsinat4j.http.proxyHost or http.proxyHost overrides this attribute.
     * @param proxyHost
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = Configuration.getProxyHost(proxyHost);
    }

    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * Sets proxy port.
     * @param proxyPort
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = Configuration.getProxyPort(proxyPort);
    }

    public String getProxyAuthUser() {
        return proxyAuthUser;
    }

    /**
     * Sets proxy authentication user.
     * @param proxyAuthUser
     */
    public void setProxyAuthUser(String proxyAuthUser) {
        this.proxyAuthUser = Configuration.getProxyUser(proxyAuthUser);
    }

    public String getProxyAuthPassword() {
        return proxyAuthPassword;
    }

    /**
     * Sets proxy authentication password.
     * @param proxyAuthPassword
     */
    public void setProxyAuthPassword(String proxyAuthPassword) {
        this.proxyAuthPassword = Configuration.getProxyPassword(proxyAuthPassword);
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Sets a specified timeout value, in milliseconds, to be used when opening a communications link to the resource referenced by this URLConnection.
     * @param connectionTimeout - an int that specifies the connect timeout value in milliseconds
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = Configuration.getConnectionTimeout(connectionTimeout);

    }
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * Sets the read timeout to a specified timeout, in milliseconds. System property -Dsinat4j.http.readTimeout overrides this attribute.
     * @param readTimeout - an int that specifies the timeout value to be used in milliseconds
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = Configuration.getReadTimeout(readTimeout);
    }

    private void encodeBasicAuthenticationString() {
        if (null != userId && null != password) {
            this.basic = "Basic " +
                    new String(new BASE64Encoder().encode((userId + ":" + password).getBytes()));
            oauth=null;
        }
    }

    public void setRetryCount(int retryCount) {
        if (retryCount >= 0) {
            this.retryCount = Configuration.getRetryCount(retryCount);
        } else {
            throw new IllegalArgumentException("RetryCount cannot be negative.");
        }
    }

    public void setUserAgent(String ua) {
        setRequestHeader("User-Agent", Configuration.getUserAgent(ua));
    }
    public String getUserAgent(){
        return getRequestHeader("User-Agent");
    }

    public void setRetryIntervalSecs(int retryIntervalSecs) {
        if (retryIntervalSecs >= 0) {
            this.retryIntervalMillis = Configuration.getRetryIntervalSecs(retryIntervalSecs) * 1000;
        } else {
            throw new IllegalArgumentException(
                    "RetryInterval cannot be negative.");
        }
    }

    public Response post(String url, PostParameter[] postParameters,
                         boolean authenticated) throws TBlogException {
    	PostParameter[] newPostParameters=Arrays.copyOf(postParameters, postParameters.length);
        return httpRequest(url, newPostParameters, authenticated);
    }

    public Response delete(String url, boolean authenticated) throws TBlogException {
    	return httpRequest(url, null, authenticated, "DELETE");
    }
    
    public Response multPartURL(String fileParamName,String url,  PostParameter[] params,File file,boolean authenticated) throws TBlogException {
    	if(this.isHttps) {
    		return httpsMultPart(fileParamName, url, params, file, authenticated);
    	}
    	return httpMultPart(fileParamName, url, params, file, authenticated);
    }
 	
 	private Response httpsMultPart(String fileParamName, String url, PostParameter[] params, File file, boolean authenticated) throws TBlogException {
 		
 		PostMethod post = new PostMethod(url);
 		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
		
 		Part[] parts=null;
		if(params==null){
			parts=new Part[1];
		}else{
			parts=new Part[params.length+1];
		}
		
		FilePart filePart;
		try {
			
			filePart = new FilePart(fileParamName, file.getName(), file, new MimetypesFileTypeMap().getContentType(file), "UTF-8");
			filePart.setTransferEncoding("binary");
			parts[parts.length-1]= filePart;
			post.setRequestEntity( new MultipartRequestEntity(parts, post.getParams()));
			
			if(authenticated) {
				post.addRequestHeader("Authorization", "OAuth2 " + this.oauth2AccessToken);
			}
			
			client.executeMethod(post);
			 
    		Response response=new Response();
    		response.setResponseAsString(post.getResponseBodyAsString());
    		response.setStatusCode(post.getStatusCode());
			return response;
			
    	} catch (Exception ex) {
	   		 throw new TBlogException(ex.getMessage(), ex, -1);
	   	} finally {
	   		post.releaseConnection();
	   		client=null;
	   	}
	}

	private Response httpMultPart(String fileParamName,String url,  PostParameter[] params, File file, boolean authenticated) throws TBlogException {
  		PostMethod post = new PostMethod(url);
  		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
    	try {
    		long t = System.currentTimeMillis();
    		Part[] parts=null;
    		if(params==null){
    			parts=new Part[1];
    		}else{
    			parts=new Part[params.length+1];
    		}
    		if (params != null ) {
    			int i=0;
      			for (PostParameter entry : params) {
      				parts[i++]=new StringPart( entry.getName(),(String)entry.getValue());
    			}
      		}
    		FilePart filePart=new FilePart(fileParamName,file.getName(), file,new MimetypesFileTypeMap().getContentType(file),"UTF-8");
    		filePart.setTransferEncoding("binary");
    		parts[parts.length-1]= filePart;

    		post.setRequestEntity( new MultipartRequestEntity(parts, post.getParams()) );
    		 List<Header> headers = new ArrayList<Header>();   
    		 
    		 if (authenticated) {
    	            if (basic == null && oauth == null) {
    	            }
    	            String authorization = null;
    	            if (null != oauth) {
    	                // use OAuth
    	                authorization = oauth.generateAuthorizationHeader( "POST" , url, params, oauthToken);
    	            } else if (null != basic) {
    	                // use Basic Auth
    	                authorization = this.basic;
    	            } else {
    	                throw new IllegalStateException(
    	                        "Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
    	            }
    	            headers.add(new Header("Authorization", authorization)); 
    	            log("Authorization: " + authorization);
    	        }
    	    client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
    		client.executeMethod(post);
 
    		Response response=new Response();
    		response.setResponseAsString(post.getResponseBodyAsString());
    		response.setStatusCode(post.getStatusCode());
    		
    		log("multPartURL URL:" + url + ", result:" + response + ", time:" + (System.currentTimeMillis() - t));
        	return response;
    	} catch (Exception ex) {
    		 throw new TBlogException(ex.getMessage(), ex, -1);
    	} finally {
    		post.releaseConnection();
    		client=null;
    	}
  	}

    public Response post(String url, boolean authenticated) throws TBlogException {
        return httpRequest(url, new PostParameter[0], authenticated);
    }

    public Response post(String url, PostParameter[] PostParameters) throws
            TBlogException {
        return httpRequest(url, PostParameters, false);
    }

    public Response post(String url) throws
            TBlogException {
        return httpRequest(url, new PostParameter[0], false);
    }

    public Response get(String url, boolean authenticated) throws TBlogException {
        return httpRequest(url, null, authenticated);
    }

    public Response get(String url) throws TBlogException {
        return httpRequest(url, null, false);
    }

    protected Response httpRequest(String url, PostParameter[] postParams,
            boolean authenticated) throws TBlogException {
		int len = 1;
		PostParameter[] newPostParameters = postParams;
    	String method = "GET";
    	if (postParams != null) {
    		method = "POST";
			len = postParams.length;
			newPostParameters = Arrays.copyOf(postParams, len);
    	}
    	
    	if(isHttps) {
    		return httpsRequest(url, newPostParameters, authenticated, method);
    	}else {
    		return httpRequest(url, newPostParameters, authenticated, method);
    	}
    }
    
    class UTF8PostMethod extends PostMethod {

		public UTF8PostMethod(String url) {
			super(url);
		}

		@Override
		public String getRequestCharSet() {
			return "utf-8";
		}
    }
    
    public Response httpsRequest(String url, PostParameter[] params, boolean authenticated, String method) throws TBlogException {
		
    	Response response = null;
    	Protocol.registerProtocol("https", new Protocol("https", new MySSLSocketFactory(), 443));
    	org.apache.commons.httpclient.HttpClient httpclient = new org.apache.commons.httpclient.HttpClient();
    	
    	if("GET".equals(method)) {
    		
    		GetMethod get = new GetMethod(url);
    		
    		if(authenticated) {
    			get.addRequestHeader("Authorization", "OAuth2 " + this.oauth2AccessToken);
    		}
    		
    		try {
				httpclient.executeMethod(get);
				response = new Response(get);
			} catch (HttpException e) {
				throw new TBlogException(e.getMessage(), e);
			} catch (IOException e) {
				throw new TBlogException(e.getMessage(), e);
			}
    		
    	}else if("POST".equals(method)) {
    		
    		UTF8PostMethod post = new UTF8PostMethod(url);
    		for(PostParameter p : params) {
    			post.addParameter(p.getName(), p.getValue());
    		}
    		if(authenticated) {
    			post.addRequestHeader("Authorization", "OAuth2 " + this.oauth2AccessToken);
    		}
    		
    		try {
				httpclient.executeMethod(post);
				response = new Response(post);
			} catch (HttpException e) {
				throw new TBlogException(e.getMessage(), e);
			} catch (IOException e) {
				throw new TBlogException(e.getMessage(), e);
			}
    	}
		return response;
	}

	public Response httpRequest(String url, PostParameter[] postParams, boolean authenticated, String httpMethod) throws TBlogException {
        int retriedCount;
        int retry = retryCount + 1;
        Response res = null;
        for (retriedCount = 0; retriedCount < retry; retriedCount++) {
            int responseCode = -1;
            try {
                HttpURLConnection con = null;
                OutputStream osw = null;
                try {
                    con = getConnection(url);
                    con.setDoInput(true);
                    setHeaders(url, postParams, con, authenticated, httpMethod);
                    if (null != postParams || "POST".equals(httpMethod)) {
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Content-Type",
                                "application/x-www-form-urlencoded");
                        con.setDoOutput(true);
                        String postParam = "";
                        if (postParams != null) {
                        	postParam = encodeParameters(postParams);
                        }
                        log("Post Params: ", postParam);
                        byte[] bytes = postParam.getBytes("UTF-8");

                        con.setRequestProperty("Content-Length",
                                Integer.toString(bytes.length));
                        osw = con.getOutputStream();
                        osw.write(bytes);
                        osw.flush();
                        osw.close();
                    } else if ("DELETE".equals(httpMethod)){
                        con.setRequestMethod("DELETE");
                    } else {
                        con.setRequestMethod("GET");
                    }
                    res = new Response(con);
                    responseCode = con.getResponseCode();
                    if(DEBUG){
                        log("Response: ");
                        Map<String, List<String>> responseHeaders = con.getHeaderFields();
                        for (String key : responseHeaders.keySet()) {
                            List<String> values = responseHeaders.get(key);
                            for (String value : values) {
                                if(null != key){
                                    log(key + ": " + value);
                                }else{
                                    log(value);
                                }
                            }
                        }
                    }
                    if (responseCode != OK) {
                        if (responseCode < INTERNAL_SERVER_ERROR || retriedCount == retryCount) {
                            throw new TBlogException(getCause(responseCode) + "\n" + res.asString(), responseCode);
                        }
                        // will retry if the status code is INTERNAL_SERVER_ERROR 
                    } else {
                        break;
                    }
                } catch(IOException e) {
                	e.printStackTrace();
                	throw e;
                } finally {
                    try {
                        osw.close();
                    } catch (Exception ignore) {
                    }
                }
            } catch (IOException ioe) {
                // connection timeout or read timeout
                if (retriedCount == retryCount) {
                    throw new TBlogException(ioe.getMessage(), ioe, responseCode);
                }
            }
            try {
                if(DEBUG && null != res){
                    res.asString();
                }
                log("Sleeping " + retryIntervalMillis +" millisecs for next retry.");
                Thread.sleep(retryIntervalMillis);
            } catch (InterruptedException ignore) {
                //nothing to do
            }
        }
        return res;
    }

    public static String encodeParameters(PostParameter[] postParams) {
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < postParams.length; j++) {
            if (j != 0) {
                buf.append("&");
            }
            try {
                buf.append(URLEncoder.encode(postParams[j].name, "UTF-8"))
                        .append("=").append(URLEncoder.encode(postParams[j].value, "UTF-8"));
            } catch (java.io.UnsupportedEncodingException neverHappen) {
            }
        }
        return buf.toString();
    }

    /**
     * sets HTTP headers
     *
     * @param connection HttpURLConnection
     * @param authenticated boolean
     */
    private void setHeaders(String url, PostParameter[] params, HttpURLConnection connection, boolean authenticated, String httpMethod) {
        log("Request: ");
        log(httpMethod + " ", url);

        if (authenticated) {
            if (basic == null && oauth == null) {
            }
            String authorization = null;
            if (null != oauth) {
                // use OAuth
                authorization = oauth.generateAuthorizationHeader(httpMethod, url, params, oauthToken);
            } else if (null != basic) {
                // use Basic Auth
                authorization = this.basic;
            } else {
                throw new IllegalStateException(
                        "Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
            }
            connection.addRequestProperty("Authorization", authorization);
            log("Authorization: " + authorization);
        }
        for (String key : requestHeaders.keySet()) {
            connection.addRequestProperty(key, requestHeaders.get(key));
            log(key + ": " + requestHeaders.get(key));
        }
    }

    public void setRequestHeader(String name, String value) {
        requestHeaders.put(name, value);
    }

    public String getRequestHeader(String name) {
        return requestHeaders.get(name);
    }

    private HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection con = null;
        if (proxyHost != null && !proxyHost.equals("")) {
            if (proxyAuthUser != null && !proxyAuthUser.equals("")) {
                log("Proxy AuthUser: " + proxyAuthUser);
                log("Proxy AuthPassword: " + proxyAuthPassword);
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication
                    getPasswordAuthentication() {
                        //respond only to proxy auth requests
                        if (getRequestorType().equals(RequestorType.PROXY)) {
                            return new PasswordAuthentication(proxyAuthUser,
                                    proxyAuthPassword
                                            .toCharArray());
                        } else {
                            return null;
                        }
                    }
                });
            }
            final Proxy proxy = new Proxy(Type.HTTP, InetSocketAddress
                    .createUnresolved(proxyHost, proxyPort));
            if(DEBUG){
                log("Opening proxied connection(" + proxyHost + ":" + proxyPort + ")");
            }
            con = (HttpURLConnection) new URL(url).openConnection(proxy);
        } else {
            con = (HttpURLConnection) new URL(url).openConnection();
        }
        if (connectionTimeout > 0 && !isJDK14orEarlier) {
            con.setConnectTimeout(connectionTimeout);
        }
        if (readTimeout > 0 && !isJDK14orEarlier) {
            con.setReadTimeout(readTimeout);
        }
        return con;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpClient)) return false;

        HttpClient that = (HttpClient) o;

        if (connectionTimeout != that.connectionTimeout) return false;
        if (proxyPort != that.proxyPort) return false;
        if (readTimeout != that.readTimeout) return false;
        if (retryCount != that.retryCount) return false;
        if (retryIntervalMillis != that.retryIntervalMillis) return false;
        if (accessTokenURL != null ? !accessTokenURL.equals(that.accessTokenURL) : that.accessTokenURL != null)
            return false;
        if (!authenticationURL.equals(that.authenticationURL)) return false;
        if (basic != null ? !basic.equals(that.basic) : that.basic != null)
            return false;
        if (oauth != null ? !oauth.equals(that.oauth) : that.oauth != null)
            return false;
        if (oauthToken != null ? !oauthToken.equals(that.oauthToken) : that.oauthToken != null)
            return false;
        if (password != null ? !password.equals(that.password) : that.password != null)
            return false;
        if (proxyAuthPassword != null ? !proxyAuthPassword.equals(that.proxyAuthPassword) : that.proxyAuthPassword != null)
            return false;
        if (proxyAuthUser != null ? !proxyAuthUser.equals(that.proxyAuthUser) : that.proxyAuthUser != null)
            return false;
        if (proxyHost != null ? !proxyHost.equals(that.proxyHost) : that.proxyHost != null)
            return false;
        if (!requestHeaders.equals(that.requestHeaders)) return false;
        if (!requestTokenURL.equals(that.requestTokenURL)) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = basic != null ? basic.hashCode() : 0;
        result = 31 * result + retryCount;
        result = 31 * result + retryIntervalMillis;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (proxyHost != null ? proxyHost.hashCode() : 0);
        result = 31 * result + proxyPort;
        result = 31 * result + (proxyAuthUser != null ? proxyAuthUser.hashCode() : 0);
        result = 31 * result + (proxyAuthPassword != null ? proxyAuthPassword.hashCode() : 0);
        result = 31 * result + connectionTimeout;
        result = 31 * result + readTimeout;
        result = 31 * result + requestHeaders.hashCode();
        result = 31 * result + (oauth != null ? oauth.hashCode() : 0);
        result = 31 * result + requestTokenURL.hashCode();
        result = 31 * result + authenticationURL.hashCode();
        result = 31 * result + (accessTokenURL != null ? accessTokenURL.hashCode() : 0);
        result = 31 * result + (oauthToken != null ? oauthToken.hashCode() : 0);
        return result;
    }

    private static void log(String message) {
        if (DEBUG) {
            System.out.println("[" + new java.util.Date() + "]" + message);
        }
    }

    private static void log(String message, String message2) {
        if (DEBUG) {
            log(message + message2);
        }
    }

    private static String getCause(int statusCode){
        String cause = null;
        switch(statusCode){
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
                cause = "Something is broken.  Please post to the group so the team can investigate.";
                break;
            case BAD_GATEWAY:
                cause = "server is down or being upgraded.";
                break;
            case SERVICE_UNAVAILABLE:
                cause = "Service Unavailable: The servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
                break;
            default:
                cause = "";
        }
        return statusCode + ":" + cause;
    }

	public String getOAuth2AuthorizeURL(String redirectURL) throws TBlogException {
		String url = "";
		try {
			url = "https://api.t.163.com/oauth2/authorize?client_id=" + oauth.consumerKey + "&redirect_uri=" + URLEncoder.encode(redirectURL, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new TBlogException(e);
		}
		return url;
	}
	
	public String getOAuth2AuthorizeTokenURL(String redirectURL) throws TBlogException {
		String url = "";
		try {
			url = "https://api.t.163.com/oauth2/authorize?response_type=token&client_id=" + oauth.consumerKey + "&redirect_uri=" + URLEncoder.encode(redirectURL, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new TBlogException(e);
		}
		return url;
	}

	public OAuth2AccessToken getOAuth2AccessTokenByCode(String code, String redirectURL) throws TBlogException {
		String url = "https://api.t.163.com/oauth2/access_token?client_id=" + oauth.consumerKey + "&client_secret=" + oauth.consumerSecret + "&grant_type=authorization_code&code=" + code + "&redirect_uri=" + redirectURL;
		Response response = httpsRequest(url, null, false, "GET");
		if(response.getStatusCode() == 200) {
			return new OAuth2AccessToken(response);
		}else {
			throw new TBlogException(response.asString());
		}
	}
	
	public OAuth2AccessToken getOAuth2AccessTokenByRefreshToken(String refreshToken) throws TBlogException {
		String url = "https://api.t.163.com/oauth2/access_token?client_id=" + oauth.consumerKey + "&client_secret=" + oauth.consumerSecret + "&grant_type=refresh_token&refresh_token=" + refreshToken;
		Response response = httpsRequest(url, null, false, "GET");
		if(response.getStatusCode() == 200) {
			return new OAuth2AccessToken(response);
		}else {
			throw new TBlogException(response.asString());
		}
	}
	
	public OAuth2AccessToken getOAuth2AccessTokenByPassword(String username, String password) throws TBlogException {
		String url = "https://api.t.163.com/oauth2/access_token?client_id=" + oauth.consumerKey + "&client_secret=" + oauth.consumerSecret + "&grant_type=password&username=" + username + "&password=" + password;
		Response response = httpsRequest(url, null, false, "GET");
		if(response.getStatusCode() == 200) {
			return new OAuth2AccessToken(response);
		}else {
			throw new TBlogException(response.asString());
		}
	}
	
	private String oauth2AccessToken;

	public void setOAuth2AccessToken(String token) {
		this.oauth2AccessToken = token;
	}
}
