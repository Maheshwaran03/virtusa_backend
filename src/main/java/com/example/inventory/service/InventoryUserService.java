package com.example.inventory.service;

import com.example.inventory.model.InventoryUser;
import com.example.inventory.repository.InventoryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryUserService {

    @Autowired
    private InventoryUserRepository repo;

    public List<InventoryUser> getAllUsers() {
        return repo.findAll();
    }

    public InventoryUser createUser(InventoryUser user) {
        if (repo.existsById(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        return repo.save(user);
    }

    public void deleteUser(String username) {
        repo.deleteById(username);
    }

    public boolean login(String username, String password) {
        return repo.findByUsernameAndPassword(username, password) != null;
    }
}
