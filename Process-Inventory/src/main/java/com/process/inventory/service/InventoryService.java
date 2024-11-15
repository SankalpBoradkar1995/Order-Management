package com.process.inventory.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.process.inventory.entity.InventoryyEntity;
import com.process.inventory.repository.InventoryRepository;

@Service
public class InventoryService {

	private final InventoryRepository inventoryRepository;

	public InventoryService(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}

	public ResponseEntity<Map<String, Object>> getProductDetails(String productId)
	{
		Optional<InventoryyEntity> response = inventoryRepository.findByProductId(productId);
		if(response.isPresent())
		{
			InventoryyEntity inventory = response.get();
			
			// Convert InventoryyEntity to Map
			Map<String, Object> responseData = new HashMap<>();
			responseData.put("itemId", inventory.getProductId());
			responseData.put("quantity", inventory.getQuantity());
			responseData.put("price", inventory.getPrice());
			responseData.put("name", inventory.getProductName());
			
			return ResponseEntity.ok(responseData);
			
		}
			
		else
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product Not Available");
	}

}
