package com.example.funcart.order;

import java.util.List;

public class OrderDto {
	
	private String email;
	private List<OrderItemDto> orderItemDtoList;
	//private List<OrderCustomerDto> ordercustomerDtoList;
	private OrderCustomerDto ordercustomerDtoList ;
	private int orderId;
	private String PaymentMode;
	
	public OrderCustomerDto getOrdercustomerDtoList() {
		return ordercustomerDtoList;
	}
	public void setOrdercustomerDtoList(OrderCustomerDto ordercustomerDtoList) {
		this.ordercustomerDtoList = ordercustomerDtoList;
	}

	public String getPaymentMode() {
		return PaymentMode;
	}

	public void setPaymentMode(String string) {
		PaymentMode = string;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int i) {
		this.orderId = i;
	}
	public List<OrderItemDto> getOrderItemDtoList() {
		return orderItemDtoList;
	}

	public void setOrderItemDtoList(List<OrderItemDto> orderItemDtoList) {
		this.orderItemDtoList = orderItemDtoList;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
