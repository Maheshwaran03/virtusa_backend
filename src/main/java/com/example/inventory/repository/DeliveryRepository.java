package com.example.inventory.repository;

import com.example.inventory.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // If your Delivery model stores email in 'agent'
    int countByAgentEmail(String agentEmail); // Optional usage

    List<Delivery> findByStatusIgnoreCase(String status);

    // Find deliveries assigned to a specific agent (email or name)
    List<Delivery> findByAgent(String agent);

    @Query("SELECT d.agent as agent, COUNT(d) as pending FROM Delivery d WHERE LOWER(d.status) = 'pending' GROUP BY d.agent")
    List<Map<String, Object>> countPendingByAgent();
}
