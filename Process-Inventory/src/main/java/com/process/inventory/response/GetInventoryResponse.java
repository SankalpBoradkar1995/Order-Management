package com.process.inventory.response;

import java.math.BigDecimal;

public class GetInventoryResponse {
	
	public String getProductName() {
		return productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}
	
	public String getProductId() {
		return productId;
	}

	private final String productName;
	private final Integer quantity;
	private final BigDecimal price;
	private final String productId;
	
	public GetInventoryResponse(String productName, Integer quantity, BigDecimal price,String productId)
	{
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
		this.productId = productId;
	}

	
	
}
