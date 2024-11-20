package com.process.orders.request;

import java.math.BigDecimal;

public class PaymentRequest {
	
	private final BigDecimal amount;
	private final Long phoneNumber;
	
	public PaymentRequest(BigDecimal amount, Long phoneNumber)
	{
		this.amount = amount;
		this.phoneNumber = phoneNumber;
	}

}
