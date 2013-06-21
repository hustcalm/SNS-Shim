package com.renren.api.client.param.impl;

import com.renren.api.client.param.Auth;

public class SessionKey implements Auth {
    private String key="session_key";
    private String value;
    public SessionKey(String value){
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
