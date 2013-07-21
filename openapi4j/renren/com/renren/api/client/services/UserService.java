package com.renren.api.client.services;

import java.util.TreeMap;

import org.json.simple.JSONArray;

import com.renren.api.client.RenrenApiInvoker;
import com.renren.api.client.param.Auth;


public class UserService extends BaseService {

    public UserService(RenrenApiInvoker invoker) {
        super(invoker);
        // TODO Auto-generated constructor stub
    }

    /**
     * 得到用户的信息支持批量操作
     * @param userIds 需要查询的用户的ID，多个ID用逗号隔开。
     * @param fields 返回的字段列表，可以指定返回那些字段，用逗号分隔。
     *        如：uid,name,sex,star,
     *        zidou,vip,birthday,tinyurl,
     *        headurl,mainurl,hometown_location,
     *        work_history,university_history
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONArray
     */
    public JSONArray getInfo(String userIds, String fields,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "users.getInfo");
        params.put("uids", userIds);
        params.put("fields", fields);
        return this.getResultJSONArray(params);
    }

    /**
     * 得到用户的信息，支持批量操作，返回默认信息(uid,name,tinyurl,headhurl,zidou,star)
     * @param userIds 需要查询的用户的ID，多个ID用逗号隔开。
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONArray
     */
    public JSONArray getInfo(String userIds,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "users.getInfo");
        params.put("uids", userIds);
        return this.getResultJSONArray(params);
    }

    /**
     * 得到当前session的用户ID。
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return int
     */
    public int getLoggedInUser(Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "users.getLoggedInUser");
        String uid = this.getResultValue(params, "uid");
        return Integer.parseInt(uid);
    }

    /**
     * 检查用户是否授予应用扩展权限 
     * @param extPerm 用户可操作的扩展授权，例如email
     * @param userId 用户ID
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return boolean
     */
    public boolean hasAppPermission(String extPerm, long userId,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "users.hasAppPermission");
        params.put("ext_perm", extPerm);
        params.put("uid", String.valueOf(userId));
        return this.getResultBoolean(params);
    }

    /**
     * 判断用户是否已对App授权
     * @param userId 用户ID
     * @return boolean
     */
    public boolean isAppUser(long userId) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("method", "users.isAppUser");
        params.put("uid", String.valueOf(userId));

        return this.getResultBoolean(params);
    }
}
