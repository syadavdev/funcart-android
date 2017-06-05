package com.example.funcart.dataClass.cart;

public class CartItemDto {
	
	private String itemName;
	private String itemPicName;
	private int itemId;
	private int itemQty;
	private Double itemTotalPrice;
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemPicName() {
		return itemPicName;
	}
	public void setItemPicName(String itemPicName) {
		this.itemPicName = itemPicName;
	}
	public int getItemQty() {
		return itemQty;
	}
	public void setItemQty(int itemQty) {
		this.itemQty = itemQty;
	}
	public Double getItemTotalPrice() {
		return itemTotalPrice;
	}
	public void setItemTotalPrice(Double itemTotalPrice) {
		this.itemTotalPrice = itemTotalPrice;
	}
}