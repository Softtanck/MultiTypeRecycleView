package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tangce on 3/29/2016.
 */
public class ChargeResponse extends BaseResponse {


    /**
     * page : null
     * msg : [{"id":4,"money":10.4,"score":0,"balanceType":"OUTCOME","createDate":1458720243000,"remark":null,"redPacket":0},{"id":6,"money":90.3,"score":0,"balanceType":"OUTCOME","createDate":1458720243000,"remark":null,"redPacket":0},{"id":3,"money":55,"score":0,"balanceType":"INCOME","createDate":1458720030000,"remark":null,"redPacket":0},{"id":5,"money":66,"score":0,"balanceType":"INCOME","createDate":1458720030000,"remark":null,"redPacket":0}]
     */

    private PageBean page;

    /**
     * id : 4
     * money : 10.4
     * score : 0
     * balanceType : OUTCOME
     * createDate : 1458720243000
     * remark : null
     * redPacket : 0
     */

    public static class PageBean implements Serializable {
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

    public static class MsgBean {
        private int id;
        private double money;
        private int score;
        private String balanceType;
        private long createDate;
        private String remark;
        private int redPacket;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
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

        public int getRedPacket() {
            return redPacket;
        }

        public void setRedPacket(int redPacket) {
            this.redPacket = redPacket;
        }
    }
}
