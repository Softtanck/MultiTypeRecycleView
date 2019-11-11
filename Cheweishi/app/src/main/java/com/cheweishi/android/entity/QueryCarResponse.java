package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 3/27/2016.
 */
public class QueryCarResponse extends BaseResponse {

    /**
     * msg : [[{"code":"A","name":"一汽大众","id":2},{"code":"A","name":"阿斯顿马丁","id":7}],[{"code":"B","name":"保时捷","id":5}],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[{"code":"Z","name":"中顺","id":6}]]
     * page : null
     */

    private Object page;
    /**
     * code : A
     * name : 一汽大众
     * id : 2
     */

    private List<List<MsgBean>> msg;

    public Object getPage() {
        return page;
    }

    public void setPage(Object page) {
        this.page = page;
    }

    public List<List<MsgBean>> getMsg() {
        return msg;
    }

    public void setMsg(List<List<MsgBean>> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private String code;
        private String name;
        private int id;

        private String icon;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
