SNS-Shim
========

A simple wrapper to encapsulate the common SNS APIs, QQ, Weibo, Renren, Douban, etc.

Basically, we will focus on Java implementation and the main step will be, first get authorized, then do whatever with the access token and uid.

Keypoints are OAuth(to deal with user authorization) and Sqlite(to store user information, mainly access token and uid) and Maven(to maintain the dependencies of all the Third Party libs and Java SDKs).

## OpenAPI
*   [QQ Connect](http://connect.qq.com/)
*   [Sina Weibo](http://open.weibo.com/)
*   [RenRen](http://dev.renren.com/)
*   [Tecent Weibo](http://dev.t.qq.com/)
*   [Douban](http://developers.douban.com/)
*   [163 Weibo](http://open.t.163.com/)

## OAuth

### Understanding the Protocal
*   [OAuth Official Site](http://oauth.net/)
*   [OAuth Implementation in Java](http://blog.csdn.net/zhujing244/article/details/6682440)
*   [OAuth认证协议原理分析及使用方法](http://kejibo.com/oauth/)
*   [OAuth介绍 - 协议解析](http://plaintext.blog.edu.cn/2011/704841.html)

### Specific OpenAPI Usage
*   [QQ Connect OAuth](http://wiki.opensns.qq.com/wiki/%E3%80%90QQ%E7%99%BB%E5%BD%95%E3%80%91Qzone_OAuth2.0%E7%AE%80%E4%BB%8B#2._QQ.E7.99.BB.E5.BD.95OAuth2.0.E7.9A.84.E5.A4.84.E7.90.86.E6.B5.81.E7.A8.8B)
*   [Sina Weibo OAuth](ttp://open.weibo.com/wiki/OAuth)
*   [RenRen OAuth](http://wiki.dev.renren.com/wiki/Authentication)
*   [Tecent Weibo OAuth](http://wiki.open.t.qq.com/index.php/OAuth%E6%8E%88%E6%9D%83%E8%AF%B4%E6%98%8E)
*   [Douban OAuth](http://www.douban.com/service/apidoc/auth)
*   [163 Weibo OAuth](http://open.t.163.com/wiki/index.php?title=OAuth%E6%8E%88%E6%9D%83%E8%AF%B4%E6%98%8E)

### Existing Wraper for OAuth
*   [oauth-api](http://code.google.com/p/oauth-api/)
*   [oauth-for-qq-renren-sina](https://code.google.com/p/oauth-for-qq-renren-sina/)

## Begin developing
*   Apply for an application to get appKey and appSecret
*   Download the Java SDK and test it as needed
*   Integrate the SDK to the wrapper

## The wrapper
### Existing projects
*   [eeplat-social-api](http://code.google.com/p/eeplat-social-api/)
*   [socialauth](http://code.google.com/p/socialauth/)

