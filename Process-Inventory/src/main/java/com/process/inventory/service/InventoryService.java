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
import com.process.inventory.response.GetInventoryResponse;

@Service
public class InventoryService {

	private final InventoryRepository inventoryRepository;

	public InventoryService(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}

	public ResponseEntity<?> getProductDetails(String productId) {
		
		
		return inventoryRepository.findByProductId(productId).map(inventory -> new GetInventoryResponse(
				inventory.getProductName(), inventory.getQuantity(), inventory.getPrice())).map(ResponseEntity::ok).
		orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Available") );
		
	}

}
