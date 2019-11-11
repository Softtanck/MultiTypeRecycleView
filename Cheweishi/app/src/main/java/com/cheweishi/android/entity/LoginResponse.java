package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.io.Serializable;

/**
 * Created by tangce on 3/23/2016.
 */
public class LoginResponse extends BaseResponse implements Serializable{

    /**
     * signature : null
     * nickName : null
     * photo : null
     * id : 1
     * userName : 15892999216
     * defaultVehicle : 一汽大众-奥迪A3
     */

    private int id;

    private LoginUserInfoResponse msg;

    public LoginUserInfoResponse getMsg() {
        return msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMsg(LoginUserInfoResponse msg) {
        this.msg = msg;
    }


}
