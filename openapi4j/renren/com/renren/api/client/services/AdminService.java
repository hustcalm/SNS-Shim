package com.renren.api.client.services;

import java.util.TreeMap;

import org.json.simple.JSONObject;

import com.renren.api.client.RenrenApiInvoker;
import com.renren.api.client.param.Auth;


/**
 * 应用管理者
 * @author DuYang (yang.du@renren-inc.com) 2011-12-14
 *
 */
public class AdminService extends BaseService {

    public AdminService(RenrenApiInvoker invoker) {
        super(invoker);
        // TODO Auto-generated constructor stub
    }

    /**
     * 获取一个应用当天可以发送的通知的配额
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     * @param isAccessToken true表示传入的为accessToken false表示传入的为sessionKey
     * @return JSONObject 配额信息
     */
    public JSONObject getAllocation(Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "admin.getAllocation");
        return this.getResultJSONObject(params);
    }

    /**
     * 对给定的文本进行过滤，返回过滤后的文本，如果不合法，则会抛出异常
     * @param text 需要过滤的文本
     * @param type 过滤的文本类型，１代表纯文本，2代表包含脚本或标签的文本
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 过滤后的结果
     */
    public String textFilter(String text, int type,Auth auth)
   {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("text", text);
        params.put("type", String.valueOf(type));
        return this.getResultValue(params, "check_result");
    }
}
