package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

/**
 * Created by tangce on 3/31/2016.
 */
public class OrderDetailResponse extends BaseResponse {


    /**
     * id : 74
     * paymentDate : 1460556149000
     * tenantInfo : {"id":1,"contactPhone":null,"address":null,"businessTime":null,"praiseRate":3,"longitude":10.000021,"latitude":10.000021,"photo":"/upload/tenant/photo/12.jpg","tenantName":"爱车"}
     * price : 30
     * recordNo : 201604132202281239410001
     * chargeStatus : PAID
     * finishDate : null
     * serviceFlag : 0
     * createDate : 1460556148000
     * subscribeDate : null
     * serviceName : 精洗
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
        private long paymentDate;
        /**
         * id : 1
         * contactPhone : null
         * address : null
         * businessTime : null
         * praiseRate : 3
         * longitude : 10.000021
         * latitude : 10.000021
         * photo : /upload/tenant/photo/12.jpg
         * tenantName : 爱车
         */

        private TenantInfoBean tenantInfo;
        private double price;
        private String recordNo;
        private String chargeStatus;
        private long finishDate;
        private int serviceFlag;
        private long createDate;
        private long subscribeDate;
        private String serviceName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(long paymentDate) {
            this.paymentDate = paymentDate;
        }

        public TenantInfoBean getTenantInfo() {
            return tenantInfo;
        }

        public void setTenantInfo(TenantInfoBean tenantInfo) {
            this.tenantInfo = tenantInfo;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getRecordNo() {
            return recordNo;
        }

        public void setRecordNo(String recordNo) {
            this.recordNo = recordNo;
        }

        public String getChargeStatus() {
            return chargeStatus;
        }

        public void setChargeStatus(String chargeStatus) {
            this.chargeStatus = chargeStatus;
        }

        public long getFinishDate() {
            return finishDate;
        }

        public void setFinishDate(long finishDate) {
            this.finishDate = finishDate;
        }

        public int getServiceFlag() {
            return serviceFlag;
        }

        public void setServiceFlag(int serviceFlag) {
            this.serviceFlag = serviceFlag;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public long getSubscribeDate() {
            return subscribeDate;
        }

        public void setSubscribeDate(long subscribeDate) {
            this.subscribeDate = subscribeDate;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public static class TenantInfoBean {
            private int id;
            private String contactPhone;
            private String address;
            private String businessTime;
            private int praiseRate;
            private double longitude;
            private double latitude;
            private String photo;
            private String tenantName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContactPhone() {
                return contactPhone;
            }

            public void setContactPhone(String contactPhone) {
                this.contactPhone = contactPhone;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getBusinessTime() {
                return businessTime;
            }

            public void setBusinessTime(String businessTime) {
                this.businessTime = businessTime;
            }

            public int getPraiseRate() {
                return praiseRate;
            }

            public void setPraiseRate(int praiseRate) {
                this.praiseRate = praiseRate;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public String getTenantName() {
                return tenantName;
            }

            public void setTenantName(String tenantName) {
                this.tenantName = tenantName;
            }
        }
    }
}
