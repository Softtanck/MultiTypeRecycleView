package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tangce on 3/24/2016.
 */
public class ServiceListResponse extends BaseResponse implements Serializable {


    /**
     * total : 1
     * pageNumber : 1
     * pageSize : 5
     */

    private PageBean page;
    /**
     * tenant_name : 环球汽修
     * praiseRate : 5
     * address : 高新区
     * contact_phone : 13621245412
     * distance : 0.5
     * latitude : 30.558516
     * photo : /upload/storePicture/2016-04-25/src_9bc4259f-327e-4d20-af6c-4d965576dff2.jpg
     * id : 6
     * washCarService : [{"price":1000,"service_id":5,"serviceName":"美女洗车","promotion_price":10},{"price":2,"service_id":23,"serviceName":"壮汉洗车","promotion_price":2}]
     * longitude : 104.077873
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

    public static class PageBean implements Serializable {
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

    public static class MsgBean implements Serializable {
        private String tenantName;
        private int praiseRate;
        private String address;
        private String contactPhone;
        private String distance;
        private double latitude;
        private String photo;
        private int id;
        private double longitude;
        /**
         * price : 1000
         * service_id : 5
         * serviceName : 美女洗车
         * promotion_price : 10
         */

        private List<WashCarServiceBean> carService;


        public String getTenantName() {
            return tenantName;
        }

        public void setTenantName(String tenantName) {
            this.tenantName = tenantName;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public int getPraiseRate() {
            return praiseRate;
        }

        public void setPraiseRate(int praiseRate) {
            this.praiseRate = praiseRate;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }


        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
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

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }


        public List<WashCarServiceBean> getCarService() {
            return carService;
        }

        public void setCarService(List<WashCarServiceBean> carService) {
            this.carService = carService;
        }

        public static class WashCarServiceBean implements Serializable {
            private double price;
            private int service_id;
            private String serviceName;
            private double promotion_price;
            private int categoryId;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getService_id() {
                return service_id;
            }

            public void setService_id(int service_id) {
                this.service_id = service_id;
            }

            public String getServiceName() {
                return serviceName;
            }

            public void setServiceName(String serviceName) {
                this.serviceName = serviceName;
            }

            public double getPromotion_price() {
                return promotion_price;
            }

            public void setPromotion_price(double promotion_price) {
                this.promotion_price = promotion_price;
            }
        }
    }
}
