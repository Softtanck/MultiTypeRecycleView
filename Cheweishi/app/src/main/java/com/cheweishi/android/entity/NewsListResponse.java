package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 7/19/2016.
 */
public class NewsListResponse extends BaseResponse {

    /**
     * total : 2
     * pageNumber : 1
     * pageSize : 6
     */

    private PageBean page;
    /**
     * imgUrl : null
     * commentCounts : 33
     * likeCounts : 5
     * subTitle : null
     * modifyDate : 1461237584000
     * readCounts : 5
     * id : 2
     * title : 百度新闻
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
        private String imgUrl;
        private String commentCounts;
        private String likeCounts;
        private String subTitle;
        private long createDate;
        private String readCounts;
        private String id;
        private String title;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getCommentCounts() {
            return commentCounts;
        }

        public void setCommentCounts(String commentCounts) {
            this.commentCounts = commentCounts;
        }

        public String getLikeCounts() {
            return likeCounts;
        }

        public void setLikeCounts(String likeCounts) {
            this.likeCounts = likeCounts;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public long getModifyDate() {
            return createDate;
        }

        public void setModifyDate(long modifyDate) {
            this.createDate = modifyDate;
        }

        public String getReadCounts() {
            return readCounts;
        }

        public void setReadCounts(String readCounts) {
            this.readCounts = readCounts;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
