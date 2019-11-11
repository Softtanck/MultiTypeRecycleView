package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 7/14/2016.
 */
public class StoreListResponse extends BaseResponse {

    /**
     * total : 3
     * pageNumber : 1
     * pageSize : 2
     */

    private PageBean page;
    /**
     * praiseRate : null
     * address : 三圣乡
     * rateCounts : null
     * tenantName : 顺通汽修
     * distance : null
     * carService : {"price":1000,"service_id":1,"serviceName":"模特洗车","promotion_price":1}
     * latitude : null
     * photo : null
     * id : 1
     * contactPhone : 13654874121
     * longitude : null
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
        private int praiseRate;
        private String address;
        private int rateCounts;
        private String tenantName;
        private String distance;
        /**
         * price : 1000
         * service_id : 1
         * serviceName : 模特洗车
         * promotion_price : 1
         */

        private CarServiceBean carService;
        private String latitude;
        private String photo;
        private int id;
        private String contactPhone;
        private String longitude;

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

        public int getRateCounts() {
            return rateCounts;
        }

        public void setRateCounts(int rateCounts) {
            this.rateCounts = rateCounts;
        }

        public String getTenantName() {
            return tenantName;
        }

        public void setTenantName(String tenantName) {
            this.tenantName = tenantName;
        }

        public Object getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public CarServiceBean getCarService() {
            return carService;
        }

        public void setCarService(CarServiceBean carService) {
            this.carService = carService;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
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

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public static class CarServiceBean {
            private String price;
            private int service_id;
            private String serviceName;
            private String promotion_price;

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
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

            public String getPromotion_price() {
                return promotion_price;
            }

            public void setPromotion_price(String promotion_price) {
                this.promotion_price = promotion_price;
            }
        }
    }
}
