package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

/**
 * Created by tangce on 4/9/2016.
 */
public class PushMsgResponse {

    /**
     * 个人消息
     * PERSONALMSG,
     * 新闻消息
     * NEWSMSG,
     * 活动消息
     * PROMOTION
     * msgId : 1
     * content : 11111111
     * time : 1450860985000
     * title : 121212
     * unreadCount : 1
     */

    private String msgId;
    private String content;
    private Long time;
    private String title;
    private String unreadCount;

    private String newsId; // 新闻ID

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    private String type; //消息类型

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }
}
