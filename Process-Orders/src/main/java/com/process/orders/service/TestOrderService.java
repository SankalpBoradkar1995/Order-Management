package com.process.orders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.orders.feign.InventoryFeign;
import com.process.orders.mapper.InventoryMapper;
import com.process.orders.response.GetInventoryResponse;

@Service
public class TestOrderService {
	
	@Autowired
	InventoryFeign inventoryClient;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public ResponseEntity<GetInventoryResponse> getInventory(String productId) throws JsonProcessingException
	{
		
		ResponseEntity<GetInventoryResponse> inventoryResponse = inventoryClient
				.getInventoryDetailByProductId1(productId);
		if(inventoryResponse.getStatusCode().is2xxSuccessful() && inventoryResponse.getBody() != null)
		{
			
			
			return inventoryResponse;
			
		}
		else
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not available");
		}
	}
	
	public ResponseEntity<GetInventoryResponse> validateJsonNode(String productId) throws JsonProcessingException
	{
		ResponseEntity<GetInventoryResponse> productInvnetory= inventoryClient.getInventoryDetailByProductId(productId);
		if(productInvnetory.getStatusCode().is2xxSuccessful() && productInvnetory.getBody() != null)
		{
			String jsonString = objectMapper.writeValueAsString(productInvnetory.getBody());
			JsonNode inventoryNode = objectMapper.readTree(jsonString);
			GetInventoryResponse inventoryResponse = objectMapper.treeToValue(inventoryNode,GetInventoryResponse.class);
			return ResponseEntity.ok(inventoryResponse);
		}
		else
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found or issue with Inventory service");
		}
	}

}
