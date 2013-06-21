package t4j.example;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import t4j.OAuthVersion;
import t4j.TBlog;
import t4j.TBlogException;

public class OAuth2 {
	
	public static void main(String[] args) throws TBlogException, HttpException, IOException {

		System.setProperty("tblog4j.oauth.consumerKey", "");
    	System.setProperty("tblog4j.oauth.consumerSecret", "");
    	
    	// OAuth 2.0版本，默认是OAuth 1
    	TBlog tblog = new TBlog(OAuthVersion.V2);
    	// 重定向URL必须和应用申请的一致
    	tblog.setRedirectURL("http://www.163.com/");
    	System.out.println("授权URL：" + tblog.getOAuth2AuthorizeURL());
    	
    	//设置重定向后返回的 token
//    	OAuth2AccessToken token = tblog.getOAuth2AccessTokenByCode("");

//    	System.out.println(token);
//    	tblog.setOAuth2AccessToken(token.getAccess_token());
//    	tblog.verifyCredentials();

	}
}
