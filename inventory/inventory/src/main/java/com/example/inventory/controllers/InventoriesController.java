package com.example.inventory.controllers;

import com.example.inventory.dto.ItemDTO;
import com.example.inventory.dto.ItemSearchDTO;
import com.example.inventory.entity.Item;
import com.example.inventory.services.inventoryService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/inventories")
public class InventoriesController {
    private final inventoryService inventoryService;

    @GetMapping()
    public ResponseEntity<Page<Item>> getItems(@RequestParam(defaultValue = "0") int page){
        return inventoryService.getAll(page);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemByID( @PathVariable int id){
        return inventoryService.getItemById(id);
    }

    @PostMapping()
    public ResponseEntity<Map<String,String>> saveItem(@RequestBody @Valid ItemDTO item){
        return inventoryService.saveInventoryItem(item);
    }
    @PutMapping()
    public ResponseEntity<Map<String,String>> updateItem(@RequestBody @Valid Item item){
        return inventoryService.UpdateItem(item);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteItemByID( @PathVariable int id){
        return inventoryService.deleteItembyId(id);
    }
    @PostMapping("/search")
    public ResponseEntity<List<Item>> searchItems(@RequestBody ItemSearchDTO searchDTO) {
        List<Item> items = inventoryService.searchItems(searchDTO);
        return ResponseEntity.ok(items);
    }
}
