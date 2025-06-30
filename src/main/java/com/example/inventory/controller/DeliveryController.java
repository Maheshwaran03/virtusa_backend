package com.example.inventory.controller;

import com.example.inventory.model.Delivery;
import com.example.inventory.model.StatusUpdateRequest;
import com.example.inventory.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryRepository deliveryRepository;

    // ✅ GET all deliveries
    @GetMapping
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    // ✅ GET deliveries by agent email
    @GetMapping("/by-agent")
    public ResponseEntity<?> getDeliveriesByAgent(@RequestParam("agent") String agentEmail) {
        if (agentEmail == null || agentEmail.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Agent email is required"));
        }
        List<Delivery> deliveries = deliveryRepository.findByAgent(agentEmail);
        return ResponseEntity.ok(deliveries);
    }

    // ✅ POST create delivery
    @PostMapping
    public Delivery createDelivery(@RequestBody Delivery delivery) {
        if (delivery.getStatus() == null) {
            delivery.setStatus("pending");
        }
        if (delivery.getPriority() == null) {
            delivery.setPriority("normal");
        }
        return deliveryRepository.save(delivery);
    }

    // ✅ PUT full update
    @PutMapping("/{id}")
    public Delivery updateDelivery(@PathVariable Long id, @RequestBody Delivery updatedDelivery) {
        return deliveryRepository.findById(id).map(existing -> {
            existing.setSku(updatedDelivery.getSku());
            existing.setProductName(updatedDelivery.getProductName());
            existing.setQuantity(updatedDelivery.getQuantity());
            existing.setAgent(updatedDelivery.getAgent());
            existing.setCustomerName(updatedDelivery.getCustomerName());
            existing.setCustomerMobile(updatedDelivery.getCustomerMobile());
            existing.setCustomerAddress(updatedDelivery.getCustomerAddress());
            existing.setDate(updatedDelivery.getDate());
            existing.setStatus(updatedDelivery.getStatus() != null ? updatedDelivery.getStatus() : existing.getStatus());
            existing.setPriority(updatedDelivery.getPriority() != null ? updatedDelivery.getPriority() : existing.getPriority());
            existing.setNotes(updatedDelivery.getNotes());
            existing.setSignatureBase64(updatedDelivery.getSignatureBase64());
            return deliveryRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Delivery not found with ID " + id));
    }

    // ✅ PATCH update status/notes/signature
    @PatchMapping("/{id}/status")
    public ResponseEntity<Delivery> updateDeliveryStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request
    ) {
        return deliveryRepository.findById(id).map(delivery -> {
            delivery.setStatus(request.getStatus());
            delivery.setNotes(request.getNotes());
            if (request.getSignature() != null && !request.getSignature().isEmpty()) {
                delivery.setSignatureBase64(request.getSignature());
            }
            Delivery saved = deliveryRepository.save(delivery);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ GET deliveries by status
    @GetMapping("/status/{status}")
    public List<Delivery> getByStatus(@PathVariable String status) {
        return deliveryRepository.findByStatusIgnoreCase(status);
    }

    // ✅ GET pending delivery count per agent
    @GetMapping("/status/pending/count")
    public List<Map<String, Object>> getPendingCountByAgent() {
        return deliveryRepository.countPendingByAgent();
    }
}
