package com.cheweishi.android.entity;

import java.io.Serializable;

/**
 * Created by tangce on 3/24/2016.
 */
public class LoginUserInfoResponse implements Serializable {
    private String signature;
    private String nickName;
    private String photo;
    private String id;
    private String defaultDeviceNo;
    private String userName;
    private String defaultVehicle;
    private String defaultVehiclePlate;
    private String defaultVehicleId;
    private String defaultVehicleIcon;

    public LoginUserInfoResponse() {

    }

    public LoginUserInfoResponse(String signature, String nickName, String photo, String id, String defaultDeviceNo, String userName, String defaultVehicle, String defaultVehiclePlate, String defaultVehicleIcon, String defaultVehicleId) {
        this.signature = signature;
        this.nickName = nickName;
        this.photo = photo;
        this.id = id;
        this.defaultDeviceNo = defaultDeviceNo;
        this.userName = userName;
        this.defaultVehicle = defaultVehicle;
        this.defaultVehiclePlate = defaultVehiclePlate;
        this.defaultVehicleId = defaultVehicleId;
        this.defaultVehicleIcon = defaultVehicleIcon;
    }

    public String getDefaultVehicleId() {
        return defaultVehicleId;
    }

    public void setDefaultVehicleId(String defaultVehicleId) {
        this.defaultVehicleId = defaultVehicleId;
    }

    public String getDefaultDeviceNo() {
        return defaultDeviceNo;
    }

    public void setDefaultDeviceNo(String defaultDeviceNo) {
        this.defaultDeviceNo = defaultDeviceNo;
    }

    public String getDefaultVehicleIcon() {
        return defaultVehicleIcon;
    }

    public void setDefaultVehicleIcon(String defaultVehicleIcon) {
        this.defaultVehicleIcon = defaultVehicleIcon;
    }

    public String getDefaultVehiclePlate() {
        return defaultVehiclePlate;
    }

    public void setDefaultVehiclePlate(String defaultVehiclePlate) {
        this.defaultVehiclePlate = defaultVehiclePlate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDefaultVehicle() {
        return defaultVehicle;
    }

    public void setDefaultVehicle(String defaultVehicle) {
        this.defaultVehicle = defaultVehicle;
    }
}
