package com.process.orders.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.process.orders.feign.ProceeePaymentFeign;
import com.process.orders.mapper.PaymentMapper;
import com.process.orders.request.PaymentRequest;

@Service
public class PaymentService {
	private final ProceeePaymentFeign processPaymentFeign;
	
	public PaymentService(ProceeePaymentFeign processPaymentFeign)
	{
		this.processPaymentFeign = processPaymentFeign;
	}
	
	@Async
	public CompletableFuture<ResponseEntity<String>> executePayments(PaymentRequest paymentRequest)
	{
		try {
	        return CompletableFuture.completedFuture(processPaymentFeign.executePayment(paymentRequest));
	    } catch (Exception e) {
	        return CompletableFuture.failedFuture(e);
	    }
	}

}
