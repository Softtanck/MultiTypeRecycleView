package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 8/26/2016.
 */
public class BuyCartListResponse extends BaseResponse {

    /**
     * total : 2
     * pageNumber : 1
     * pageSize : 4
     */

    private PageBean page;
    /**
     * product : {"fullName":"太空椅AAAAA","image":null,"id":3,"price":501}
     * quantity : 1
     * id : 2
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
        /**
         * fullName : 太空椅AAAAA
         * image : null
         * id : 3
         * price : 501
         */

        private ProductBean product;
        private String quantity;
        private int id;
        private boolean isChecked = true;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public ProductBean getProduct() {
            return product;
        }

        public void setProduct(ProductBean product) {
            this.product = product;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public static class ProductBean {
            private String fullName;
            private String image;
            private int id;
            private String price;

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }
}
