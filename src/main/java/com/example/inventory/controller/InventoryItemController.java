package com.example.inventory.controller;

import com.example.inventory.model.InventoryItem;
import com.example.inventory.service.InventoryItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:5173") // adjust if frontend runs on different port
public class InventoryItemController {


    private final InventoryItemService service;

    public InventoryItemController(InventoryItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<InventoryItem> getAll() {
        return service.getAllItems();
    }
    @PutMapping("/{id}")
    public InventoryItem updateItem(@PathVariable Long id, @RequestBody InventoryItem updatedItem) {
        return service.updateItem(id, updatedItem);
    }

    @PostMapping
    public InventoryItem addItem(@RequestBody InventoryItem item) {
        return service.saveItem(item);
    }

    @PostMapping("/bulk")
    public List<InventoryItem> bulkUpload(@RequestBody List<InventoryItem> items) {
        return service.saveAll(items);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        service.deleteItem(id);
    }
}
