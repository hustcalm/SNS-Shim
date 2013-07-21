package com.renren.api.client.services;

import java.util.TreeMap;

import org.json.simple.JSONArray;

import com.renren.api.client.RenrenApiInvoker;
import com.renren.api.client.param.Auth;


/**
 * 支付
 * @author Administrator
 *
 */
public class PayService extends BaseService {

    public PayService(RenrenApiInvoker invoker) {
        super(invoker);
        // TODO Auto-generated constructor stub
    }

    /**
     * 查询某个用户在一个应用中一次消费是否完成
     * @param orderId 用户消费校内豆订单号
     * @throws RenrenApiException<br/>
     *         <ul>
     *          <li>10800     传递订单号已失效，无法获取到token</li>
     *          <li>10801     无效的订单号 (小于零)</li>
     *          <li>10802     消费金额无效:不支持大笔消费金额</li>
     *          <li>10803     校内网支付平台,应用资料审核未通过，无法使用平台,请在此处填写申请信息</li>
     *          <li>10804     该订单不存在</li>
     *         </ul>
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 
     */
    public boolean isCompleted(long orderId,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "pay.isCompleted");
        params.put("order_id", String.valueOf(orderId));
        return this.getResultBoolean(params);
    }

    /**
     * 只用于开发者测试使用
     * @param orderId 用户消费校内豆订单号
     * @throws RenrenApiException<br/>
     *         <ul>
     *          <li>10800     传递订单号已失效，无法获取到token</li>
     *          <li>10801     无效的订单号 (小于零)</li>
     *          <li>10802     消费金额无效:不支持大笔消费金额</li>
     *          <li>10803     校内网支付平台,应用资料审核未通过，无法使用平台,请在此处填写申请信息</li>
     *          <li>10804     该订单不存在</li>
     *         </ul>
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return
     */
    public boolean isCompletedTest(long orderId,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "pay4Test.isCompleted");
        params.put("order_id", String.valueOf(orderId));
        return this.getResultBoolean(params);
    }

    /**
     * 预存入用户在应用中消费产生的订单数据，消费数额等信息<br/>
     * <b>注意 :</b>是必须保证某次调用接口用订单号产生唯一的订单，已取得Token的订单号不要再调用此接口
     * @param orderId 用户消费校内豆订单号，参数必须保证唯一，每一次不能传递相同的参数
     * @param amount 校内豆消费数额, 取值范围为[0,1000]
     * @param desc 用户使用校内豆购买的虚拟物品的名称
     * @param type 0代表WEB支付订单，1代表WAP支付订单，默认值为0
     *      ** @throws RenrenApiException<br/>
     *         <ul>
     *          <li>10800     传递订单号已失效，无法获取到token</li>
     *          <li>10801     无效的订单号 (小于零)</li>
     *          <li>10802     消费金额无效:不支持大笔消费金额</li>
     *          <li>10803     校内网支付平台,应用资料审核未通过，无法使用平台,请在此处填写申请信息</li>
     *          <li>10804     该订单不存在</li>
     *         </ul>
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 保证用户某次支付校内豆安全性的Token
     */
    public String regOrder(long orderId, int amount, String desc, int type,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "pay.regOrder");
        params.put("order_id", String.valueOf(orderId));
        params.put("amount", String.valueOf(amount));
        params.put("type", String.valueOf(type));
        params.put("desc", desc);
        return this.getResultValue(params, "token");
    }

    /**
     * 只用于开发者测试使用
     * @param orderId  用户消费校内豆订单号，参数必须保证唯一，每一次不能传递相同的参数
     * @param amount 校内豆消费数额, 取值范围为[0,1000]
     * @param desc 用户使用校内豆购买的虚拟物品的名称
     * @param type 0代表WEB支付订单，1代表WAP支付订单，默认值为0
     *      ** @throws RenrenApiException<br/>
     *         <ul>
     *          <li>10800     传递订单号已失效，无法获取到token</li>
     *          <li>10801     无效的订单号 (小于零)</li>
     *          <li>10802     消费金额无效:不支持大笔消费金额</li>
     *          <li>10803     校内网支付平台,应用资料审核未通过，无法使用平台,请在此处填写申请信息</li>
     *          <li>10804     该订单不存在</li>
     *         </ul>
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 保证用户某次支付校内豆安全性的Token
     */
    public String regOrderTest(long orderId, int amount, String desc, int type,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "pay4Test.regOrder");
        params.put("order_id", String.valueOf(orderId));
        params.put("amount", String.valueOf(amount));
        params.put("type", String.valueOf(type));
        params.put("desc", desc);
        return this.getResultValue(params, "token");
    }

    /**
     * 根据订单号查询订单，支持批量查询 
     * @param order_numbers 订单号，多个订单用逗号隔开
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 查询结果
     */
    public JSONArray queryOrders(String order_numbers,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "pay.queryOrders");
        params.put("order_numbers", order_numbers);
        return this.getResultJSONArray(params);
    }
}
