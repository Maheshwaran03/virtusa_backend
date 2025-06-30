package com.example.inventory.repository;

import com.example.inventory.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByStatusIgnoreCase(String status);

    @Query("SELECT d.agent as agent, COUNT(d) as pending FROM Delivery d WHERE LOWER(d.status) = 'pending' GROUP BY d.agent")
    List<Map<String, Object>> countPendingByAgent();
}
