package com.process.orders.mapper;

public class PaymentMapper {

	private final String data;
	
	public String getData() {
		return data;
	}

	public PaymentMapper(String data)
	{
		this.data = data;
	}
	
	public PaymentMapper()
	{
		this.data = null;
	}
	
}
