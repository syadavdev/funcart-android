package com.example.funcart.dataClass;

/**
 * Created by mario on 30/05/17.
 */

public class ItemData{

    private int itemId;
    private String name;
    private String picName;
    private double price;

    public ItemData(int itemId, String name, String picName, double price) {
        this.itemId = itemId;
        this.name = name;
        this.picName = picName;
        this.price = price;
    }

    public int getItemId() {
        return itemId;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPicName() {
        return picName;
    }
    public void setPicName(String picName) {
        this.picName = picName;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
