package com.renren.api.client;

import com.renren.api.client.services.AdminService;
import com.renren.api.client.services.BaseService;
import com.renren.api.client.services.BlogService;
import com.renren.api.client.services.FeedService;
import com.renren.api.client.services.FriendsService;
import com.renren.api.client.services.InvitationsService;
import com.renren.api.client.services.NotificationsService;
import com.renren.api.client.services.PageService;
import com.renren.api.client.services.PayService;
import com.renren.api.client.services.PhotoService;
import com.renren.api.client.services.StatusService;
import com.renren.api.client.services.UserService;

/**
 * 
 * @author Administrator
 *
 */
public class RenrenApiClient {
    private static RenrenApiClient instance = new RenrenApiClient();

    private RenrenApiInvoker       renrenApiInvoker;

    private FriendsService         friendsService;

    private UserService            userService;

    private AdminService           adminService;

    private InvitationsService     invitationsService;

    private NotificationsService   notificationsService;

    private PageService            pageService;

    private StatusService          statusService;

    private BlogService            blogService;

    private FeedService            feedService;

    private PayService             payService;

    private PhotoService           photoService;

    /**
     * 如果sessionKey为空，那么只能调用不需要sessionKey的接口。
     * 
     * @param sessionKey
     */
    //    public RenrenApiClient(String sessionKey) {
    //        this(sessionKey, false);
    //    }

    /**
     * 
     * @param token 访问标识
     * @param isAccessToken ture:token为accessToken, false:sessionKey
     */
    //    public RenrenApiClient(String token, boolean isAccessToken) {
    //        this.renrenApiInvoker = new RenrenApiInvoker(token, isAccessToken);
    //        this.checkConfig();
    //        this.initService();
    //    }

    private RenrenApiClient() {
        this.renrenApiInvoker = new RenrenApiInvoker();
        this.checkConfig();
        this.initService();
    }

    public static RenrenApiClient getInstance() {
        return instance;
    }

    /**
     * 检测配置
     */
    private void checkConfig() {
        String renrenApiKey = RenrenApiConfig.renrenApiKey;
        String renrenApiSecret = RenrenApiConfig.renrenApiSecret;
        if (renrenApiKey == null || renrenApiKey.length() < 1 || renrenApiSecret == null
            || renrenApiSecret.length() < 1) {
            throw new RuntimeException(
                "Please init com.renren.api.client.RenrenApiConfig.renrenApiKey and com.renren.api.client.RenrenApiConfig.renrenApiSecret!");
        }
    }

    /**
     * 初始化
     */
    private void initService() {
        this.friendsService = new FriendsService(this.renrenApiInvoker);
        this.userService = new UserService(this.renrenApiInvoker);
        this.adminService = new AdminService(this.renrenApiInvoker);
        this.invitationsService = new InvitationsService(this.renrenApiInvoker);
        this.notificationsService = new NotificationsService(this.renrenApiInvoker);
        this.pageService = new PageService(this.renrenApiInvoker);
        this.payService = new PayService(this.renrenApiInvoker);
        this.photoService = new PhotoService(this.renrenApiInvoker);
        this.feedService = new FeedService(this.renrenApiInvoker);
        this.blogService = new BlogService(this.renrenApiInvoker);
        this.statusService = new StatusService(this.renrenApiInvoker);
    }


    public BaseService getService(String clazz){
        BaseService baseService=null;
        try {
            baseService=(BaseService) Class.forName(clazz).newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return baseService;
    }
    public FriendsService getFriendsService() {
        return friendsService;
    }

    public UserService getUserService() {
        return userService;
    }

    public AdminService getAdminService() {
        return adminService;
    }

    public InvitationsService getInvitationsService() {
        return invitationsService;
    }

    public NotificationsService getNotificationsService() {
        return notificationsService;
    }

    public PageService getPageService() {
        return pageService;
    }

    public StatusService getStatusService() {
        return statusService;
    }

    public BlogService getBlogService() {
        return blogService;
    }

    public FeedService getFeedService() {
        return feedService;
    }

    public PayService getPayService() {
        return payService;
    }

    public PhotoService getPhotoService() {
        return photoService;
    }
}
