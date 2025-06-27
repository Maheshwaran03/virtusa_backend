package com.example.inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "inventory_item",
        uniqueConstraints = @UniqueConstraint(columnNames = {"item_name", "sku", "batch"})
)
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String sku;

    private String category;
    private String batch;
    private int quantity;

    @Column(name = "expiry_date")
    private String expiryDate;

    @Column(name = "is_perishable")
    private boolean isPerishable;

    @Column(name = "is_damaged")
    private int damaged;
}
