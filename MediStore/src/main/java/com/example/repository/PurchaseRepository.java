package com.example.repository;

import com.example.entity.Customer;
import com.example.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    // Count distinct customers by phone for today
    @Query("SELECT COUNT(DISTINCT p.customer.phone) " +
            "FROM Purchase p " +
            "WHERE DATE(p.purchaseDateTime) = CURRENT_DATE")
    long countTodayCustomers();

    // Find all purchases for a specific customer by phone
    List<Purchase> findByCustomerPhone(String phone);

    // ✅ Get DISTINCT customers who purchased today
    @Query("SELECT DISTINCT p.customer " +
            "FROM Purchase p " +
            "WHERE DATE(p.purchaseDateTime) = CURRENT_DATE")
    List<Customer> findTodayCustomers();

    // ✅ Get all purchases for today
    @Query("SELECT p FROM Purchase p WHERE DATE(p.purchaseDateTime) = CURRENT_DATE")
    List<Purchase> findTodayPurchases();
}
