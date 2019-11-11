package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tangce on 3/25/2016.
 */
public class MyCarManagerResponse extends BaseResponse implements Serializable {

    /**
     * msg : [{"isDefault":true,"plate":"川A00AKJ","id":1,"vehicleFullBrand":"一汽大众-奥迪A3"}]
     * page : null
     */

    private Object page;
    /**
     * isDefault : true
     * plate : 川A00AKJ
     * id : 1
     * vehicleFullBrand : 一汽大众-奥迪A3
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
        private boolean isDefault;
        private String plate;
        private int id;
        private String vehicleNo;
        private String brandIcon;
        private String vehicleFullBrand;
        private String deviceNo;
        private String nextAnnualInspection;
        private String trafficInsuranceExpiration;
        private String lastMaintainMileage;
        private String commercialInsuranceExpiration;
        private String driveMileage;


        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

        public String getDriveMileage() {
            return driveMileage;
        }

        public void setDriveMileage(String driveMileage) {
            this.driveMileage = driveMileage;
        }

        public String getCommercialInsuranceExpiration() {
            return commercialInsuranceExpiration;
        }

        public void setCommercialInsuranceExpiration(String commercialInsuranceExpiration) {
            this.commercialInsuranceExpiration = commercialInsuranceExpiration;
        }

        public String getLastMaintainMileage() {
            return lastMaintainMileage;
        }

        public void setLastMaintainMileage(String lastMaintainMileage) {
            this.lastMaintainMileage = lastMaintainMileage;
        }

        public String getTrafficInsuranceExpiration() {
            return trafficInsuranceExpiration;
        }

        public void setTrafficInsuranceExpiration(String trafficInsuranceExpiration) {
            this.trafficInsuranceExpiration = trafficInsuranceExpiration;
        }

        public String getNextAnnualInspection() {
            return nextAnnualInspection;
        }

        public void setNextAnnualInspection(String nextAnnualInspection) {
            this.nextAnnualInspection = nextAnnualInspection;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public boolean isDefault() {
            return isDefault;
        }

        public void setDefault(boolean aDefault) {
            isDefault = aDefault;
        }

        public String getBrandIcon() {
            return brandIcon;
        }

        public void setBrandIcon(String brandIcon) {
            this.brandIcon = brandIcon;
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

        public String getVehicleFullBrand() {
            return vehicleFullBrand;
        }

        public void setVehicleFullBrand(String vehicleFullBrand) {
            this.vehicleFullBrand = vehicleFullBrand;
        }
    }
}
