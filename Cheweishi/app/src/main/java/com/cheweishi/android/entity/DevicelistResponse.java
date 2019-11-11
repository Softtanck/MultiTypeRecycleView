package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 6/19/2016.
 */
public class DevicelistResponse extends BaseResponse {


    /**
     * msg : [{"tenantName":"大是大非汽修","deviceNo":"14254454554"},{"tenantName":"大是大非汽修","deviceNo":"8856000542"}]
     * page : null
     */

    private Object page;
    /**
     * tenantName : 大是大非汽修
     * deviceNo : 14254454554
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
        private String tenantName;
        private String deviceNo;

        public String getTenantName() {
            return tenantName;
        }

        public void setTenantName(String tenantName) {
            this.tenantName = tenantName;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }
    }
}
