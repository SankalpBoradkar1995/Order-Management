package com.process.payment.request;

import java.math.BigDecimal;

public class PaymentRequest {

	public String getOrderId() {
		return orderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return "PaymentRequest [amount=" + amount + ", phoneNumber=" + phoneNumber + ", orderId=" + orderId + "]";
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
