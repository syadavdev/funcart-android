package com.example.funcart.order;

public class OrderItemDto {
	
	private String itemName;
	private int itemId;
	private int itemQty;
	private Double itemTotalPrice;
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
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
