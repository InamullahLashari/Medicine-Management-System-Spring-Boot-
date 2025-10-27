package com.example.repository;

import com.example.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Optional<Medicine> findByNameIgnoreCase(String name);

    // List all out-of-stock medicines
    @Query("SELECT m FROM Medicine m WHERE m.quantity <= 0")
    List<Medicine> findOutOfStockMedicines();

    // Count out-of-stock medicines
    @Query("SELECT COUNT(m) FROM Medicine m WHERE m.quantity <= 0")
    long countOutOfStockMedicines();
}
