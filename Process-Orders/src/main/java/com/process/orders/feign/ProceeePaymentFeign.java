package com.process.orders.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.process.orders.mapper.PaymentMapper;
import com.process.orders.request.PaymentRequest;

@FeignClient(name = "process-payment")
public interface ProceeePaymentFeign {
	
	@PutMapping("/api/payment/execute/")
	ResponseEntity<PaymentMapper> executePayment(@RequestBody PaymentRequest paymentRequest);

}
