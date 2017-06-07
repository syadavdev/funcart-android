package com.example.funcart.dataClass;

/**
 * Created by ni11 on 6/7/17.
 */

public class CartItemData {

    private int itemId;
    private String name;
    private int itemQty;
    private String picName;
    private double totalPrice;

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getItemId() {
        return itemId;
    }
    public String getName() {
        return name;
    }
    public int getItemQty() {
        return itemQty;
    }
    public String getPicName() {
        return picName;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
}
