package com.process.orders.service;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.orders.entity.OrderEntity;
import com.process.orders.feign.InventoryFeign;
import com.process.orders.repository.OrderRepository;

@Service
public class OrderService {

	private final OrderEntity orderEntity;
	private final ObjectMapper objectMapper;
	private final OrderRepository orderRepository;
	private final InventoryFeign inventoryFeign;

	public OrderService(OrderEntity orderEntity, ObjectMapper objectMapper, OrderRepository orderRepository,
			InventoryFeign inventoryFeign) {
		this.orderEntity = orderEntity;
		this.objectMapper = objectMapper;
		this.orderRepository = orderRepository;
		this.inventoryFeign = inventoryFeign;

	}

	public ResponseEntity<?> orchestrator(OrderEntity orderEntity)
			throws JsonMappingException, JsonProcessingException {

		if (validateStock(orderEntity)) {
			processOrderAndUpdateStock(orderEntity);
		}

		String orderId = "123456789";
		return ResponseEntity.ok("Your order has been processed, " + orderId);
	}

	private void processOrderAndUpdateStock(OrderEntity orderEntity) {

	}

	private boolean validateStock(OrderEntity orderEntity) throws JsonMappingException, JsonProcessingException {
		// implement a spring feign call to get inventory result by product name
		// getInventoryByProductName(orderEntity.getProductName);

		ResponseEntity<Map<String, Object>> response = inventoryFeign
				.getInventoryDetailByProductId(orderEntity.getProductId());

		// For now I have added test json here

		if (response != null && response.getBody() != null) {
			Map<String, Object> data = response.getBody();
			int requestQuantity = orderEntity.getQuantity();

			if (Objects.nonNull(data)) {
				Integer availableQuantity = (Integer) data.get("quantity");
				if (requestQuantity > availableQuantity) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product out of stock");
				}
				return true;
			}
		}
		return false;

	}

}
