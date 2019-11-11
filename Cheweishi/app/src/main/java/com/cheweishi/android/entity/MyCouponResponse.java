package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 5/17/2016.
 */
public class MyCouponResponse extends BaseResponse {

    /**
     * total : 2
     * pageNumber : 1
     * pageSize : 5
     */

    private PageBean page;
    /**
     * isOverDue : false
     * overDueTime : 2016-08-12
     * coupon : {"remark":"租户服务红包","amount":34,"type":"COMMON"}
     * id : 3
     * isUsed : false
     */

    private List<MsgBean> msg;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class PageBean {
        private int total;
        private int pageNumber;
        private int pageSize;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

    public static class MsgBean {
        private boolean isOverDue;
        private String overDueTime;
        /**
         * remark : 租户服务红包
         * amount : 34
         * type : COMMON
         */

        private CouponBean coupon;
        private int id;
        private boolean isUsed;

        public boolean isIsOverDue() {
            return isOverDue;
        }

        public void setIsOverDue(boolean isOverDue) {
            this.isOverDue = isOverDue;
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

        public boolean isIsUsed() {
            return isUsed;
        }

        public void setIsUsed(boolean isUsed) {
            this.isUsed = isUsed;
        }

        public static class CouponBean {
            private String remark;
            private String amount;
            private String type;

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

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
