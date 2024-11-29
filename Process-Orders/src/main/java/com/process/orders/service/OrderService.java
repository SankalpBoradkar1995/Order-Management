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
                        PaymentService paymentService, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
        this.inventoryFeign = inventoryFeign;
        this.paymentService = paymentService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public CompletableFuture<ResponseEntity<?>> orchestrator(OrderEntity orderEntity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return paymentService(orderEntity);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error processing order: " + e.getMessage());
            }
        });
    }

    private ResponseEntity<?> paymentService(OrderEntity orderEntity) throws JsonProcessingException, IllegalArgumentException {
        GetInventoryResponse inventoryResponse = validateStock(orderEntity);

        if (Objects.nonNull(inventoryResponse)) {
            return processPaymentAndUpdateStock(orderEntity, inventoryResponse)
                    .thenApply(orderResponse -> ResponseEntity.ok(orderResponse))
                    .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Payment processing failed: " + ex.getMessage()))
                    .join();
        } else {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Please try again after some time");
        }
    }

    private CompletableFuture<String> processPaymentAndUpdateStock(OrderEntity orderRequest, GetInventoryResponse inventoryResponse)throws JsonProcessingException, IllegalArgumentException  {
        OrderEntity orderToProcess = new OrderEntity("Order ID :" + generateRandomOrderId(), null,
                orderRequest.getAccountId(), orderRequest.getProductName(), orderRequest.getQuantity(),
                orderRequest.getProductId(), orderRequest.getPrice(), "pending");

        BigDecimal amount = BigDecimal.valueOf(orderToProcess.getQuantity()).multiply(inventoryResponse.getPrice());

        return paymentService.executePayments(new PaymentRequest(amount, "9766034820", orderToProcess.getOrderId()))
                .thenApply(paymentResponse -> {
                    if (paymentResponse.getStatusCode().is2xxSuccessful() && paymentResponse.getBody() != null) {
                        if (paymentResponse.getBody().toString().equalsIgnoreCase("PAYMENT-SUCCESS")) {
                            updateOrder(orderToProcess, "ORDER-COMPLETE", amount, inventoryResponse);
                            return "Your order has been processed, " + orderToProcess.getOrderId();
                        } else {
                            updateOrder(orderToProcess, "ORDER-FAILED", amount, inventoryResponse);
                            String inventoryEventJson = createInventoryEvent(orderRequest, inventoryResponse);
                            sendStockRevertEvent(inventoryEventJson);
                            return "Payment failed for order ID:" + orderToProcess.getOrderId();
                        }
                    } else {
                        updateOrder(orderToProcess, "Payment service down. Please try again", amount, inventoryResponse);
                        String inventoryEventJson = createInventoryEvent(orderRequest, inventoryResponse);
                        sendStockRevertEvent(inventoryEventJson);
                        return orderToProcess.getOrderId();
                    }
                }).exceptionally(ex -> {
                    return "Payment for Order ID: " + orderToProcess.getOrderId() + " failed. Please try again";
                });
    }

    private GetInventoryResponse validateStock(OrderEntity orderEntity) throws JsonProcessingException, IllegalArgumentException {
        ResponseEntity<GetInventoryResponse> productInventory = inventoryFeign
                .getInventoryDetailByProductId(orderEntity.getProductId());

        if (productInventory.getStatusCode().is2xxSuccessful() && productInventory.getBody() != null) {
            String jsonString = objectMapper.writeValueAsString(productInventory.getBody());
            JsonNode inventoryNode = objectMapper.readTree(jsonString);
            GetInventoryResponse inventoryResponse = objectMapper.treeToValue(inventoryNode, GetInventoryResponse.class);
            int requestQuantity = orderEntity.getQuantity();
            if (requestQuantity <= inventoryResponse.getQuantity()) {
                String inventoryEventJson = createInventoryEvent(orderEntity, inventoryResponse);
                sendStockDeductionEvent(inventoryEventJson);
                return inventoryResponse;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Product not found or issue with Inventory service");
        }
    }

    private void updateOrder(OrderEntity orderEntity, String orderStatus, BigDecimal orderTotal, GetInventoryResponse inventoryResponse) {
        OrderEntity orderUpdate = new OrderEntity(orderEntity.getOrderId(), new Date(), orderEntity.getAccountId(),
                inventoryResponse.getProductName(), orderEntity.getQuantity(), inventoryResponse.getProductId(),
                orderTotal, orderStatus);

        orderRepository.save(orderUpdate);
    }

    private String generateRandomOrderId() {
        return "ORD-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    private String createInventoryEvent(OrderEntity orderEntity, GetInventoryResponse inventoryResponse) {
        InventoryEvent inventoryEvent = new InventoryEvent(orderEntity.getQuantity(), orderEntity.getProductId(),
                inventoryResponse.getPrice(), orderEntity.getProductName());
        try {
			return objectMapper.writeValueAsString(inventoryEvent);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

    private void sendStockDeductionEvent(String inventoryEventJson) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.INVENTORY_EXCHANGE, RabbitMQConfig.STOCK_DEDUCTION_ROUTING_KEY,
                inventoryEventJson);
    }

    private void sendStockRevertEvent(String inventoryEventJson) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.INVENTORY_EXCHANGE, RabbitMQConfig.STOCK_REVERT_ROUTING_KEY,
                inventoryEventJson);
    }
}