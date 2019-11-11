package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by Tanck on 9/2/2016.
 * <p>
 * Describe:
 */
public class AreaListResponse extends BaseResponse {


    /**
     * msg : [{"name":"澳门特别行政区","id":3314},{"name":"香港特别行政区","id":3292},{"name":"台湾省","id":3219},{"name":"新疆维吾尔自治区","id":3106},{"name":"北京市","id":1}]
     * page : null
     */

    private Object page;
    /**
     * name : 澳门特别行政区
     * id : 3314
     */

    private List<MsgBean> msg;

    public Object getPage() {
        return page;
    }

    public void setPage(Object page) {
        this.page = page;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private String name;
        private int id;

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
