package com.example.inventory.controller;

import com.example.inventory.model.InventoryUser;
import com.example.inventory.service.InventoryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/inventory-users")
public class AdminController {

    @Autowired
    private InventoryUserService service;

    @GetMapping
    public List<InventoryUser> getAllUsers() {
        return service.getAllUsers();
    }

    @PostMapping
    public InventoryUser createUser(@RequestBody InventoryUser user) {
        return service.createUser(user);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        service.deleteUser(username);
    }
}
