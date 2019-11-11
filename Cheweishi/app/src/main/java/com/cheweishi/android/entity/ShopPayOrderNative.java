package com.cheweishi.android.entity;

import java.io.Serializable;

/**
 * Created by tangce on 8/29/2016.
 */
public class ShopPayOrderNative implements Serializable {

    private String icon;

    private String name;

    private String number;

    private String money;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
