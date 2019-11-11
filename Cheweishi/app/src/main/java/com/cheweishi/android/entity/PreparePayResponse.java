package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

/**
 * Created by tangce on 4/5/2016.
 */
public class PreparePayResponse extends BaseResponse {


    /**
     * prepay_id : null
     * nonce_str : null
     * out_trade_no : 1232342323
     * sign : ererere
     * type : 2
     * channel : wx
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private String prepay_id;
        private String nonce_str;
        private String out_trade_no;
        private String sign;
        private String type;
        private String channel;
        private boolean isNeedPay;

        public boolean isNeedPay() {
            return isNeedPay;
        }

        public void setNeedPay(boolean needPay) {
            isNeedPay = needPay;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }
    }
}
