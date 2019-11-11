package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tangce on 3/27/2016.
 */
public class OrderResponse extends BaseResponse implements Serializable {


    /**
     * pageSize : 10
     * total : 37
     * pageNumber : 1
     */

    private PageBean page;
    /**
     * id : 63
     * paymentDate : 1460362894000
     * tenantID : 3
     * price : 30
     * chargeStatus : PAID
     * tenantPhoto : /upload/tenant/photo/2.jpg
     * carService : {"id":3,"serviceCategory":{"createDate":1458524450000,"id":2,"categoryName":"洗车","modifyDate":1458524451000},"serviceName":"普通洗车"}
     * createDate : 1460362894000
     * tenantEvaluate : {"createDate":1460425066000,"id":10,"modifyDate":1460425066000}
     * tenantName : 中和汽修
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
        private int pageSize;
        private int total;
        private int pageNumber;

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

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
    }

    public static class MsgBean implements Serializable {
        private int id;
        private long paymentDate;
        private String tenantID;
        private double price;
        private String chargeStatus;
        private String tenantPhoto;
        /**
         * id : 3
         * serviceCategory : {"createDate":1458524450000,"id":2,"categoryName":"洗车","modifyDate":1458524451000}
         * serviceName : 普通洗车
         */

        private CarServiceBean carService;
        private long createDate;
        /**
         * createDate : 1460425066000
         * id : 10
         * modifyDate : 1460425066000
         */

        private TenantEvaluateBean tenantEvaluate;
        private String tenantName;

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

        public String getTenantID() {
            return tenantID;
        }

        public void setTenantID(String tenantID) {
            this.tenantID = tenantID;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getChargeStatus() {
            return chargeStatus;
        }

        public void setChargeStatus(String chargeStatus) {
            this.chargeStatus = chargeStatus;
        }

        public String getTenantPhoto() {
            return tenantPhoto;
        }

        public void setTenantPhoto(String tenantPhoto) {
            this.tenantPhoto = tenantPhoto;
        }

        public CarServiceBean getCarService() {
            return carService;
        }

        public void setCarService(CarServiceBean carService) {
            this.carService = carService;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public TenantEvaluateBean getTenantEvaluate() {
            return tenantEvaluate;
        }

        public void setTenantEvaluate(TenantEvaluateBean tenantEvaluate) {
            this.tenantEvaluate = tenantEvaluate;
        }

        public String getTenantName() {
            return tenantName;
        }

        public void setTenantName(String tenantName) {
            this.tenantName = tenantName;
        }

        public static class CarServiceBean implements Serializable {
            private int id;
            /**
             * createDate : 1458524450000
             * id : 2
             * categoryName : 洗车
             * modifyDate : 1458524451000
             */

            private ServiceCategoryBean serviceCategory;
            private String serviceName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public ServiceCategoryBean getServiceCategory() {
                return serviceCategory;
            }

            public void setServiceCategory(ServiceCategoryBean serviceCategory) {
                this.serviceCategory = serviceCategory;
            }

            public String getServiceName() {
                return serviceName;
            }

            public void setServiceName(String serviceName) {
                this.serviceName = serviceName;
            }

            public static class ServiceCategoryBean implements Serializable {
                private long createDate;
                private int id;
                private String categoryName;
                private long modifyDate;

                public long getCreateDate() {
                    return createDate;
                }

                public void setCreateDate(long createDate) {
                    this.createDate = createDate;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getCategoryName() {
                    return categoryName;
                }

                public void setCategoryName(String categoryName) {
                    this.categoryName = categoryName;
                }

                public long getModifyDate() {
                    return modifyDate;
                }

                public void setModifyDate(long modifyDate) {
                    this.modifyDate = modifyDate;
                }
            }
        }

        public static class TenantEvaluateBean implements Serializable {
            private long createDate;
            private int id;
            private long modifyDate;

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getModifyDate() {
                return modifyDate;
            }

            public void setModifyDate(long modifyDate) {
                this.modifyDate = modifyDate;
            }
        }
    }
}
