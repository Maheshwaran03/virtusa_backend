package com.example.inventory.repository;

import com.example.inventory.model.InventoryUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryUserRepository extends JpaRepository<InventoryUser, String> {
    InventoryUser findByUsernameAndPassword(String username, String password);
}
