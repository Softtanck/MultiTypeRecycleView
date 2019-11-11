package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by Tanck on 10/26/2016.
 * <p>
 * Describe:
 */
public class ReturnResponse extends BaseResponse {


    /**
     * total : 2
     * pageNumber : 1
     * pageSize : 5
     */

    private PageBean page;
    /**
     * deliveryCorp : null
     * trackingNo : null
     * returnsItem : [{"thumbnail":null,"quantity":1,"price":601,"name":"熊猫座椅","id":1}]
     * id : 2
     * sn : 20160826101
     * returnAmount : 601
     * returnsStatus : applyReturn
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
        private String paymentType;//支付类型
        private int type = 0;//订单类型
        private int orderPosition;//订单位置
        private String deliveryCorp;
        private String trackingNo;
        private int id;
        private String sn;
        private String returnAmount;
        private String returnsStatus;

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getOrderPosition() {
            return orderPosition;
        }

        public void setOrderPosition(int orderPosition) {
            this.orderPosition = orderPosition;
        }

        /**
         * thumbnail : null
         * quantity : 1
         * price : 601
         * name : 熊猫座椅
         * id : 1
         */



        private List<ReturnsItemBean> returnsItem;

        public String getDeliveryCorp() {
            return deliveryCorp;
        }

        public void setDeliveryCorp(String deliveryCorp) {
            this.deliveryCorp = deliveryCorp;
        }

        public String getTrackingNo() {
            return trackingNo;
        }

        public void setTrackingNo(String trackingNo) {
            this.trackingNo = trackingNo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getReturnAmount() {
            return returnAmount;
        }

        public void setReturnAmount(String returnAmount) {
            this.returnAmount = returnAmount;
        }

        public String getReturnsStatus() {
            return returnsStatus;
        }

        public void setReturnsStatus(String returnsStatus) {
            this.returnsStatus = returnsStatus;
        }

        public List<ReturnsItemBean> getReturnsItem() {
            return returnsItem;
        }

        public void setReturnsItem(List<ReturnsItemBean> returnsItem) {
            this.returnsItem = returnsItem;
        }

        public static class ReturnsItemBean {
            private String thumbnail;
            private int quantity;
            private String price;
            private String name;
            private int id;

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
