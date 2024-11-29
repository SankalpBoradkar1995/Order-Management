package com.process.orders.request;

import java.math.BigDecimal;

public class PaymentRequest {

	public BigDecimal getAmount() {
		return amount;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	private final BigDecimal amount;
	private final String phoneNumber;
	private final String orderId;

	public PaymentRequest(BigDecimal amount, String phoneNumber,String orderId) {
		this.amount = amount;
		this.phoneNumber = phoneNumber;
		this.orderId = orderId;
	}

	public PaymentRequest() {
		this.amount = null;
		this.phoneNumber = null;
		this.orderId = null;
	}

}
