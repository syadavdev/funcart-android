package com.example.funcart.dataClass.cart;

import java.util.List;

public class CartDto {
	
	private String email;
	private List<CartItemDto> itemDtoList;
	
	public List<CartItemDto> getItemDtoList() {
		return itemDtoList;
	}
	public void setItemDtoList(List<CartItemDto> itemDtoList) {
		this.itemDtoList = itemDtoList;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
