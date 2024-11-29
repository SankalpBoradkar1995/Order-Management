package com.process.orders.response;

import java.math.BigDecimal;

public class GetInventoryResponse {
	
	public String getProductName() {
		return productName;
	}

	public Long getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}
	
	public String getProductId() {
		return productId;
	}

	private final String productName;
	private final Long quantity;
	private final BigDecimal price;
	private final String productId;
	
	public GetInventoryResponse(String productName, Long quantity, BigDecimal price,String productId)
	{
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
		this.productId = productId;
	}
	
	public GetInventoryResponse()
	{
		this.productName = null;
		this.quantity = null;
		this.price = null;
		this.productId = null;
	}

	
	
}
