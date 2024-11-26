package com.process.orders.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.process.orders.mapper.InventoryMapper;
import com.process.orders.response.GetInventoryResponse;

@FeignClient(name = "process-inventory")
public interface InventoryFeign {
	
	@GetMapping("/api/inventory/getProductDetails/{productId}")
	ResponseEntity<GetInventoryResponse> getInventoryDetailByProductId(@PathVariable String productId);
	

	@GetMapping("/api/inventory/getProductDetails/{productId}") // this one is for testing purpose only
	ResponseEntity<GetInventoryResponse> getInventoryDetailByProductId1(@PathVariable String productId);

}
