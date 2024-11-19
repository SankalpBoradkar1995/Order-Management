package com.process.orders.mapper;

import java.util.Map;

public class InventoryMapper {
	private  Object data;

	public InventoryMapper(Object data) {
		this.data = data;
	}

	public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
