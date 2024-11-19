package com.process.orders.feign;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.process.orders.mapper.PaymentMapper;

@FeignClient(name = "process-payment")
public interface ProceeePaymentFeign {
	
	@PutMapping("/api/payment/execute/{amount}/{phoneNumber}")
	ResponseEntity<PaymentMapper> executePayment(@PathVariable BigDecimal amount, @PathVariable String phoneNumber);

}
