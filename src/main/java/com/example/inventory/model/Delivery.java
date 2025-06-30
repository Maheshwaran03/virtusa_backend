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

    private String productName; // ✅ NEW FIELD

    private int quantity;
    private String agent;

    private String customerName;
    private String customerMobile;
    private String customerAddress;

    private String date;

    private String status = "pending";   // default status
    private String priority = "normal";  // default priority

    private String notes;

    @Lob
    @Column(name = "signature_base64", columnDefinition = "LONGTEXT")
    private String signatureBase64;

    public String getSignatureBase64() {
        return signatureBase64;
    }

    public void setSignatureBase64(String signatureBase64) {
        this.signatureBase64 = signatureBase64;
    }
    private String agentEmail; // Add this line if not already present

}
