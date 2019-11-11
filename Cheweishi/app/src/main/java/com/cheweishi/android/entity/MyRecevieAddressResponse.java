package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tangce on 8/31/2016.
 */
public class MyRecevieAddressResponse extends BaseResponse implements Serializable {


    /**
     * pageSize : 10
     * total : 2
     * pageNumber : 1
     */

    private PageBean page;
    /**
     * id : 6
     * phone : 18380473706
     * area : {"id":3317}
     * areaName : 四川省成都市高新区
     * address : 软件园
     * isDefault : true
     * consignee : 唐笧
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

    public static class MsgBean implements Serializable{
        private int id;
        private String phone;
        /**
         * id : 3317
         */

        private AreaBean area;
        private String areaName;
        private String address;
        private boolean isDefault;
        private String consignee;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public AreaBean getArea() {
            return area;
        }

        public void setArea(AreaBean area) {
            this.area = area;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public boolean isIsDefault() {
            return isDefault;
        }

        public void setIsDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public static class AreaBean implements Serializable {
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
