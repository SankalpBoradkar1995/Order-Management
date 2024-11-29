package com.process.payment.service.execute;

import org.springframework.stereotype.Service;

import com.process.payment.request.PaymentRequest;
import com.process.payment.service.PaymentService;

@Service
public class ExecutePayments {
	
	private final PaymentService paymentService;
	
	
	public ExecutePayments(PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}
	
	public String orchestrator(PaymentRequest paymentRequest)
	{
		return paymentService.executepayment(paymentRequest);
	}

}
