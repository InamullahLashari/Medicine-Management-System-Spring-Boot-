package com.example.controller;

import com.example.dto.PurchaseRequest;
import com.example.dto.PurchaseResponse;
import com.example.entity.Purchase;
import com.example.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    // ➕ Create purchase
    @PostMapping
    public PurchaseResponse createPurchase(@RequestBody PurchaseRequest request) {
        return purchaseService.createPurchase(request);
    }

    // 📋 Get all purchases (history)
    @GetMapping
    public List<Purchase> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    // 📋 Get only today’s purchases
    @GetMapping("/today")
    public List<Purchase> getTodayPurchases() {
        return purchaseService.getTodayPurchases();
    }

    // 🔍 Get purchases by customer phone
    @GetMapping("/customer/{phone}")
    public List<Purchase> getPurchasesByCustomer(@PathVariable String phone) {
        return purchaseService.getPurchasesByCustomer(phone);
    }

    // 📊 Get today’s unique customer count (for dashboard)
    @GetMapping("/today-customers")
    public long getTodayCustomerCount() {
        return purchaseService.getTodayCustomerCount();
    }
}
