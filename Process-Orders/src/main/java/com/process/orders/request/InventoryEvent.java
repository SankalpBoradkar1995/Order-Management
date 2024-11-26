package com.process.orders.request;

import java.math.BigDecimal;

public class InventoryEvent {



	@Override
	public String toString() {
		return "InventoryEvent [QUANTITY=" + quantity + ", PRODUCT_ID=" + productId + ", PRICE=" + price
				+ ", PRODUCT_NAME=" + productName + "]";
	}

	private final Integer quantity;
	private final String productId;
	private final BigDecimal price;
	private final String productName;

	public Integer getQuantity() {
		return quantity;
	}

	public String getProductId() {
		return productId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getProductName() {
		return productName;
	}

	public InventoryEvent() {
		this.productId = null;
		this.quantity = null;
		this.price = null;
		this.productName = null;
	}

	public InventoryEvent(Integer quantity, String productId,BigDecimal price,String productName) {
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
		this.productName = productName;
	}

	

	
}
