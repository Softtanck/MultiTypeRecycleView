package com.cheweishi.android.response;

import java.io.Serializable;

/**
 * Created by tangce on 3/23/2016.
 */
public class BaseResponse implements Serializable {

    protected String code;

    protected String desc;

    protected String token;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
