package com.process.orders.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "process-inventory")
public interface InventoryFeign {
	
	@GetMapping("/api/inventory/getProductDetails/{productId}")
	ResponseEntity<Map<String, Object>> getInventoryDetailByProductId(@PathVariable String productId);

}
