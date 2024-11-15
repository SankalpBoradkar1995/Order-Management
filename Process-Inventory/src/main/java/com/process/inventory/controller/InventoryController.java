package com.process.inventory.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.process.inventory.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	@Autowired
	InventoryService inventoryService;

	@GetMapping("/getProductDetails/{productId}")
	public ResponseEntity<Map<String, Object>> getProductDetails(@PathVariable String productId)
	{
		return inventoryService.getProductDetails(productId);
	}
}