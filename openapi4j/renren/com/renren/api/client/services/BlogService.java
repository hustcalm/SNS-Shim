package com.renren.api.client.services;

import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.renren.api.client.RenrenApiInvoker;
import com.renren.api.client.param.Auth;


/**
 * 日志
 * @author DuYang (yang.du@renren-inc.com) 2011-12-14
 *
 */
public class BlogService extends BaseService {

    /**
     * 日志权限
     */
    public static final int     BLOG_VISABLE_ALL    = 99;
    public static final int     BLOG_VISABLE_FRIEND = 1;
    public static final int     BLOG_VISABLE_OWN    = -1;
    /**
     * 日志属于个人 \ 公共主页
     */
    public static final boolean BLOG_FOR_USER       = true;
    public static final boolean BLOG_FOR_PAGE       = false;

    public BlogService(RenrenApiInvoker invoker) {
        super(invoker);
        // TODO Auto-generated constructor stub
    }

    /**
     * 发表普通日志
     * 注意：此方法token需要用户授予 publish_blog 权限(在OAuth2.0授权中由scope参数指定)
     * @param title 日志标题
     * @param content 日志内容
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 日志ID
     */
    public int addBlog(String title, String content,Auth auth) {
        return this.addBlog(title, content, BLOG_VISABLE_ALL, "",auth);
    }

    /**
     * 发表日志
     * 注意：此方法需token要用户授予 publish_blog 权限(在OAuth2.0授权中由scope参数指定)
     * @param title 日志标题
     * @param content 日志内容
     * @param visable 日志的隐私设置 BLOG_VISABLE_ALL BLOG_VISABLE_FRIEND BLOG_VISABLE_OWN
     * @param password 密码
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 日志ID
     */
    public int addBlog(String title, String content, int visable, String password,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "blog.addBlog");
        params.put("title", title);
        params.put("content", content);
        params.put("visable", String.valueOf(visable));
        params.put("password", password);
        return this.getResultInt(params, "id");
    }

    /**
     * 返回指定用户的所有可浏览的日志
     * 注意：此方法token需要用户授予 read_user_blog 权限(在OAuth2.0授权中由scope参数指定)
     * @param uid 用户id
     * @param page 页数
     * @param count 每页数目
     * @param flag 判断uid为个人或公共主页 (BLOG_FOR_USER \ BLOG_FOR_PAGE)
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONArray
     */
    public JSONObject getBlogs(long uid, int page, int count, boolean flag,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "blog.gets");
        if (flag == BLOG_FOR_USER) { //属于个人日志
            params.put("uid", String.valueOf(uid));
        } else {//属于公主主页日志
            params.put("page_id", String.valueOf(uid));
        }
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        return this.getResultJSONObject(params);
    }

    /**
     * 获取自己或好友一篇日志的信息。 
     * 注意：此方法token需要用户授予 read_user_blog 权限(在OAuth2.0授权中由scope参数指定)
     * @param uid 用户id
     * @param blogid 日志id
     * @param comment 返回评论数 0为不返回
     * @param password 日志密码（当设置密码时有效）
     * @param flag BLOG_FOR_USER BLOG_FOR_PAGE 个人 或公共主页的日志
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONObject 日志的信息
     */
    public JSONObject getBlog(long uid, long blogid, int comment, String password, boolean flag,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "blog.get");
        if (flag == BLOG_FOR_USER) {
            params.put("uid", String.valueOf(uid));
        } else {
            params.put("page_id", String.valueOf(uid));
        }
        params.put("id", String.valueOf(blogid));
        params.put("comment", String.valueOf(comment));
        params.put("password", password);
        return this.getResultJSONObject(params);
    }

    /**
     * 获取评论
     * 注意：此方法token需要用户授予 read_user_blog,read_user_comment 权限(在OAuth2.0授权中由scope参数指定)
     * @param uid 用户的ID \公共主页ID
     * @param blogid 日志id
     * @param page 分页的页数
     * @param count 最大值为50, 每页所包含的评论数
     * @param order 排序方式。1：代表逆序；0：正序
     * @param flag 判断用户或公共主页 (BLOG_FOR_USER \ BLOG_FOR_PAGE)
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONArray 评论信息
     */
    public JSONArray getComments(long uid, long blogid, int page, int count, int order, boolean flag,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "blog.getComments");
        if (flag == BLOG_FOR_USER) {
            params.put("uid", String.valueOf(uid));
        } else {
            params.put("page_id", String.valueOf(uid));
        }
        params.put("id", String.valueOf(blogid));
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        params.put("order", String.valueOf(order));
        
        return this.getResultJSONArray(params);
    }

    /**
     * 发布对一篇日志的评论。 
     * 注意：此方法token需要用户授予 publish_blog,publish_comment 权限(在OAuth2.0授权中由scope参数指定)
     * @param id 日志id
     * @param uid 用户id或公共主页id
     * @param content 评论内容
     * @param rid 用于二级回复，被回复的人的用户ID 0表示忽略此参数
     * @param type  是否为悄悄话，1表示悄悄话，0表示公开评论
     * @param flag 判断用户或公共主页 (BLOG_FOR_USER \ BLOG_FOR_PAGE)
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return int结果
     */
    public int addComment(long id, long uid, String content, long rid, int type, boolean flag,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "blog.addComment");
        if (flag == BLOG_FOR_USER) {
            params.put("uid", String.valueOf(uid));
        } else {
            params.put("page_id", String.valueOf(uid));
        }
        params.put("id", String.valueOf(id));
        params.put("content",content);
        if (rid > 0) {
            params.put("rid", String.valueOf(rid));
        }
        params.put("type", String.valueOf(type));
        return this.getResultInt(params);
    }

}
