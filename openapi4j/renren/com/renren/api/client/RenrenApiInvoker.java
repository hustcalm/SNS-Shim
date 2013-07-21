package com.renren.api.client;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.renren.api.client.services.RenrenApiException;
import com.renren.api.client.utils.HttpURLUtils;
import com.renren.api.client.utils.Md5Utils;




public class RenrenApiInvoker {
    
    public static final String FORMAT_JSON="JSON";
    
//    private String token;
//    
//    private boolean isAccessToken;
    
    
//    //以session_key的方式获得权限
//    public RenrenApiInvoker(String sessionKey){
//        this.token=sessionKey;
//        this.isAccessToken=false;
//    }
//    
//    //以access_token的方式获得权限
//    public RenrenApiInvoker(String token,boolean flag){
//        this.token=token;
//        this.isAccessToken=flag;
//    }
//    
//    public String getToken() {
//        return token;
//    }
//
//    public boolean isAccessToken() {
//        return this.isAccessToken;
//    }
    
    //sdk对外只提供json方式
    public String sendPostRestRequest(TreeMap<String, String> params) {
        return sendPostRestRequest(params, FORMAT_JSON);
    }
    
    
    private String sendPostRestRequest(TreeMap<String, String> params, String format) {
        return sendPostRestRequest(params, format, RenrenApiConfig.renrenApiUrl);
    }

    private String sendPostRestRequest(TreeMap<String, String> params, String format, String url) {
        prepareParams(params, format);
        String content = HttpURLUtils.doPost(url, params);
        if (content.indexOf("error_code") >= 0) {
            throw this.parseRenrenApiException(content);
        }
        return content;
    }
    
    //sdk对外提供上传照片
    public String uploadPhoto(TreeMap<String, String> parameters, String filename,
            String contentType, byte[] data) {
        prepareParams(parameters, FORMAT_JSON);
        String content = HttpURLUtils.doUploadFile(RenrenApiConfig.renrenApiUrl, parameters,
                "upload", filename, contentType, data);
        if (content.indexOf("error_code") >= 0) {
            throw this.parseRenrenApiException(content);
        }
        return content;
    }

    private TreeMap<String, String> prepareParams(TreeMap<String, String> params, String format) {
        params.put("v", RenrenApiConfig.renrenApiVersion);
        params.put("call_id", String.valueOf(System.currentTimeMillis()));
        params.put("format", format);
        params.put("api_key", RenrenApiConfig.renrenApiKey);
//        if (this.token != null && this.token.length() > 0) {
//            if (this.isAccessToken) {
//                params.put("access_token", this.token);
//            } else {
//                params.put("session_key", this.token);
//            }
//        }

        return sigParams(params);
    }

    private TreeMap<String, String> sigParams(TreeMap<String, String> params) {
        StringBuffer sb = new StringBuffer();
        for (Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator(); iterator
                .hasNext();) {
            Map.Entry<String, String> entry = iterator.next();
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        sb.append(RenrenApiConfig.renrenApiSecret);
        params.put("sig", Md5Utils.md5(sb.toString()));
        return params;
    }

    private RenrenApiException parseRenrenApiException(String errorJson) {
        String errorFlag = "error_msg";
        int start = errorJson.indexOf(errorFlag);
        start = start + errorFlag.length() + 3;// ":"3个字符
        String errorMsg = errorJson.substring(start);
        int end = errorMsg.indexOf("\"");
        errorMsg = errorMsg.substring(0, end);

        errorFlag = "error_code";
        start = errorJson.indexOf(errorFlag);
        start = start + errorFlag.length() + 2;// ":2个字符
        String errorCode = errorJson.substring(start);
        end = errorCode.indexOf(",");
        errorCode = errorCode.substring(0, end).trim();
        RenrenApiException exception = new RenrenApiException(Integer.parseInt(errorCode), errorMsg);
        return exception;
    }
    
}
