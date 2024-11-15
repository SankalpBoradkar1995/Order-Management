package com.process.orders.mapper;

import java.util.Map;

public class InventoryMapper {
	private final Map<String, Object> data;

	public InventoryMapper(Map<String, Object> data) {
		this.data = data;
	}

	public Map<String, Object> getData() {
		return data;
	}
}
