package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 5/16/2016.
 */
public class PayCouponListResponse extends BaseResponse {


    /**
     * msg : [{"overDueTime":"2016-06-14","coupon":{"type":"COMMON","remark":"租户通用红包","amount":30},"id":4},{"overDueTime":"2016-08-12","coupon":{"type":"SPECIFY","remark":"租户服务红包","amount":34},"id":3}]
     * page : null
     */

    private Object page;
    /**
     * overDueTime : 2016-06-14
     * coupon : {"type":"COMMON","remark":"租户通用红包","amount":30}
     * id : 4
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
        private String overDueTime;
        /**
         * type : COMMON
         * remark : 租户通用红包
         * amount : 30
         */

        private CouponBean coupon;
        private int id;

        private boolean isCheck;

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getOverDueTime() {
            return overDueTime;
        }

        public void setOverDueTime(String overDueTime) {
            this.overDueTime = overDueTime;
        }

        public CouponBean getCoupon() {
            return coupon;
        }

        public void setCoupon(CouponBean coupon) {
            this.coupon = coupon;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public static class CouponBean {
            private String type;
            private String remark;
            private String amount;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }
        }
    }
}
