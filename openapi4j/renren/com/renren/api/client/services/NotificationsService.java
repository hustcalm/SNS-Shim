package com.renren.api.client.services;

import java.util.TreeMap;

import com.renren.api.client.RenrenApiInvoker;
import com.renren.api.client.param.Auth;


/**
 * 发送通知
 * @author Administrator
 *
 */
public class NotificationsService extends BaseService {

    public NotificationsService(RenrenApiInvoker invoker) {
        super(invoker);
        // TODO Auto-generated constructor stub
    }

    /**
     * 给用户发送通知<br>
     * 需要用户授予 notifications_send 权限
     * @param toIds 用户id列表，单个或多个，逗号分隔，不多于20
     * @param notification 通知内容
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return
     */
    public int send(String toIds, String notification,Auth auth) {
        String type = "user_to_user";// app_to_user , user_to_user(default) ,app_to_wap(mobile client)
        return this.send(toIds, notification, type,auth);
    }

    /**
     * 发送通知
     * 需要用户授予 notifications_send 权限<br/>
     * @param toIds 用户id的列表，单个或多个，可以是逗号分隔，不多于20
     * @param notification 通知的内容
     * @param type 类型
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return
     */
    public int send(String toIds, String notification, String type,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "notifications.send");
        params.put("to_ids", toIds);
        params.put("type", type);
        params.put("notification", notification);
        return this.getResultInt(params);
    }
}
