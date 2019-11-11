package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

/**
 * Created by tangce on 4/9/2016.
 */
public class UpLoadPhotoResponse extends BaseResponse {

    /**
     * signature : djdnndndhhhh
     * nickName : djdnndnd
     * photo : /upload\endUser\photo\src_ed011d91-6ed8-46d6-8c7b-42034850ee58.jpeg
     * id : 4
     * userName : 18380473706
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private String signature;
        private String nickName;
        private String photo;
        private int id;
        private String userName;

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
