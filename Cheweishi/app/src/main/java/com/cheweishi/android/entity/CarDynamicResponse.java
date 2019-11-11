package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

/**
 * Created by tangce on 4/7/2016.
 */
public class CarDynamicResponse extends BaseResponse {


    /**
     * lon : 104.0637
     * averageOil : null
     * speed : null
     * azimuth : 348
     * engineRuntime : null
     * mileAge : null
     * acc : null
     * lat : 30.6338
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private double lng;
        private String averageOil;
        private String speed;
        private int azimuth;
        private long engineRuntime;
        private String mileAge;
        private String acc;
        private double lat;
        private long createtime;

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getAverageOil() {
            return averageOil;
        }

        public void setAverageOil(String averageOil) {
            this.averageOil = averageOil;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public int getAzimuth() {
            return azimuth;
        }

        public void setAzimuth(int azimuth) {
            this.azimuth = azimuth;
        }

        public long getEngineRuntime() {
            return engineRuntime;
        }

        public void setEngineRuntime(long engineRuntime) {
            this.engineRuntime = engineRuntime;
        }

        public String getMileAge() {
            return mileAge;
        }

        public void setMileAge(String mileAge) {
            this.mileAge = mileAge;
        }

        public String getAcc() {
            return acc;
        }

        public void setAcc(String acc) {
            this.acc = acc;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }
}
