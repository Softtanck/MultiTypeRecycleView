package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

/**
 * Created by tangce on 4/6/2016.
 */
public class CarDetectionResponse extends BaseResponse {


    /**
     * averageSpeed : 11.443515
     * mileAge : 0
     * averageFuelConsumption : 3.2788703
     * cost : 0
     * runningTime : 0
     * fuelConsumption : 0
     * totalMileAge : 0
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private double averageSpeed;
        private float mileAge; // 今日里程
        private double averageFuelConsumption;
        private double cost;
        private int runningTime; // 今日行驶时间
        private double fuelConsumption;
        private float totalMileAge;

        private String emergencybrakecount; // 急刹车

        private String suddenturncount; // 急转弯次数

        private String rapidlyspeedupcount;// 急加速

        private String fatiguedrivingcount;//疲劳驾驶

        private String drivingScore;//行驶分数


        public String getEmergencybrakecount() {
            return emergencybrakecount;
        }

        public void setEmergencybrakecount(String emergencybrakecount) {
            this.emergencybrakecount = emergencybrakecount;
        }

        public String getSuddenturncount() {
            return suddenturncount;
        }

        public void setSuddenturncount(String suddenturncount) {
            this.suddenturncount = suddenturncount;
        }

        public String getRapidlyspeedupcount() {
            return rapidlyspeedupcount;
        }

        public void setRapidlyspeedupcount(String rapidlyspeedupcount) {
            this.rapidlyspeedupcount = rapidlyspeedupcount;
        }

        public String getFatiguedrivingcount() {
            return fatiguedrivingcount;
        }

        public void setFatiguedrivingcount(String fatiguedrivingcount) {
            this.fatiguedrivingcount = fatiguedrivingcount;
        }

        public String getDrivingScore() {
            return drivingScore;
        }

        public void setDrivingScore(String drivingScore) {
            this.drivingScore = drivingScore;
        }

        public double getAverageSpeed() {
            return averageSpeed;
        }

        public void setAverageSpeed(double averageSpeed) {
            this.averageSpeed = averageSpeed;
        }

        public float getMileAge() {
            return mileAge;
        }

        public void setMileAge(float mileAge) {
            this.mileAge = mileAge;
        }

        public double getAverageFuelConsumption() {
            return averageFuelConsumption;
        }

        public void setAverageFuelConsumption(double averageFuelConsumption) {
            this.averageFuelConsumption = averageFuelConsumption;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public int getRunningTime() {
            return runningTime;
        }

        public void setRunningTime(int runningTime) {
            this.runningTime = runningTime;
        }

        public double getFuelConsumption() {
            return fuelConsumption;
        }

        public void setFuelConsumption(double fuelConsumption) {
            this.fuelConsumption = fuelConsumption;
        }

        public float getTotalMileAge() {
            return totalMileAge;
        }

        public void setTotalMileAge(float totalMileAge) {
            this.totalMileAge = totalMileAge;
        }
    }
}
