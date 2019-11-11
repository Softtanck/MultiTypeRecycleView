package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 5/13/2016.
 */
public class ActivityCouponResponse extends BaseResponse{


    /**
     * pageSize : 10
     * total : 10
     * pageNumber : 1
     */

    private PageBean page;
    /**
     * id : 5
     * remainNum : 0
     * overDueTime : null
     * amount : 10
     * isGet : false
     * type : COMMON
     * remark : Gggg
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
        private int pageSize;
        private int total;
        private int pageNumber;

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

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
    }

    public static class MsgBean {
        private int id;
        private int remainNum;
        private String deadlineTime;
        private String amount;
        private boolean isGet;
        private String type;
        private String remark;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRemainNum() {
            return remainNum;
        }

        public void setRemainNum(int remainNum) {
            this.remainNum = remainNum;
        }

        public String getDeadlineTime() {
            return deadlineTime;
        }

        public void setDeadlineTime(String deadlineTime) {
            this.deadlineTime = deadlineTime;
        }

        public boolean isGet() {
            return isGet;
        }

        public void setGet(boolean get) {
            isGet = get;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public boolean isIsGet() {
            return isGet;
        }

        public void setIsGet(boolean isGet) {
            this.isGet = isGet;
        }

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
    }
}
