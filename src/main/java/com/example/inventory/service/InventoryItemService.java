package com.example.inventory.service;

import com.example.inventory.model.InventoryItem;
import com.example.inventory.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryItemService {

    private final InventoryItemRepository repository;

    public InventoryItemService(InventoryItemRepository repository) {
        this.repository = repository;
    }

    public List<InventoryItem> getAllItems() {
        return repository.findAll();
    }

    public InventoryItem saveItem(InventoryItem item) {
        return repository.findByItemNameAndCategoryIgnoreCase(item.getItemName(), item.getCategory())
                .map(existingItem -> {
                    existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                    existingItem.setExpiryDate(item.getExpiryDate());
                    existingItem.setPerishable(item.isPerishable());
                    existingItem.setDamaged(item.isDamaged());
                    existingItem.setSku(item.getSku());
                    return repository.save(existingItem);
                })
                .orElseGet(() -> repository.save(item));
    }

    public List<InventoryItem> saveAll(List<InventoryItem> items) {
        for (InventoryItem item : items) {
            saveItem(item);
        }
        return repository.findAll();
    }

    public void deleteItem(Long id) {
        repository.deleteById(id);
    }

    public InventoryItem updateItem(Long id, InventoryItem updatedItem) {
        return repository.findById(id).map(item -> {
            item.setItemName(updatedItem.getItemName());
            item.setSku(updatedItem.getSku());
            item.setCategory(updatedItem.getCategory());
            item.setQuantity(updatedItem.getQuantity());
            item.setExpiryDate(updatedItem.getExpiryDate());
            item.setPerishable(updatedItem.isPerishable());
            item.setDamaged(updatedItem.isDamaged());
            return repository.save(item);
        }).orElseThrow(() -> new RuntimeException("Item not found with id " + id));
    }
}
