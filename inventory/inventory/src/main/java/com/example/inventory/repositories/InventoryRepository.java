package com.example.inventory.repositories;

import com.example.inventory.entity.Item;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Item,Integer> {
    @Query("SELECT i FROM Item i " +
            "WHERE (:itemName IS NULL OR LOWER(i.itemName) LIKE LOWER(CONCAT('%', :itemName, '%'))) " +
            "AND (:itemType IS NULL OR i.itemType = :itemType) " +
            "AND (:brands IS NULL OR i.itemBrand IN :brands)")
    List<Item> searchItems(@Param("itemName") String itemName,
                           @Param("itemType") String itemType,
                           @Param("brands") List<String> brands);

}
