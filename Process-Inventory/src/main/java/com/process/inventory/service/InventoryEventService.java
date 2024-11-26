package com.process.inventory.service;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.inventory.entity.InventoryEntity;
import com.process.inventory.event.InventoryEvent;
import com.process.inventory.repository.InventoryRepository;
import com.rabbit.config.RabbitMQConfig;

@Service
public class InventoryEventService {

	private final InventoryRepository inventoryRepository;
	private final ObjectMapper objectMapper;

	public InventoryEventService(InventoryRepository inventoryRepository,ObjectMapper objectMapper) {
		this.inventoryRepository = inventoryRepository;
		this.objectMapper = objectMapper;
	}

	@RabbitListener(queues = RabbitMQConfig.STOCK_DEDUCTION_QUEUE)
	@Transactional(rollbackFor = Exception.class)
	public void handleStockDeductionEvent(String inventoryEventJson) throws JsonProcessingException   {
		try
		{
			InventoryEvent inventoryEvent = objectMapper.readValue(inventoryEventJson, InventoryEvent.class);
			System.out.println("Processing Stock Deduction Event: " + inventoryEvent);
			Optional<InventoryEntity> productInventory = inventoryRepository
					.findByProductId(inventoryEvent.getProductId());
			if (productInventory.isPresent()) {
				InventoryEntity availableQuantity = productInventory.get();
				Integer updatedQuantity = availableQuantity.getQuantity() - inventoryEvent.getQuantity();
				if (updatedQuantity < 0) {
				    System.err.println("Insufficient stock for product: " + inventoryEvent.getProductId());
				    return; // or throw a custom exception
				}
				InventoryEntity updatedProduct = availableQuantity.withUpdatedStock(updatedQuantity);
				inventoryRepository.save(updatedProduct);
			} else {
				System.err.println("Product not found: " + inventoryEvent.getProductId());
			}
		}
		catch(JsonProcessingException e)
		{
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		    throw e; // Re-throw or handle the exception as needed
		}
		
	}

	@RabbitListener(queues = RabbitMQConfig.STOCK_REVERT_QUEUE)
	public void handleStockRevertEvent(InventoryEvent inventoryEvent) {
		System.out.println("Processing Stock Revert Event: " + inventoryEvent);
		// Process the stock revert logic here
	}
	//JsonMappingException, JsonProcessingException 

}
