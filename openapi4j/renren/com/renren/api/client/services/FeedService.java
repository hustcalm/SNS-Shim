package com.renren.api.client.services;

import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.renren.api.client.RenrenApiInvoker;
import com.renren.api.client.param.Auth;

/**
 * 新鲜事
 * @author DuYang (yang.du@renren-inc.com) 2011-12-14
 *
 */
public class FeedService extends BaseService {

    public FeedService(RenrenApiInvoker invoker) {
        super(invoker);
        // TODO Auto-generated constructor stub
    }

    /**
     * 获取当前用户的新鲜事内容
     * 注意：此方法需要用户授予 read_user_feed 权限(在OAuth2.0授权中由scope参数指定)
     * @param type 新鲜事的类别，多个类型以逗号分隔
     *        <ul>
     *        <li>10    更新状态的新鲜事。</li>
     *        <li>11  page更新状态的新鲜事。</li>
     *        <li>20  发表日志的新鲜事。</li>
     *        <li>21  分享日志的新鲜事。</li>
     *        <li>22  page发表日志的新鲜事。</li>
     *        <li>23  page分享日志的新鲜事。</li>
     *        <li>30  上传照片的新鲜事。</li>
     *        <li>31  page上传照片的新鲜事。</li>
     *        <li>32  分享照片的新鲜事。</li>
     *        <li>33  分享相册的新鲜事。</li>
     *        <li>34  修改头像的新鲜事。</li>
     *        <li>35  page修改头像的新鲜事。</li>
     *        <li>36  page分享照片的新鲜事。</li>
     *        <li>40  成为好友的新鲜事。</li>
     *        <li>41  成为page粉丝的新鲜事。</li>
     *        <li>50  分享视频的新鲜事。</li>
     *        <li>51  分享链接的新鲜事。</li>
     *        <li>52  分享音乐的新鲜事。</li>
     *        <li>53  page分享视频的新鲜事。</li>
     *        <li>54  page分享链接的新鲜事。</li>
     *        <li>55  page分享音乐的新鲜事</li>
     *        </ul>
     * @param uid 支持传入当前用户的一个好友ID,表示获取此好友的新鲜事
     *        0表示获取当前用户的新鲜事 
     * @param page 支持分页，指定页号页号从1开始
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return Feed json 新鲜事内容
     */
    public JSONArray getFeed(String type, long uid, int page, int count, Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "feed.get");
        params.put("type", type);
        if (uid > 0) {
            params.put("uid", String.valueOf(uid));
        }
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        return this.getResultJSONArray(params);
    }

    /**
     * 以当前会话的用户（session_key对应用户）身份发送应用的自定义新鲜事
     * 需要public_feed权限
     * @param name 新鲜事标题 注意：最多30个字符 
     * @param description 新鲜事主体内容 注意：最多200个字符
     * @param url 新鲜事标题和图片指向的链接
     * @param image 新鲜事图片地址 
     * @param caption 新鲜事副标题 注意：最多20个字符 
     * @param action_name 新鲜事动作模块文案。 注意：最多10个字符
     * @param action_link 新鲜事动作模块链接
     * @param message 用户输入的自定义内容。注意：最多200个字符
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 包含新鲜事id的JSONObject
     */
    public JSONObject publicFeed(String name, String description, String url, String image,
                                 String caption, String action_name, String action_link,
                                 String message, Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "feed.publishFeed");
        params.put("name", name);
        params.put("description", description);
        params.put("url", url);
        params.put("image", image);
        params.put("caption", caption);
        params.put("action_name", action_name);
        params.put("action_link", action_link);
        params.put("message", message);
        return this.getResultJSONObject(params);
    }
}
