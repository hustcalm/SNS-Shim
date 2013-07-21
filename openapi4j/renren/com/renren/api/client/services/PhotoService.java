package com.renren.api.client.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.renren.api.client.RenrenApiInvoker;
import com.renren.api.client.param.Auth;
import com.renren.api.client.utils.HttpURLUtils;

/**
 * 相册
 * @author DuYang (yang.du@renren-inc.com) 2011-12-14
 *
 */
public class PhotoService extends BaseService {

    private static final String CONTENT_TYPE_BMP  = "image/bmp";

    private static final String CONTENT_TYPE_PNG  = "image/png";

    private static final String CONTENT_TYPE_GIF  = "image/gif";

    private static final String CONTENT_TYPE_JPEG = "image/jpeg";

    private static final String CONTENT_TYPE_JPG  = "image/jpg";

    public static final boolean IS_PHOTO          = true;

    public static final boolean IS_ALBUM          = false;

    public PhotoService(RenrenApiInvoker invoker) {
        super(invoker);
        // TODO Auto-generated constructor stub
    }

    /**
     * 上传本地图片
     * @param albumId 相册ID
     * @param fileName 图片名称
     * @param description 图片描述
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 图片信息
     */
    public JSONObject uploadLocalImg(long albumId, String fileName, String description,Auth auth) {
        InputStream in = null;
        byte[] photo = null;
        File file = new File(fileName);
        if (file.isFile() && file.length() > 0) {
            photo = new byte[(int) file.length()];
            try {
                in = new FileInputStream(file);
                in.read(photo);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return this.upload(albumId, photo, fileName, description,auth);
    }

    /**
     * 上传网络图片
     * @param albumId 相册ID
     * @param url 图片URL地址
     * @param description 图片描述
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 图片信息
     */
    public JSONObject uploadURLImg(long albumId, String url, String description,Auth auth) {
        byte[] photo = HttpURLUtils.getBytes(url);
        return this.upload(albumId, photo, url, description,auth);
    }

    /**
     * 上传照片
     * 注意：此方法需要用户授予 photo_upload 权限(在OAuth2.0授权中由scope参数指定)
     * @param albumId 相册id
     * @param photo 文件
     * @param fileName 文件名
     * @param description 照片描述 
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return 图片信息
     */
    public JSONObject upload(long albumId, byte[] photo, String fileName, String description,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "photos.upload");
        params.put("aid", String.valueOf(albumId));
        params.put("caption", description);

        String contentType = parseContentType(fileName);
        String contnet = this.invoker.uploadPhoto(params, fileName, contentType, photo);
        return (JSONObject) JSONValue.parse(contnet);
    }

    private String parseContentType(String fileName) {
        String contentType = "image/jpg";
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".jpg"))
            contentType = CONTENT_TYPE_JPG;
        else if (fileName.endsWith(".png"))
            contentType = CONTENT_TYPE_PNG;
        else if (fileName.endsWith(".jpeg"))
            contentType = CONTENT_TYPE_JPEG;
        else if (fileName.endsWith(".gif"))
            contentType = CONTENT_TYPE_GIF;
        else if (fileName.endsWith(".bmp"))
            contentType = CONTENT_TYPE_BMP;
        else
            throw new RuntimeException("不支持的文件类型'" + fileName + "'(或没有文件扩展名)");
        return contentType;
    }

    /**
     * 获取用户指定的相册列表信息。当取第一页时，会返回头像相册和快速上传相册。
     * 注意：此方法需要用户授予 read_user_album 权限(在OAuth2.0授权中由scope参数指定)
     * @param uid 用户id
     * @param page 分页
     * @param count 每页个数 上线1000
     * @param aids 多个相册的ID，以逗号分隔，最多支持10个数据
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSAONArray
     */
    public JSONArray getAlbums(long uid, int page, int count, String aids,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "photos.getAlbums");
        params.put("uid", String.valueOf(uid));
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        params.put("aids", aids);
        return this.getResultJSONArray(params);
    }

    /**
     * 返回指定用户指定的照片
     * 注意：此方法需要用户授予 read_user_photo 权限(在OAuth2.0授权中由scope参数指定)
     * @param uid 用户id
     * @param pids 照片id串，以分","割，最多20个
     * @param password 照片所在相册密码（当相册设置密码时使用，若pids指定的照片中有一张密码错误，则失败）
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSAONArray
     */
    public JSONArray getPhotos(long uid, String pids, String password,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "photos.get");
        params.put("uid", String.valueOf(uid));
        params.put("pids", pids);
        return this.getResultJSONArray(params);
    }

    /**
     * 返回指定相册中的照片<br/>注意：此方法需要用户授予 read_user_photo 权限(在OAuth2.0授权中由scope参数指定)
     * @param uid 用户或公共主页id
     * @param aid 相册id
     * @param page 分页
     * @param count 每页个数
     * @param password 密码（当相册设置密码时使用） 
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONArray
     */
    public JSONArray getPhotos(long uid, long aid, int page, int count, String password,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "photos.get");
        params.put("uid", String.valueOf(uid));
        params.put("aid", String.valueOf(aid));
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        params.put("password", password);
        return this.getResultJSONArray(params);
    }

    /**
     * 对可见照片\相册进行评论<br/>
     * 注意：此方法需要用户授予 read_user_photo，publish_comment 权限(在OAuth2.0授权中由scope参数指定)
     * @param uid 照片所有者ID
     * @param pid 照片ID
     * @param content 评论内容 140字以内
     * @param rid 二次评论的ID 0表示忽略此参数，即不适用二次回复
     * @param type 是否为悄悄话，1表示悄悄话，0表示公开评论
     * @param flag 判断评论相册或照片 (IS_PHOTO \ IS_ALBUM )
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return int
     */
    public int addComment(long uid, long pid, String content, long rid, int type, boolean flag,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "photos.addComment");
        params.put("uid", String.valueOf(uid));
        if (flag == IS_PHOTO) {// 评论照片
            params.put("pid", String.valueOf(pid));
        } else {
            params.put("aid", String.valueOf(pid));
        }
        params.put("content", content);
        if (rid > 0) {
            params.put("rid", String.valueOf(rid));
        }
        params.put("type", String.valueOf(type));
        return this.getResultInt(params);
    }

    /**
     * 获取可见照片或相册的相关评论内容<br/>
     * 注意：此方法需要用户授予 read_user_photo，read_user_comment 权限(在OAuth2.0授权中由scope参数指定)
     * @param uid 照片或相册所有者的用户ID
     * @param pid 照片或相册的ID
     * @param page 分页
     * @param count 每页个数
     * @param flag 区别照片或相册(IS_PHOTO \ IS_ALBUM )
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return
     */
    public JSONArray getComments(long uid, long pid, int page, int count, boolean flag,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "photos.getComments");
        params.put("uid", String.valueOf(uid));
        if (flag == IS_PHOTO) {// 评论照片
            params.put("pid", String.valueOf(pid));
        } else {
            params.put("aid", String.valueOf(pid));
        }
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        return this.getResultJSONArray(params);
    }

    /**
     * 以session_key \ access_token 对应用户的身份圈出照片中的好友或人，<br/>
     * 此API会发送圈人请求，待用户接受圈人请求后会发出圈人新鲜事。 <br/>
     * 注意：此方法需要用户授予 send_request 权限(在OAuth2.0授权中由scope参数指定)
     * @param photo_id 照片的ID
     * @param owner_id 照片所有者的ID
     * @param photo_width 照片的宽度，以像素为单位
     * @param photo_height  照片的高度，以像素为单位
     * @param top 圈人框距照片上端的距离，以像素为单位
     * @param left 圈人框距照片左端的距离，以像素为单位
     * @param frame_width 圈人框的宽度，以像素为单位
     * @param frame_height 圈人框的高度，以像素为单位
     * @param tagged_user_id 被圈用户的ID，该ID对应用户必须与session_key \access_token对应用户为好友关系
     *         此参数会触发圈人请求
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONObject
     */
    public JSONObject addTag(long photo_id, long owner_id, int photo_width, int photo_height,
                             int top, int left, int frame_width, int frame_height,
                             long tagged_user_id,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "photos.tag");
        params.put("photo_id", String.valueOf(photo_id));
        params.put("owner_id", String.valueOf(owner_id));
        params.put("photo_width", String.valueOf(photo_width));
        params.put("photo_height", String.valueOf(photo_height));
        params.put("top", String.valueOf(top));
        params.put("left", String.valueOf(left));
        params.put("frame_width", String.valueOf(frame_width));
        params.put("frame_height", String.valueOf(frame_height));
        params.put("tagged_user_id", String.valueOf(tagged_user_id));
        return this.getResultJSONObject(params);
    }

    /**
     * 以session_key \ access_token 对应用户的身份圈出照片中的好友或人，<br/>
     * 不会有圈人请求触发，只是圈出照片中的人<br/>
     * 注意：此方法需要用户授予 send_request 权限(在OAuth2.0授权中由scope参数指定)<br/>
     * @param photo_id 照片的ID
     * @param owner_id 照片所有者的ID
     * @param photo_width 照片的宽度，以像素为单位
     * @param photo_height  照片的高度，以像素为单位
     * @param top 圈人框距照片上端的距离，以像素为单位
     * @param left 圈人框距照片左端的距离，以像素为单位
     * @param frame_width 圈人框的宽度，以像素为单位
     * @param frame_height 圈人框的高度，以像素为单位
     * @param tagged_user_name 被圈用户的名字
     *        此参数不会有圈人请求触发，只是圈出照片中的人
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONObject
     */
    public JSONObject addTag(long photo_id, long owner_id, int photo_width, int photo_height,
                             int top, int left, int frame_width, int frame_height,
                             String tagged_user_name,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "photos.tag");
        params.put("photo_id", String.valueOf(photo_id));
        params.put("owner_id", String.valueOf(owner_id));
        params.put("photo_width", String.valueOf(photo_width));
        params.put("photo_height", String.valueOf(photo_height));
        params.put("top", String.valueOf(top));
        params.put("left", String.valueOf(left));
        params.put("frame_width", String.valueOf(frame_width));
        params.put("frame_height", String.valueOf(frame_height));
        params.put("tagged_user_name", tagged_user_name);
        return this.getResultJSONObject(params);
    }

    /**
     * 以session_key \ access_token对应用户的身份获取照片中圈出的好友或人<br/>
     * 注意：此方法需要用户授予 read_user_photo 权限(在OAuth2.0授权中由scope参数指定)
     * @param photo_id 照片的ID
     * @param owner_id 照片所有者的ID
     * @param page 页码   必须大于0，无上限 
     * @param count 每页个数 必须大于0，小于200
    * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return JSONObject
     */
    public JSONObject getTags(long photo_id, long owner_id, int page, int count,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "photos.getTags");
        params.put("photo_id", String.valueOf(photo_id));
        params.put("owner_id", String.valueOf(owner_id));
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        return this.getResultJSONObject(params);
    }

    /**
     * 接受圈人请求，此API会触发一个圈人新鲜事<br/>
     * 注意：此方法需要用户授予 deal_request 权限(在OAuth2.0授权中由scope参数指定)
     * @param tag_id 圈人（Tag）的ID，此ID在调用addTag方法时返回
     * @param photo_owner_id 圈人（Tag）所在照片所有者的ID
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return int
     */
    public int acceptTag(long tag_id, long photo_owner_id,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "photos.acceptTag");
        params.put("tag_id", String.valueOf(tag_id));
        params.put("photo_owner_id", String.valueOf(photo_owner_id));
        return this.getResultInt(params);
    }

    /**
     * 拒绝接受圈人请求，此API会删除圈人请求<br/>
     * 注意：此方法需要用户授予 deal_request 权限(在OAuth2.0授权中由scope参数指定)
     * @param tag_id 圈人（Tag）的ID，此ID在调用addTag方法时返回
     * @param photo_owner_id 圈人（Tag）所在照片所有者的ID
     * @param auth Auth接口类型，表示accessToken或sessionKey，传入如下值：
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return int
     */
    public int refuseTag(long tag_id, long photo_owner_id,Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "photos.refuseTag");
        params.put("tag_id", String.valueOf(tag_id));
        params.put("photo_owner_id", String.valueOf(photo_owner_id));
        return this.getResultInt(params);
    }
}
