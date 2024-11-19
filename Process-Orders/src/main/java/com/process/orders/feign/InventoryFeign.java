package com.process.orders.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.process.orders.mapper.InventoryMapper;

@FeignClient(name = "process-inventory")
public interface InventoryFeign {
	
	@GetMapping("/api/inventory/getProductDetails/{productId}")
	ResponseEntity<InventoryMapper> getInventoryDetailByProductId(@PathVariable String productId);

}
