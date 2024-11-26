package com.process.orders.entity;

import java.math.BigDecimal;
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

    private String orderId;
    private Date orderDate;
    private String accountId;
    private String emiStatus;
    private String productName;
    private Integer quantity;
    private String productId;
    private BigDecimal price;
    private String orderStatus;

    // Default constructor (required by JPA)
    public OrderEntity() {
    }

    // Parameterized constructor
    public OrderEntity(String orderId, Date orderDate, String accountId, String emiStatus, String productName, Integer quantity, String productId, BigDecimal price, String orderStatus) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.accountId = accountId;
        this.emiStatus = emiStatus;
        this.productName = productName;
        this.quantity = quantity;
        this.productId = productId;
        this.price = price;
        this.orderStatus = orderStatus;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

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

    public Integer getQuantity() {
        return quantity;
    }

    public String getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
