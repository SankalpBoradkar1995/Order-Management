package com.process.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.process.payment.entity.PaymentEntity;
import com.process.payment.request.PaymentRequest;
import com.process.payment.service.execute.ExecutePayments;

@RestController
@RequestMapping("/api/payment")
public class PostPaymentController {
	
	@Autowired
	private ExecutePayments executePayments;
	
	@PostMapping(value="/execute")
	public ResponseEntity<?> executePayment(@RequestBody PaymentRequest paymentRequest)
	{
		System.out.println("Inside execute payments controller");
		return ResponseEntity.ok(executePayments.orchestrator(paymentRequest));
	}

}
