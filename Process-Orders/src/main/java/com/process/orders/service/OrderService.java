package com.process.orders.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.orders.entity.OrderEntity;
import com.process.orders.feign.InventoryFeign;
import com.process.orders.mapper.InventoryMapper;
import com.process.orders.repository.OrderRepository;
import com.process.orders.request.PaymentRequest;

@Service
public class OrderService {

	private final ObjectMapper objectMapper;
	private final OrderRepository orderRepository;
	private final InventoryFeign inventoryFeign;
	private final PaymentService paymentService;

	public OrderService(ObjectMapper objectMapper, OrderRepository orderRepository, InventoryFeign inventoryFeign,
			PaymentService paymentService) {
		this.objectMapper = objectMapper;
		this.orderRepository = orderRepository;
		this.inventoryFeign = inventoryFeign;
		this.paymentService = paymentService;

	}

	public ResponseEntity<?> paymentService(OrderEntity orderEntity)
			throws JsonMappingException, JsonProcessingException {

		if (validateStock(orderEntity)) {
			processPaymentAndUpdateStock(orderEntity);
		}

		String orderId = "123456789";
		return ResponseEntity.ok("Your order has been processed, " + orderId);
	}

	private CompletableFuture<String> processPaymentAndUpdateStock(OrderEntity orderRequest) {
		OrderEntity orderToProcess = new OrderEntity("Order ID :" + generateRandomOrderId(), null,
				orderRequest.getAccountId(), orderRequest.getEmiStatus(), orderRequest.getProductName(),
				orderRequest.getQuantity(), orderRequest.getProductId(), orderRequest.getPrice(), "pending"

		);

		BigDecimal amount = BigDecimal.valueOf(orderToProcess.getQuantity()).multiply(orderToProcess.getPrice());

		return paymentService.executePayments(new PaymentRequest(amount, 9766034820L)).thenApply(paymentResponse -> {
			if (paymentResponse.getStatusCode().is2xxSuccessful() && paymentResponse.getBody() != null) {
				OrderEntity processedOrder = new OrderEntity(orderToProcess.getOrderId(), new Date(),
						orderToProcess.getAccountId(), orderToProcess.getEmiStatus(), orderToProcess.getProductName(),
						orderToProcess.getQuantity(), orderToProcess.getProductId(), amount, "PAYMENT_SUCCESSFUL");
				orderRepository.save(processedOrder);
				return processedOrder.getOrderId();
			} else {
				OrderEntity processedOrder = new OrderEntity(orderToProcess.getOrderId(), new Date(),
						orderToProcess.getAccountId(), orderToProcess.getEmiStatus(), orderToProcess.getProductName(),
						orderToProcess.getQuantity(), orderToProcess.getProductId(), amount, "PAYMENT_PENDING");
				orderRepository.save(processedOrder);
				return processedOrder.getOrderId();
			}
		}).exceptionally(ex -> {
			return "Payment for Order ID: " + orderToProcess.getOrderId() + " is failed. Please try again";

		});
	}

	private boolean validateStock(OrderEntity orderEntity) throws JsonMappingException, JsonProcessingException {
		JsonNode inventoryResponseNode1 = null;
		// implement a spring feign call to get inventory result by product name
		// getInventoryByProductName(orderEntity.getProductName);

		ResponseEntity<InventoryMapper> inventoryResponse = inventoryFeign
				.getInventoryDetailByProductId(orderEntity.getProductId());
		if (inventoryResponse.getStatusCode().is2xxSuccessful() && inventoryResponse.getBody() != null) {
			String jsonString = objectMapper.writeValueAsString(inventoryResponse.getBody());
			inventoryResponseNode1 = objectMapper.readTree(jsonString);
			if (inventoryResponseNode1.has("quantity")) {
				Long availableQuantity = inventoryResponseNode1.get("quantity").asLong();
				if (orderEntity.getQuantity() >= availableQuantity) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product out of stock");
				} else if (inventoryResponseNode1.has("price")
						&& !inventoryResponseNode1.get("price").asText().equals(orderEntity.getPrice().toString())) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price mismatch");
				}
				return true;
			}
		}

		return false;

	}

	public String generateRandomOrderId() {
		// Generate a random UUID and convert it to a string
		String randomOrderId = "ORD-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

		return randomOrderId;
	}

}
