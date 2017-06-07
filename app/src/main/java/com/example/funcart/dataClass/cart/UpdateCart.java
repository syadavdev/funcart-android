package com.example.funcart.dataClass.cart;

import java.util.List;

public class UpdateCartDto {
	private String email;
	private List<UpdateCartItemDto> updateCartItem;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<UpdateCartItemDto> getUpdateCartItem() {
		return updateCartItem;
	}
	public void setUpdateCartItem(List<UpdateCartItemDto> updateCartItem) {
		this.updateCartItem = updateCartItem;
	}
}
