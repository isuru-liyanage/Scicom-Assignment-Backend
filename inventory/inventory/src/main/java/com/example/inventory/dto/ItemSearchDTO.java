package com.example.inventory.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemSearchDTO {
    private String itemName;
    private String itemType;
    private List<String> brands;
}
