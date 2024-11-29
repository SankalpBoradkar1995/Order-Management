package com.process.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.process.inventory.entity.InventoryEntity;



@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer>{
	Optional<InventoryEntity> findByProductId(String productId);

}
