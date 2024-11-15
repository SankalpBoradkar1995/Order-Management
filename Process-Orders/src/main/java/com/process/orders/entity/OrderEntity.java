package com.process.orders.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OrderEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private final String orderId;
	private final Date orderDate;
	public String getOrderId() {
		return orderId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getEmiStatus() {
		return emiStatus;
	}

	public String getProductName() {
		return productName;
	}

	private final String accountId;
	private final String emiStatus;
	private final String productName;
	private final int quantity;
	private final String productId;

	public String getProductId() {
		return productId;
	}

	public OrderEntity(String orderId, Date orderDate, String accountId, String emiStatus,String productName,int quantity,String productId) {
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.accountId = accountId;
		this.emiStatus = emiStatus;
		this.productName = productName;
		this.quantity = quantity;
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

}
