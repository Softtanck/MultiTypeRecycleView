package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tangce on 4/9/2016.
 */
public class PessanyResponse extends BaseResponse implements Serializable {


    /**
     * msg : [{"score":2,"lng":104.09914236362,"processingSite":"成都市公安局交通管理局第三分局","illegalContent":"机动车通过有灯控路口时，不按所需行进方向驶入导向车道的","finesAmount":100,"illegalAddress":"锦江大道与锦华路三段交叉路口","plate":"川A893VM","id":2,"illegalDate":"2016-01-29 12:53:28","lat":30.592107264059}]
     * page : null
     */

    private Object page;
    /**
     * score : 2
     * lng : 104.09914236362
     * processingSite : 成都市公安局交通管理局第三分局
     * illegalContent : 机动车通过有灯控路口时，不按所需行进方向驶入导向车道的
     * finesAmount : 100
     * illegalAddress : 锦江大道与锦华路三段交叉路口
     * plate : 川A893VM
     * id : 2
     * illegalDate : 2016-01-29 12:53:28
     * lat : 30.592107264059
     */

    private List<MsgBean> msg;

    public Object getPage() {
        return page;
    }

    public void setPage(Object page) {
        this.page = page;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean implements Serializable {
        private int score;
        private double lng;
        private String processingSite;
        private String illegalContent;
        private int finesAmount;
        private String illegalAddress;
        private String plate;
        private int id;
        private String illegalDate;
        private double lat;

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getProcessingSite() {
            return processingSite;
        }

        public void setProcessingSite(String processingSite) {
            this.processingSite = processingSite;
        }

        public String getIllegalContent() {
            return illegalContent;
        }

        public void setIllegalContent(String illegalContent) {
            this.illegalContent = illegalContent;
        }

        public int getFinesAmount() {
            return finesAmount;
        }

        public void setFinesAmount(int finesAmount) {
            this.finesAmount = finesAmount;
        }

        public String getIllegalAddress() {
            return illegalAddress;
        }

        public void setIllegalAddress(String illegalAddress) {
            this.illegalAddress = illegalAddress;
        }

        public String getPlate() {
            return plate;
        }

        public void setPlate(String plate) {
            this.plate = plate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIllegalDate() {
            return illegalDate;
        }

        public void setIllegalDate(String illegalDate) {
            this.illegalDate = illegalDate;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }
}
