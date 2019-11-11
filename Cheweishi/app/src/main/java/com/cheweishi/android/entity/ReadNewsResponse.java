package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

/**
 * Created by tangce on 7/21/2016.
 */
public class ReadNewsResponse extends BaseResponse {

    /**
     * contentUrl : <div>ererererer</div>
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private String contentUrl;
        private String title;
        private String readCounts;
        private String likeCounts;
        private String commentCounts;

        public String getCommentCounts() {
            return commentCounts;
        }

        public void setCommentCounts(String commentCounts) {
            this.commentCounts = commentCounts;
        }

        public String getReadCounts() {
            return readCounts;
        }

        public void setReadCounts(String readCounts) {
            this.readCounts = readCounts;
        }

        public String getLikeCounts() {
            return likeCounts;
        }

        public void setLikeCounts(String likeCounts) {
            this.likeCounts = likeCounts;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }
    }
}
