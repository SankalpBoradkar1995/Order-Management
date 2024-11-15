package com.process.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.process.inventory.entity.InventoryyEntity;



@Repository
public interface InventoryRepository extends JpaRepository<InventoryyEntity, Integer>{
	Optional<InventoryyEntity> findByProductId(String productId);

}
