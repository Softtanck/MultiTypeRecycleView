package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 3/27/2016.
 */
public class MaintainResponse extends BaseResponse {


    /**
     * id : 1
     * carServices : [{"subServices":[{"id":1,"promotionPrice":30,"serviceName":"车身保养","price":20},{"id":10,"promotionPrice":30,"serviceName":"轮胎保养","price":99}],"categoryName":"保养"}]
     * contactPhone : null
     * address : null
     * businessTime : null
     * longitude : 10.000021
     * latitude : 10.000021
     * photo : http://10.50.40.56:8081/csh-interface/upload/Koala.jpg
     * tenantName : 爱车
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
        private Object contactPhone;
        private Object address;
        private Object businessTime;
        private double longitude;
        private double latitude;
        private String photo;
        private String tenantName;
        /**
         * subServices : [{"id":1,"promotionPrice":30,"serviceName":"车身保养","price":20},{"id":10,"promotionPrice":30,"serviceName":"轮胎保养","price":99}]
         * categoryName : 保养
         */

        private List<CarServicesBean> carServices;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(Object contactPhone) {
            this.contactPhone = contactPhone;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public Object getBusinessTime() {
            return businessTime;
        }

        public void setBusinessTime(Object businessTime) {
            this.businessTime = businessTime;
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

        public List<CarServicesBean> getCarServices() {
            return carServices;
        }

        public void setCarServices(List<CarServicesBean> carServices) {
            this.carServices = carServices;
        }

        public static class CarServicesBean {
            private String categoryName;
            /**
             * id : 1
             * promotionPrice : 30
             * serviceName : 车身保养
             * price : 20
             */

            private List<SubServicesBean> subServices;

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public List<SubServicesBean> getSubServices() {
                return subServices;
            }

            public void setSubServices(List<SubServicesBean> subServices) {
                this.subServices = subServices;
            }

            public static class SubServicesBean {
                private int id;
                private int promotionPrice;
                private String serviceName;
                private int price;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getPromotionPrice() {
                    return promotionPrice;
                }

                public void setPromotionPrice(int promotionPrice) {
                    this.promotionPrice = promotionPrice;
                }

                public String getServiceName() {
                    return serviceName;
                }

                public void setServiceName(String serviceName) {
                    this.serviceName = serviceName;
                }

                public int getPrice() {
                    return price;
                }

                public void setPrice(int price) {
                    this.price = price;
                }
            }
        }
    }
}
