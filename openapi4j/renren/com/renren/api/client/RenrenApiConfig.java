package com.renren.api.client;

import com.renren.api.client.utils.ConfigUtil;

public class RenrenApiConfig {
    public static String renrenApiUrl     = ConfigUtil.getValue("renrenApiUrl");
    public static String renrenApiVersion = ConfigUtil.getValue("renrenApiVersion");
    public static String renrenApiKey     = ConfigUtil.getValue("renrenApiKey");
    public static String renrenApiSecret  = ConfigUtil.getValue("renrenApiSecret");
    public static String renrenAppID      = ConfigUtil.getValue("renrenAppID");
}
