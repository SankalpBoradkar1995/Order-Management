package com.process.orders.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.process.orders.entity.OrderEntity;


public interface OrderRepository extends JpaRepository<OrderEntity, Integer>{
	
	Optional<OrderEntity> findByOrderId(String orderId);

}
