package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tanck on 10/11/2016.
 * <p/>
 * Describe:
 */
public class ShopOrderListResponse extends BaseResponse {

    /**
     * total : 8
     * pageNumber : 1
     * pageSize : 2
     */

    private PageBean page;
    /**
     * amount : 601
     * consignee : andrea
     * address : 天府软件园D区
     * orderItem : [{"thumbnail":null,"quantity":1,"price":601,"name":"熊猫座椅","id":11}]
     * freight : 0
     * orderStatus : unconfirmed
     * productCount : 1
     * shippingStatus : unshipped
     * phone : 15892999216
     * areaName : 四川省成都市
     * id : 9
     * sn : 20160825202
     * paymentStatus : unpaid
     * createDate : 1472123141000
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

    public static class MsgBean implements Cloneable, Serializable {
        /**
         * 支付宝
         * ALIPAY,
         * 微信
         * WECHAT,
         * 钱包余额
         * WALLET,
         */
        private String paymentType;//支付类型
        private int type = 0;//订单类型
        private int orderPosition;//订单位置
        //        private int realPosition;//订单真实位置
        private String amount;
        private String consignee;
        private String address;
        private int freight;
        private String orderStatus;
        private int productCount;
        private String shippingStatus;
        private String phone;
        private String areaName;
        private int id;
        private String sn;
        private String paymentStatus;
        private long createDate;

        @Override
        public Object clone() {
            try {
                ShopOrderListResponse.MsgBean msgBean = (MsgBean) super.clone();
                return msgBean;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }

//        public int getRealPosition() {
//            return realPosition;
//        }
//
//        public void setRealPosition(int realPosition) {
//            this.realPosition = realPosition;
//        }


        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public int getOrderPosition() {
            return orderPosition;
        }

        public void setOrderPosition(int orderPosition) {
            this.orderPosition = orderPosition;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        /**
         * thumbnail : null
         * quantity : 1
         * price : 601
         * name : 熊猫座椅
         * id : 11
         */


        private List<OrderItemBean> orderItem;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getFreight() {
            return freight;
        }

        public void setFreight(int freight) {
            this.freight = freight;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getProductCount() {
            return productCount;
        }

        public void setProductCount(int productCount) {
            this.productCount = productCount;
        }

        public String getShippingStatus() {
            return shippingStatus;
        }

        public void setShippingStatus(String shippingStatus) {
            this.shippingStatus = shippingStatus;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
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

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public List<OrderItemBean> getOrderItem() {
            return orderItem;
        }

        public void setOrderItem(List<OrderItemBean> orderItem) {
            this.orderItem = orderItem;
        }

        public static class OrderItemBean implements Serializable {
            private String thumbnail;
            private String quantity;
            private String price;
            private String name;
            private int id;
            private productBean product;

            public productBean getProduct() {
                return product;
            }

            public void setProduct(productBean product) {
                this.product = product;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
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

            public static class productBean implements Serializable {
                private int id;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }
            }
        }
    }
}
