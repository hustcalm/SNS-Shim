package com.renren.api.client.param.impl;

import com.renren.api.client.param.Auth;

public class AccessToken implements Auth {
    private String key="access_token";
    private String value;
    public AccessToken(String value){
        this.value=value;
    }
    public String getKey() {
        // TODO Auto-generated method stub
        return key;
    }

    public String getValue() {
        // TODO Auto-generated method stub
        return value;
    }

}
