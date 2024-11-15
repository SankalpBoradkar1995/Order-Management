package com.process.inventory.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class InventoryyEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@Column(name="productId")
	private final String productId;
	
	public String getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getProductName() {
		return productName;
	}

	@Column(name="quantity")
	private final Long quantity;
	
	@Column(name="price")
	private final BigDecimal price;
	
	@Column(name="name")
	private final String productName;
	
	public InventoryyEntity(String productId,Long quantity,BigDecimal price,String productName)
	{
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
		this.productName = productName;
	}
	

}
