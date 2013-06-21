package com.renren.api.client.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.renren.api.client.RenrenApiInvoker;
import com.renren.api.client.param.Auth;


/**
 * 
 * @author Administrator
 *
 */
public class InvitationsService extends BaseService {
    
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public final static int DOMAIN_RENREN = 0;

    public final static int DOMAIN_KAIXIN = 1;
    
    public InvitationsService(RenrenApiInvoker invoker) {
        super(invoker);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * 生成邀请站外用户注册的链接地址.应用通过调用该接口可以引导用户通过QQ或者msn等渠道邀请站外好友加入应用
     * @param domain 获取邀请链接地址的域名属性，0表示人人(wwv.renren.com)，1表示开心(wwv.kaixin.com)
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return url
     */
    public String createLink(int domain,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "invitations.createLink");
        params.put("domain", String.valueOf(domain));
        return this.getResultStringList(params).get(0);
    }
    /**
     * 根据应用新用户的id,获取用户是否通过接受邀请的方式安装，同时得到此次邀请的详细信息（包括邀请者、邀请时间、被邀请者等）
     * @param inviteeId 被邀请者的用户ID
     * @return 邀请的详细信息
     */
    public JSONObject getInfo(long inviteeId) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("method", "invitations.getInfo");
        params.put("invitee_uid", String.valueOf(inviteeId));
        JSONArray rets = this.getResultJSONArray(params);
        if (rets.size() < 1) return null;
        return (JSONObject) this.getResultJSONArray(params).get(0);
    }
    
    
    /**
     * 根据查询起止时间,获取用户是否通过接受邀请的方式安装，同时得到此次邀请的详细信息（包括邀请者、邀请时间、被邀请者等）
     * @param begin 查询起始时间
     * @param end 查询结束时间
     * @param page 分页 从1开始
     * @param count 每页数量
     * @return
     */
    public JSONArray getInfo(Date begin, Date end,int page,int count) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("method", "invitations.getInfo");
        params.put("begin_time", dateFormat.format(begin));
        params.put("end_time", dateFormat.format(end));
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        return this.getResultJSONArray(params);
    }
    
    /**
     * 根据应用新用户的id,获取用户是否通过接受邀请的方式安装，同时得到此次邀请的详细信息（包括邀请者、邀请时间、被邀请者等）
     * @param inviter_id 邀请用户id
     * @param page 分页 1开始
     * @param count 每页个数
     * @return
     */
    public JSONArray getInfo(long inviter_id,int page,int count){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("method", "invitations.getInfo");
        params.put("inviter_id",String.valueOf(inviter_id));
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        return this.getResultJSONArray(params);
    }
}
