package com.process.orders.controller;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.process.orders.entity.OrderEntity;
import com.process.orders.repository.OrderRepository;
import com.process.orders.response.GetInventoryResponse;
import com.process.orders.service.OrderService;
import com.process.orders.service.TestOrderService;

@RestController
@RequestMapping("/api/orders")
public class ProcessOrdersController {
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	TestOrderService testOrderService;
	

	@GetMapping("/getOrder/{orderId}")
	public ResponseEntity<OrderEntity> orderDetail(@PathVariable String orderId) {
		String orderInfo = "Order details for order ID: " + orderId;
		//return ResponseEntity.ok(orderRepository.findByOrderId(orderId).get);
		Optional<OrderEntity> orderDetailByOrderId = orderRepository.findByOrderId(orderId);
		if (orderDetailByOrderId.isPresent()) {
	        return ResponseEntity.ok(orderDetailByOrderId.get());  // Return the order if found
	    } else {
	        return ResponseEntity.notFound().build();  // Return 404 if order is not found
	    }
	}
	
	@PostMapping(value = "/processOrder")
	public CompletableFuture<ResponseEntity<?>> processOrder(@RequestBody OrderEntity orderEntity) throws JsonMappingException, JsonProcessingException {
	    return orderService.orchestrator(orderEntity);
	}
	
	
	// Test URIs
	
	@GetMapping("/valueByFeign/{productId}")
	public ResponseEntity<GetInventoryResponse> getDataByFeign(@PathVariable String productId) throws JsonProcessingException
	{
		return testOrderService.getInventory(productId);
	}

	
	@GetMapping("/valueByFeign/jsonNode/{productId}")
	public ResponseEntity<GetInventoryResponse> validateJsonNodeService(@PathVariable String productId) throws JsonProcessingException
	{
		return testOrderService.validateJsonNode(productId);
	}
}
