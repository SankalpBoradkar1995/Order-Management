package com.process.inventory.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class InventoryEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	

	@Column(name="quantity")
	private final Integer quantity;
	
	@Column(name="price")
	private final BigDecimal price;
	
	@Column(name="name")
	private final String productName;
	
	
	@Column(name="productId")
	private final String productId;
	
	public String getProductId() {
		return productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getProductName() {
		return productName;
	}

	
	public InventoryEntity(Integer id, String productId,Integer quantity,BigDecimal price,String productName)
	{
		this.id = id;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
		this.productName = productName;
	}
	
	public InventoryEntity()
	{
		this.productId = null;
		this.price = null;
		this.productName = null;
		this.quantity = null;
	}
	
	public InventoryEntity withUpdatedStock(int updatedQuantity) {
        return new InventoryEntity(this.id,this.productId, updatedQuantity, this.price, this.productName);
    }

}
