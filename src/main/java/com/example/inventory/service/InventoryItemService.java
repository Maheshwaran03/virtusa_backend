package com.example.inventory.service;

import com.example.inventory.model.InventoryItem;
import com.example.inventory.repository.InventoryItemRepository;
import org.springframework.dao.DataIntegrityViolationException;
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
        List<InventoryItem> all = repository.findAll();
        for (InventoryItem i : all) {
            if (i.getSku().equalsIgnoreCase(item.getSku()) &&
                    !i.getItemName().equalsIgnoreCase(item.getItemName())) {
                throw new DataIntegrityViolationException("SKU already used by another item name");
            }
        }

        return repository.findByNameSkuAndBatchIgnoreCase(item.getItemName(), item.getSku(), item.getBatch())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + item.getQuantity());
                    existing.setDamaged(existing.getDamaged() + item.getDamaged());
                    existing.setExpiryDate(item.getExpiryDate());
                    existing.setCategory(item.getCategory());
                    existing.setPerishable(item.isPerishable());
                    return repository.save(existing);
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
            item.setBatch(updatedItem.getBatch());
            item.setQuantity(updatedItem.getQuantity());
            item.setExpiryDate(updatedItem.getExpiryDate());
            item.setPerishable(updatedItem.isPerishable());
            item.setDamaged(updatedItem.getDamaged());
            return repository.save(item);
        }).orElseThrow(() -> new RuntimeException("Item not found with ID " + id));
    }
    public List<String> getDistinctCategories() {
        return repository.findDistinctCategories();
    }
}
