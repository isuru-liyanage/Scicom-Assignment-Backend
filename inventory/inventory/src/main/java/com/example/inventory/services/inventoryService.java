package com.example.inventory.services;

import com.example.inventory.dto.ItemDTO;
import com.example.inventory.dto.ItemSearchDTO;
import com.example.inventory.entity.Item;
import com.example.inventory.repositories.InventoryRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class inventoryService {
    private final InventoryRepository inventoryRepository;

    public ResponseEntity<Page<Item>> getAll(int page){
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        System.out.println(inventoryRepository.findAll(pageable));
        return ResponseEntity.ok(inventoryRepository.findAll(pageable));
    }

    public  ResponseEntity<Map<String,String>> saveInventoryItem(ItemDTO itemDTO){
        Item item = itemDTO.getItem();
        try {
            System.out.println(itemDTO.getItemExpireDate());
            inventoryRepository.save(item);
            return ResponseEntity.ok(Map.of("Message","Item Saved Successfully!"));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of("message","Error : "+e.getMessage()));
        }

    }

    public ResponseEntity <Map<String,String>> UpdateItem(Item item){
        inventoryRepository.save(item);
        return ResponseEntity.ok(Map.of("Message","Item Saved Successfully!"));
    }


    public ResponseEntity<Item> getItemById(int id) {
        return ResponseEntity.ok(inventoryRepository.findById(id).orElseThrow(null));
    }


    public ResponseEntity<Map<String,String>> deleteItembyId(int id) {
        inventoryRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("Message","Item Deleted Successfully!"));

    }

    public List<Item> searchItems(ItemSearchDTO searchDTO) {
        if (searchDTO.getBrands().isEmpty()){
            searchDTO.setBrands(null);
        }
        if(searchDTO.getItemName().isEmpty()){
            searchDTO.setItemName(null);
        }
        if(searchDTO.getItemType().isEmpty()){
            searchDTO.setItemType(null);
        }

        return inventoryRepository.searchItems(
                searchDTO.getItemName(),
                searchDTO.getItemType(),
                searchDTO.getBrands()
        );
    }
}
