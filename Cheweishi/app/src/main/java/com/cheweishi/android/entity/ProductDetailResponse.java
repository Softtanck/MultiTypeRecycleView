package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 */
public class ProductDetailResponse extends BaseResponse implements Serializable{

    /**
     * image : null
     * productImages : [{"mobileicon":null,"id":10},{"mobileicon":null,"id":11}]
     * productParam : [{"name":"大小","value":"大"},{"name":"材质","value":"棉质"},{"name":"重量","value":"10kg"}]
     * reviews : [{"score":21,"bizReply":"商家回复","member":{"userName":"18696546911","photo":"/upload/endUser/photo/src_8e97aef2-cb1d-4e71-9d83-081b96ddfe28.jpeg"},"id":1,"content":"1111111111111111","createDate":1461398979000}]
     * reviewCount : 3
     * price : 601
     * cartProductCount : 0
     * fullName : 熊猫座椅AAA
     * id : 2
     * stock : 93
     * sales : null
     * introduction :
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean implements Serializable{
        private String image;
        private int reviewCount;
        private String price;
        private String cartProductCount;
        private String fullName;
        private int id;
        private int stock;
        private String sales;
        private String introduction;
        /**
         * mobileicon : null
         * id : 10
         */

        private List<ProductImagesBean> productImages;
        /**
         * name : 大小
         * value : 大
         */

        private List<ProductParamBean> productParam;
        /**
         * score : 21
         * bizReply : 商家回复
         * member : {"userName":"18696546911","photo":"/upload/endUser/photo/src_8e97aef2-cb1d-4e71-9d83-081b96ddfe28.jpeg"}
         * id : 1
         * content : 1111111111111111
         * createDate : 1461398979000
         */

        private List<ReviewsBean> reviews;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getReviewCount() {
            return reviewCount;
        }

        public void setReviewCount(int reviewCount) {
            this.reviewCount = reviewCount;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCartProductCount() {
            return cartProductCount;
        }

        public void setCartProductCount(String cartProductCount) {
            this.cartProductCount = cartProductCount;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public List<ProductImagesBean> getProductImages() {
            return productImages;
        }

        public void setProductImages(List<ProductImagesBean> productImages) {
            this.productImages = productImages;
        }

        public List<ProductParamBean> getProductParam() {
            return productParam;
        }

        public void setProductParam(List<ProductParamBean> productParam) {
            this.productParam = productParam;
        }

        public List<ReviewsBean> getReviews() {
            return reviews;
        }

        public void setReviews(List<ReviewsBean> reviews) {
            this.reviews = reviews;
        }

        public static class ProductImagesBean implements Serializable{
            private String mobileicon;
            private int id;

            public String getMobileicon() {
                return mobileicon;
            }

            public void setMobileicon(String mobileicon) {
                this.mobileicon = mobileicon;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        public static class ProductParamBean implements Serializable{
            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class ReviewsBean implements Serializable{
            private int score;
            private String bizReply;
            /**
             * userName : 18696546911
             * photo : /upload/endUser/photo/src_8e97aef2-cb1d-4e71-9d83-081b96ddfe28.jpeg
             */

            private MemberBean member;
            private int id;
            private String content;
            private long createDate;

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public String getBizReply() {
                return bizReply;
            }

            public void setBizReply(String bizReply) {
                this.bizReply = bizReply;
            }

            public MemberBean getMember() {
                return member;
            }

            public void setMember(MemberBean member) {
                this.member = member;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }

            public static class MemberBean implements Serializable{
                private String userName;
                private String photo;

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }

                public String getPhoto() {
                    return photo;
                }

                public void setPhoto(String photo) {
                    this.photo = photo;
                }
            }
        }
    }
}
