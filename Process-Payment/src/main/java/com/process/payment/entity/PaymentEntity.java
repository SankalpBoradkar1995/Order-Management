package com.process.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PaymentEntity {
	
	public Integer getId() {
		return id;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public String getTransactionId() {
		return transactionId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Payment-Id")
    private Integer id;
	
	@Column(name="Order-Id")
	private final String orderId;
	
	@Column(name="Payment-Status")
	private final String paymentStatus;
	
	@Column(name="Transaction-Id")
	private final String transactionId;
	
	public PaymentEntity()
	{
		this.orderId = null;
		this.paymentStatus = null;
		this.transactionId = null;
	}
	
	public PaymentEntity(String orderId, String paymentStatus, String transactionId)
	{
		this.orderId = orderId;
		this.paymentStatus = paymentStatus;
		this.transactionId = transactionId;
	}

}
