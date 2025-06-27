package com.example.inventory.controller;

import com.example.inventory.model.Delivery;
import com.example.inventory.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryRepository deliveryRepository;

    // GET all deliveries
    @GetMapping
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    // POST a new delivery
    @PostMapping
    public Delivery createDelivery(@RequestBody Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    // ✅ PUT: Update delivery by ID
    @PutMapping("/{id}")
    public Delivery updateDelivery(@PathVariable Long id, @RequestBody Delivery updatedDelivery) {
        return deliveryRepository.findById(id).map(existing -> {
            existing.setSku(updatedDelivery.getSku());
            existing.setQuantity(updatedDelivery.getQuantity());
            existing.setAgent(updatedDelivery.getAgent());
            existing.setCustomerName(updatedDelivery.getCustomerName());
            existing.setCustomerMobile(updatedDelivery.getCustomerMobile());
            existing.setCustomerAddress(updatedDelivery.getCustomerAddress());
            existing.setDate(updatedDelivery.getDate());
            existing.setStatus(updatedDelivery.getStatus() != null ? updatedDelivery.getStatus() : existing.getStatus());
            return deliveryRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Delivery not found with ID " + id));
    }
}
