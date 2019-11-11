package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 8/12/2016.
 */
public class ShopListResponse extends BaseResponse {

    /**
     * total : 3
     * pageNumber : 1
     * pageSize : 2
     */

    private PageBean page;
    /**
     * image : null
     * price : 200
     * name : 熊猫座椅
     * id : 2
     * sales : 20
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
        private String image;
        private String price;
        private String fullName;
        private int id;
        private String sales;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return fullName;
        }

        public void setName(String name) {
            this.fullName = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }
    }
}
