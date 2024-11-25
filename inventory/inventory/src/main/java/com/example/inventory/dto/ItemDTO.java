package com.example.inventory.dto;

import com.example.inventory.entity.Item;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class ItemDTO {
    @NotNull(message = "Item Type is required.")
    private String itemType;

    @NotBlank(message = "Item Name is required.")
    @Size(max = 50, message = "Item Name must not exceed 50 characters.")
    private String itemName;

    @NotBlank(message = "Item Description is required.")
    @Size(max = 200, message = "Item Description must not exceed 200 characters.")
    private String itemDescription;

    @NotEmpty(message = "Brand is required.")
    private String itemBrand;

    @NotNull(message = "Item Price is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Item Price must be greater than 0.")
    private BigDecimal itemPrice;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date itemExpireDate;

    public Item getItem() {
        Item item = new Item();
        item.setItemType(itemType);
        item.setItemName(itemName);
        item.setItemDescription(itemDescription);
        item.setItemBrand(itemBrand);
        item.setItemPrice(itemPrice);
        item.setItemExpireDate(itemExpireDate);
        return item;
    }
}
