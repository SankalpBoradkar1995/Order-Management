package com.process.inventory.response;

import java.math.BigDecimal;

public class GetInventoryResponse {
	
	private final String productName;
	private final Long quantity;
	private final BigDecimal price;
	
	public GetInventoryResponse(String productName, Long quantity, BigDecimal price)
	{
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
	}

}
