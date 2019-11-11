package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 8/17/2016.
 */
public class PreparePayCouponResponse extends BaseResponse {


    /**
     * existWashing : false
     * redPacketAmount : 0
     * couponList : [{"id":18,"overDueTime":"2016-11-01","coupon":{"amount":100,"type":"COMMON","remark":"租户绑定优惠券"}},{"id":19,"overDueTime":"2016-11-01","coupon":{"amount":100,"type":"COMMON","remark":"租户绑定优惠券"}},{"id":20,"overDueTime":"2016-11-01","coupon":{"amount":100,"type":"COMMON","remark":"租户绑定优惠券"}},{"id":53,"overDueTime":"2016-11-01","coupon":{"amount":2,"type":"COMMON","remark":"好东西大家一起领."}},{"id":74,"overDueTime":"2016-11-12","coupon":{"amount":34,"type":"SPECIFY","remark":"<p class=\"detailPic\" style=\"text-indent:28px;font-size:14px;text-align:center;color:#2B2B2B;font-family:simsun, arial, helvetica, clean, sans-serif;background-color:#FFFFFF;\">\r\n\t<img src=\"http://p3.ifengimg.com/a/2016_25/fcfe77425e1cbbf_size41_w500_h692.jpg\" width=\"500\" height=\"692\" alt=\"\" style=\"height:auto;\" /> \r\n<\/p>\r\n<p class=\"picIntro\" style=\"font-size:14px;text-align:center;font-family:楷体_gb2312, 楷体;color:#2B2B2B;background-color:#FFFFFF;\">\r\n\t马加特\r\n<\/p>\r\n<p style=\"text-indent:28px;font-size:14px;text-align:justify;color:#2B2B2B;font-family:simsun, arial, helvetica, clean, sans-serif;background-color:#FFFFFF;\">\r\n\t6月14日中午，山东鲁能在俱乐部会议室举行新闻发布会，正式宣布德国人马加特出任山东鲁能主教练。马加特是著名的德国教练员，青年时候，就曾是西德国家队队员，退役之后，先后执教过德国多家足球俱乐部，训练以\u201c魔鬼训练\u201d著名，也有\u201c救火\u201d教练的美名。在新闻发布会上，马加特说，对于上场山东鲁能与河北华夏的比赛，<strong>他认为，后防线存在问题，特别是对方第二个进球，他感觉问题很大，当时，他不免笑了起来，太不可思议了。<\/strong>对于山东鲁能队员是否适应他的训练法。他说，德国人适应，中国队员同样感觉不是问题，山东鲁能队员不会对魔鬼训练胆怯。他还说，他会将德国足球的严谨，进取带给山东鲁能的队员们。\r\n<\/p>\r\n<p style=\"text-indent:28px;font-size:14px;text-align:justify;color:#2B2B2B;font-family:simsun, arial, helvetica, clean, sans-serif;background-color:#FFFFFF;\">\r\n\t齐鲁晚报记者admin1<span class=\"ifengLogo\"><a href=\"http://www.ifeng.com/\" target=\"_blank\"><img src=\"http://y2.ifengimg.com/a/2015/0708/icon_logo.gif\" style=\"height:auto;\" /><\/a><\/span> \r\n<\/p>"}},{"id":75,"overDueTime":"2016-11-12","coupon":{"amount":34,"type":"SPECIFY","remark":"租户服务红包"}},{"id":54,"overDueTime":"2016-11-19","coupon":{"amount":999,"type":"SPECIFY","remark":"可以随便用."}}]
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private boolean existWashing;
        private String redPacketAmount;
        /**
         * id : 18
         * overDueTime : 2016-11-01
         * coupon : {"amount":100,"type":"COMMON","remark":"租户绑定优惠券"}
         */

        private List<CouponListBean> couponList;

        public boolean isExistWashing() {
            return existWashing;
        }

        public void setExistWashing(boolean existWashing) {
            this.existWashing = existWashing;
        }

        public String getRedPacketAmount() {
            return redPacketAmount;
        }

        public void setRedPacketAmount(String redPacketAmount) {
            this.redPacketAmount = redPacketAmount;
        }

        public List<CouponListBean> getCouponList() {
            return couponList;
        }

        public void setCouponList(List<CouponListBean> couponList) {
            this.couponList = couponList;
        }

        public static class CouponListBean {
            private int id;
            private String overDueTime;
            /**
             * amount : 100
             * type : COMMON
             * remark : 租户绑定优惠券
             */

            private CouponBean coupon;
            private boolean isCheck;

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public static class CouponBean {
                private String amount;
                private String type;
                private String remark;

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

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }
            }
        }
    }
}
