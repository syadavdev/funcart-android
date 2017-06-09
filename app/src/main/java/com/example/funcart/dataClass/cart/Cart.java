package com.example.funcart.dataClass.cart;

import java.util.List;

public class Cart {
	
	private String email;
	private List<CartItem> itemDtoList;
	
	public List<CartItem> getItemDtoList() {
		return itemDtoList;
	}
	public void setItemDtoList(List<CartItem> itemDtoList) {
		this.itemDtoList = itemDtoList;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
