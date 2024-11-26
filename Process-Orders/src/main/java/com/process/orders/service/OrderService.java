package com.process.orders.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import com.process.orders.repository.OrderRepository;
import com.process.orders.request.InventoryEvent;
import com.process.orders.request.PaymentRequest;
import com.process.orders.response.GetInventoryResponse;
import com.rabbit.config.RabbitMQConfig;

@Service
public class OrderService {

	private final ObjectMapper objectMapper;
	private final OrderRepository orderRepository;
	private final InventoryFeign inventoryFeign;
	private final PaymentService paymentService;
	private final RabbitTemplate rabbitTemplate;

	public OrderService(ObjectMapper objectMapper, OrderRepository orderRepository, InventoryFeign inventoryFeign,
			PaymentService paymentService,RabbitTemplate rabbitTemplate) {
		this.objectMapper = objectMapper;
		this.orderRepository = orderRepository;
		this.inventoryFeign = inventoryFeign;
		this.paymentService = paymentService;
		this.rabbitTemplate = rabbitTemplate;

	}

	public ResponseEntity<?> orchestrator(OrderEntity orderEntity)
			throws JsonMappingException, JsonProcessingException {
		return paymentService(orderEntity);
	}

	private ResponseEntity<?> paymentService(OrderEntity orderEntity)
			throws JsonMappingException, JsonProcessingException {

		GetInventoryResponse inventoryResponse;
		synchronized (this) {
			inventoryResponse = validateStock(orderEntity);
		}

		if (Objects.nonNull(inventoryResponse)) {
			CompletableFuture<String> orderId = processPaymentAndUpdateStock(orderEntity, inventoryResponse);
			return ResponseEntity.ok("Your order has been processed, " + orderId);
		} else {
			throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Please try again after some time");
		}
	}

	private CompletableFuture<String> processPaymentAndUpdateStock(OrderEntity orderRequest,
			GetInventoryResponse inventoryResponse) {
		OrderEntity orderToProcess = new OrderEntity("Order ID :" + generateRandomOrderId(), null,
				orderRequest.getAccountId(), orderRequest.getEmiStatus(), orderRequest.getProductName(),
				orderRequest.getQuantity(), orderRequest.getProductId(), orderRequest.getPrice(), "pending"

		);

		BigDecimal amount = BigDecimal.valueOf(orderToProcess.getQuantity()).multiply(inventoryResponse.getPrice());

		return paymentService.executePayments(new PaymentRequest(amount, 9766034820L)).thenApply(paymentResponse -> {
			if (paymentResponse.getStatusCode().is2xxSuccessful() && paymentResponse.getBody() != null) {

				updateOrder(orderToProcess, "Completed", amount, inventoryResponse);

				return orderToProcess.getOrderId();
			} else {
				updateOrder(orderToProcess, "Payment failed. Please try again", amount, inventoryResponse);
				InventoryEvent inventoryEvent = new InventoryEvent(orderRequest.getQuantity(),orderRequest.getProductId(),inventoryResponse.getPrice()
						, orderRequest.getProductName());
				rabbitTemplate.convertAndSend(RabbitMQConfig.INVENTORY_EXCHANGE,RabbitMQConfig.STOCK_REVERT_ROUTING_KEY,inventoryEvent);
				return orderToProcess.getOrderId();
			}
		}).exceptionally(ex -> {
			return "Payment for Order ID: " + orderToProcess.getOrderId() + " is failed. Please try again";

		});
	}

	private GetInventoryResponse validateStock(OrderEntity orderEntity)
			throws JsonMappingException, JsonProcessingException {
		// implement a spring feign call to get inventory result by product name
		// getInventoryByProductName(orderEntity.getProductName);

		ResponseEntity<GetInventoryResponse> productInvnetory = inventoryFeign
				.getInventoryDetailByProductId(orderEntity.getProductId());
		
		if (productInvnetory.getStatusCode().is2xxSuccessful() && productInvnetory.getBody() != null) {
			String jsonString = objectMapper.writeValueAsString(productInvnetory.getBody());
			JsonNode inventoryNode = objectMapper.readTree(jsonString);
			GetInventoryResponse inventoryResponse = objectMapper.treeToValue(inventoryNode,
					GetInventoryResponse.class);
			int requestQuantity = orderEntity.getQuantity();
			if(requestQuantity < inventoryResponse.getQuantity())
			{
				InventoryEvent inventoryEvent = new InventoryEvent(requestQuantity,orderEntity.getProductId(),inventoryResponse.getPrice(),
						orderEntity.getProductName());
				String inventoryEventJson = objectMapper.writeValueAsString(inventoryEvent);
				rabbitTemplate.convertAndSend(RabbitMQConfig.INVENTORY_EXCHANGE,RabbitMQConfig.STOCK_DEDUCTION_ROUTING_KEY,inventoryEventJson);
				System.out.println("Order Deduction Event posted");
				System.out.println(inventoryEvent);
				return inventoryResponse;
			}
			else {
			    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock");
			}
			
			
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Product not found or issue with Inventory service");
		}
	}

	private void updateOrder(OrderEntity orderEntity, String orderStatus, BigDecimal orderTotal,
			GetInventoryResponse inventoryResponse) {
		OrderEntity orderUpdate = new OrderEntity(orderEntity.getOrderId(), new Date(), orderEntity.getAccountId(),
				"Completed", // emi status,
				inventoryResponse.getProductName(), orderEntity.getQuantity(), inventoryResponse.getProductId(),
				orderTotal, orderStatus);

		orderRepository.save(orderUpdate);
	}

	private String generateRandomOrderId() {
		// Generate a random UUID and convert it to a string
		String randomOrderId = "ORD-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

		return randomOrderId;
	}
	
	private InventoryEvent createInventoryEvent(OrderEntity orderEntity, GetInventoryResponse inventoryResponse) {
	    return new InventoryEvent(
	        orderEntity.getQuantity(),
	        orderEntity.getProductId(),
	        inventoryResponse.getPrice(),
	        orderEntity.getProductName()
	    );
	}

}
