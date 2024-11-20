package com.process.orders.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import com.process.orders.feign.ProceeePaymentFeign;
import com.process.orders.mapper.PaymentMapper;
import com.process.orders.request.PaymentRequest;

public class PaymentService {
	private final ProceeePaymentFeign processPaymentFeign;
	
	public PaymentService(ProceeePaymentFeign processPaymentFeign)
	{
		this.processPaymentFeign = processPaymentFeign;
	}
	
	@Async
	public CompletableFuture<ResponseEntity<PaymentMapper>> executePayments(PaymentRequest paymentRequest)
	{
		return CompletableFuture.supplyAsync(() ->processPaymentFeign.executePayment(paymentRequest));
	}

}
