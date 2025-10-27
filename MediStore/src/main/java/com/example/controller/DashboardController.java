package com.example.controller;

import com.example.service.MedicineService;
import com.example.service.PurchaseService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final PurchaseService purchaseService;
    private final MedicineService medicineService;

    public DashboardController(PurchaseService purchaseService, MedicineService medicineService) {
        this.purchaseService = purchaseService;
        this.medicineService = medicineService;
    }

    // 📊 Dashboard summary (counts only)
    @GetMapping
    public Map<String, Long> getDashboardSummary() {
        Map<String, Long> summary = new HashMap<>();
        summary.put("todayCustomers", purchaseService.getTodayCustomerCount());
        summary.put("outOfStockCount", medicineService.getOutOfStockCount());
        return summary;
    }
}
