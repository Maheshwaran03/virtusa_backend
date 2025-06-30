package com.example.inventory.repository;

import com.example.inventory.model.AgentCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentCredentialRepository extends JpaRepository<AgentCredential, Long> {
    Optional<AgentCredential> findByEmail(String email);
    boolean existsByEmail(String email);
}