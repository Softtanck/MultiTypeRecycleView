package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

/**
 * Created by tangce on 3/27/2016.
 */
public class BalanceResponse extends BaseResponse {


    /**
     * id : 2
     * score : 0
     * balanceAmount : 0
     * giftAmount : 0
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private int id;
        private String score;
        private String balanceAmount;
        private String giftAmount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getBalanceAmount() {
            return balanceAmount;
        }

        public void setBalanceAmount(String balanceAmount) {
            this.balanceAmount = balanceAmount;
        }

        public String getGiftAmount() {
            return giftAmount;
        }

        public void setGiftAmount(String giftAmount) {
            this.giftAmount = giftAmount;
        }
    }
}
