package com.renren.api.client.services;

import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.renren.api.client.RenrenApiInvoker;
import com.renren.api.client.param.Auth;


/**
 * 状态
 * @author DuYang (yang.du@renren-inc.com) 2011-12-14
 *
 */
public class StatusService extends BaseService {

    public static final String TYPE_ALL    = "all";

    public static final String TYPE_RECENT = "recent";

    public StatusService(RenrenApiInvoker invoker) {
        super(invoker);
        // TODO Auto-generated constructor stub
    }

    /**
     * 返回指定用户的状态列表，不包含回复内容
     * 注意：此方法需要用户授予 read_user_status 权限(在OAuth2.0授权中由scope参数指定)
     * @param uid 状态信息所属用户id 0返回当前用户
     * @param page 分页
     * @param count 每页个数 最大不能超过1000
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONArray 状态列表
     */
    public JSONArray getStatuses(long uid, int page, int count,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "status.gets");
        if (uid > 0) {//
            params.put("uid", String.valueOf(uid));
        }
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        return this.getResultJSONArray(params);
    }

    /**
     * 返回用户某条状态，不包含回复内容
     * 注意：此方法需要用户授予 read_user_status 权限(在OAuth2.0授权中由scope参数指定)
     * @param status_id 状态的id  0返回当前用户最新的状态
     * @param owner_id 状态信息所属用户id，0前用户
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return
     */
    public JSONObject getStatus(long status_id, long owner_id,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "status.get");
        if (status_id > 0) {//
            params.put("uid", String.valueOf(status_id));
        }
        if (owner_id > 0) {
            params.put("owner_id", String.valueOf(owner_id));
        }
        return this.getResultJSONObject(params);
    }


    /**
    * 用户更新状态。
     * 注意：此方法需要用户授予 status_update 权限(在OAuth2.0授权中由scope参数指定)
     * @param status 用户更新的状态信息，最多140个字符
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return result 1成功 0失败
     */
    public int setStatus(String status,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "status.set");
        params.put("status", status);
        return this.getResultInt(params);
    }

    /**
     * 获取一条状态中的所有最新回复内容
     * 注意：此方法需要用户授予 read_user_status 权限(在OAuth2.0授权中由scope参数指定)
     * @param status_id 状态的id
     * @param owner_id 状态所有者的id
     * @param page 支持分页，指定页号，页号从1开始
     * @param count 支持分页，指定每页记录数
     * @param order 获取留言的排序规则，0表示升序(最旧到新)，1表示降序(最新到旧)
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONArray
     */
    public JSONArray getComments(long status_id, long owner_id, int page, int count, int order,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "status.getComment");
        params.put("status_id", String.valueOf(status_id));
        params.put("owner_id", String.valueOf(owner_id));
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        params.put("order", String.valueOf(order));
        return this.getResultJSONArray(params);
    }

    /**
     * 对一条状态增加一条回复
     * 注意：此方法需要用户授予 read_user_status,publish_comment 权限(在OAuth2.0授权中由scope参数指定)
     * @param owner_id 状态所有者的ID 
     * @param status_id 状态的ID
     * @param content 回复的内容 
     * @param rid 被回复的用户的ID 0表示直接回复某条状态
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return int
     */
    public int addComment(long status_id, long owner_id, String content, long rid,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "status.addComment");
        params.put("status_id", String.valueOf(status_id));
        params.put("owner_id", String.valueOf(owner_id));
        params.put("content", content);
        if (rid > 0) {
            params.put("rid", String.valueOf(rid));
        }
        return this.getResultInt(params);
    }

    /**
     * 获取状态中的表情图片和表情符号对应列表
     * @param type 获取列表的类型
     *      <ul>
     *      <li>TYPE_ALL返回全部数据</li>
     *      <li>TYPE_RECENT返回网页同步数据</li>
     *      </ul>
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 表情图片信息
     */
    public JSONArray getEmoticons(String type,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "status.getEmoticons");
        params.put("type", type);
        return this.getResultJSONArray(params);
    }

    /**
     * 获取状态中的表情图片和表情符号对应列表
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return
     */
    public JSONArray getEmoticons(Auth auth) {
        return this.getEmoticons(TYPE_RECENT,auth);
    }

    /**
     * 用户转发状态的操作，支持同时评论给被转发人
     * 注意：此方法需要用户授予 status_update 权限
     * @param forward_id 被转发的状态id
     * @param forward_owner 被转发的状态所有者的id
     * @param status 用户更新的状态信息，与转发前的内容加在一起最多240个字符
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 新生成状态Id
     */
    public JSONObject forwardStatus(long forward_id, long forward_owner, String status,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "status.forward");
        params.put("forward_id", String.valueOf(forward_id));
        params.put("forward_owner", String.valueOf(forward_owner));
        return this.getResultJSONObject(params);
    }
}
