package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 3/27/2016.
 */
public class MessageResponse extends BaseResponse {


    private int id;

    /**
     * total : 6
     * pageNumber : 1
     * pageSize : 8
     */

    private PageBean page;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * messageType : PERSONALMSG
     * isRead : false
     * messageTitle : 车辆异常告警
     * id : 5
     * messageContent : 警告：您的爱车川ZG3626于2016-01-18 10:17:40发生【异常震动】，点击查看详情！
     * createDate : 1450860985000
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
        private int id;
        private int total;
        private int pageNumber;
        private int pageSize;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

    public static class MsgBean {
        private String messageType;
        private boolean isRead;
        private String messageTitle;
        private int id;
        private String messageContent;
        private long createDate;

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public boolean isIsRead() {
            return isRead;
        }

        public void setIsRead(boolean isRead) {
            this.isRead = isRead;
        }

        public String getMessageTitle() {
            return messageTitle;
        }

        public void setMessageTitle(String messageTitle) {
            this.messageTitle = messageTitle;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMessageContent() {
            return messageContent;
        }

        public void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }
    }
}
