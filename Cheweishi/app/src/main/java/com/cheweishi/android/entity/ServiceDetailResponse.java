package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/26.
 */
public class ServiceDetailResponse extends BaseResponse implements Serializable {


    /**
     * address : null
     * tenantName : 爱车
     * latitude : 10.000021
     * photo : http://10.50.40.56:8081/csh-interface/upload/Koala.jpg
     * id : 1
     * businessTime : null
     * contactPhone : null
     * longitude : 10.000021
     * carServices : [{"subServices":[{"promotionPrice":30,"price":20,"id":1,"serviceName":"车身保养"},{"promotionPrice":30,"price":99,"id":10,"serviceName":"轮胎保养"}],"categoryName":"保养"},{"subServices":[{"promotionPrice":30,"price":88,"id":9,"serviceName":"普通洗车"}],"categoryName":"洗车"}]
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean implements Serializable{


        private List<tenantImagesBean> tenantImages;
        private Object address;
        private String tenantName;
        private double latitude;
        private String photo;
        private int id;
        private Object businessTime;
        private Object contactPhone;
        private double longitude;
        /**
         * subServices : [{"promotionPrice":30,"price":20,"id":1,"serviceName":"车身保养"},{"promotionPrice":30,"price":99,"id":10,"serviceName":"轮胎保养"}]
         * categoryName : 保养
         */

        private List<CarServicesBean> carServices;

        public List<tenantImagesBean> getTenantImages() {
            return tenantImages;
        }

        public void setTenantImages(List<tenantImagesBean> tenantImages) {
            this.tenantImages = tenantImages;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public String getTenantName() {
            return tenantName;
        }

        public void setTenantName(String tenantName) {
            this.tenantName = tenantName;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getBusinessTime() {
            return businessTime;
        }

        public void setBusinessTime(Object businessTime) {
            this.businessTime = businessTime;
        }

        public Object getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(Object contactPhone) {
            this.contactPhone = contactPhone;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public List<CarServicesBean> getCarServices() {
            return carServices;
        }

        public void setCarServices(List<CarServicesBean> carServices) {
            this.carServices = carServices;
        }

        public static class tenantImagesBean implements Serializable{
            private int id;
            private String image;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }

        public static class CarServicesBean implements Serializable{
            private String categoryName;
            private int categoryId;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            /**
             * promotionPrice : 30
             * price : 20
             * id : 1
             * serviceName : 车身保养
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

            public static class SubServicesBean implements Serializable {
                private double promotionPrice;
                private double price;
                private int id;
                private String serviceName;

                private String serviceDesc;
                private String imgPath;

                public String getServiceDesc() {
                    return serviceDesc;
                }

                public void setServiceDesc(String serviceDesc) {
                    this.serviceDesc = serviceDesc;
                }

                public String getImgPath() {
                    return imgPath;
                }

                public void setImgPath(String imgPath) {
                    this.imgPath = imgPath;
                }

                public double getPromotionPrice() {
                    return promotionPrice;
                }

                public void setPromotionPrice(double promotionPrice) {
                    this.promotionPrice = promotionPrice;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getServiceName() {
                    return serviceName;
                }

                public void setServiceName(String serviceName) {
                    this.serviceName = serviceName;
                }
            }
        }
    }
}
