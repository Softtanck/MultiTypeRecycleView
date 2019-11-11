package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 6/18/2016.
 */
public class PurseIntegralResponse extends BaseResponse {


    /**
     * pageSize : 5
     * total : 1
     * pageNumber : 1
     */

    private PageBean page;
    /**
     * id : 8
     * money : 0
     * score : 20
     * balanceType : INCOME
     * createDate : 1466159844000
     * remark : 购买服务赠送积分
     * redPacket : 0
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
        private String money;
        private String score;
        private String balanceType;
        private long createDate;
        private String remark;
        private String redPacket;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getBalanceType() {
            return balanceType;
        }

        public void setBalanceType(String balanceType) {
            this.balanceType = balanceType;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRedPacket() {
            return redPacket;
        }

        public void setRedPacket(String redPacket) {
            this.redPacket = redPacket;
        }
    }
}
