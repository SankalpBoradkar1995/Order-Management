package com.process.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class ProcessOrdersController {

	@GetMapping("/getOrder/{orderId}")
	public ResponseEntity<String> orderDetail(@PathVariable int orderId) {
		String orderInfo = "Order details for order ID: " + orderId;
		return ResponseEntity.ok(orderInfo);
	}

}
