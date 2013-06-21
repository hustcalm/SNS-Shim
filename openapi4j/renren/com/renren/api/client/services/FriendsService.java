package com.renren.api.client.services;

import java.util.List;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.renren.api.client.RenrenApiInvoker;
import com.renren.api.client.param.Auth;

/**
 * 好友
 * @author DuYang (yang.du@renren-inc.com) 2011-12-14
 *
 */
public class FriendsService extends BaseService {

    public FriendsService(RenrenApiInvoker invoker) {
        super(invoker);
        // TODO Auto-generated constructor stub
    }

    /**
     * 判断两组用户是否互为好友关系，比较的两组用户数必须相等。
     * @param users1 第一组用户的ID，每个ID之间以逗号分隔
     * @param users2    第二组用户的ID，每个ID之间以逗号分隔
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONArray
     */
    public JSONArray areFriends(String users1, String users2, Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "friends.areFriends");
        params.put("uids1", users1);
        params.put("uids2", users2);
        JSONArray friendsInfos = this.getResultJSONArray(params);
        return friendsInfos;
    }

    /**
     * 得到当前登录用户的好友列表，得到的只是含有好友id的列表
     * @param page 分页
     * @param count 每页显示数目
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 好友id列表
     */
    public List<Integer> getFriendIds(int page, int count, Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "friends.get");
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        return this.getResultIntList(params);
    }

    /**
     * 返回共同的好友
     * @param uid1 用户1
     * @param uid2 用户2
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONArray 共同好友
     */
    public JSONObject getSameFriends(long uid1, long uid2, Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "friends.getSameFriends");
        params.put("uid1", String.valueOf(uid1));
        params.put("uid2", String.valueOf(uid2));
        return this.getResultJSONObject(params);
    }

    /**
     * 得到当前登录用户的好友列表
     * @param page 分页
     * @param count 每页显示数目
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONArray 好友详细信息列表
     */
    public JSONArray getFriends(int page, int count, Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "friends.getFriends");
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        return this.getResultJSONArray(params);
    }

    /**
     * 返回App好友的ID列表。App好友是指某个用户安装了同一应用的好友。
     * @param fields 返回的字段列表，可以指定返回那些字段，用逗号分隔。
     *        目前支持name（姓名）、
     *        tinyurl(小头像)、
     *        headurl（中等头像）
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONArray
     */
    public JSONArray getAppUsers(String fields,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "friends.getAppFriends");
        params.put("fields", fields);
        JSONArray friends = this.getResultJSONArray(params);
        return friends;
    }

    /**
     * 
     * @param name 按姓名搜索
     * @param uid 按uid搜索 0忽略
     * @param email 按email搜索
     * @param page  分页
     * @param count 每页数量
     * @param condition 搜索条件 JSON字符串
     *        name uid email condition 至少一个
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONArray
     */
    public JSONObject searchFriends(String name, long uid, String email, int page, int count,
                                    String condition, Auth auth) {

        /*
         * condition 有待改进
         * */
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "friends.search");
        params.put("name", name);
        if (uid > 0) {
            params.put("uid", String.valueOf(uid));
        }
        params.put("email", email);
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        params.put("condition", condition);
        JSONObject friends = this.getResultJSONObject(params);
        return friends;
    }

}
