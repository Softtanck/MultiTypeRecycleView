package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by Tanck on 10/25/2016.
 * <p>
 * Describe:
 */
public class UserCommentsResponse extends BaseResponse {

    /**
     * total : 3
     * pageNumber : 1
     * pageSize : 2
     */

    private PageBean page;
    /**
     * score : 21
     * bizReply : 商家回复
     * member : {"userName":"15892999216","photo":"/upload/endUser/photo/src_8e97aef2-cb1d-4e71-9d83-081b96ddfe28.jpeg"}
     * id : 1
     * content : 1111111111111111
     * createDate : 1461398979000
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
        private int score;
        private String bizReply;
        /**
         * userName : 15892999216
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

        public static class MemberBean {
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
