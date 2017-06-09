package com.example.funcart.dataClass.cart;

import java.util.List;

public class UpdateCart {
	private String email;
	private List<UpdateCartItem> updateCartItem;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<UpdateCartItem> getUpdateCartItem() {
		return updateCartItem;
	}
	public void setUpdateCartItem(List<UpdateCartItem> updateCartItem) {
		this.updateCartItem = updateCartItem;
	}
}
