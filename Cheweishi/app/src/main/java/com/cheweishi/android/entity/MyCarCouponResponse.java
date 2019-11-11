package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 6/15/2016.
 */
public class MyCarCouponResponse extends BaseResponse {


    /**
     * msg : [{"carWashingCoupon":{"tenantName":"顺通汽修","remark":"洗车券用于洗车","couponName":"洗车券"},"remainNum":99,"id":1}]
     * page : null
     */

    private Object page;
    /**
     * carWashingCoupon : {"tenantName":"顺通汽修","remark":"洗车券用于洗车","couponName":"洗车券"}
     * remainNum : 99
     * id : 1
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
        /**
         * tenantName : 顺通汽修
         * remark : 洗车券用于洗车
         * couponName : 洗车券
         */

        private CarWashingCouponBean carWashingCoupon;
        private int remainNum;
        private int id;

        public CarWashingCouponBean getCarWashingCoupon() {
            return carWashingCoupon;
        }

        public void setCarWashingCoupon(CarWashingCouponBean carWashingCoupon) {
            this.carWashingCoupon = carWashingCoupon;
        }

        public int getRemainNum() {
            return remainNum;
        }

        public void setRemainNum(int remainNum) {
            this.remainNum = remainNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public static class CarWashingCouponBean {
            private String tenantName;
            private String remark;
            private String couponName;

            public String getTenantName() {
                return tenantName;
            }

            public void setTenantName(String tenantName) {
                this.tenantName = tenantName;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getCouponName() {
                return couponName;
            }

            public void setCouponName(String couponName) {
                this.couponName = couponName;
            }
        }
    }
}
