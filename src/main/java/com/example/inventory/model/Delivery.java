package com.example.inventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private int quantity;
    private String agent;
    private String customerName;
    private String customerMobile; // ✅ Added
    private String customerAddress;
    private String date;
    private String status = "pending"; // default value

    // Getters and Setters

}
