package com.example.service;

import com.example.dto.PurchaseRequest;
import com.example.dto.PurchaseResponse;
import com.example.entity.Customer;
import com.example.entity.Medicine;
import com.example.entity.Purchase;
import com.example.exception.StockNotAvailableException;
import com.example.repository.CustomerRepository;
import com.example.repository.MedicineRepository;
import com.example.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedicineService medicineService;

    // ➕ Create a new purchase
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        // 1️⃣ Find or create customer
        Customer customer = customerRepository.findByPhone(request.getPhone())
                .orElseGet(() -> customerRepository.save(
                        new Customer(request.getCustomerName(), request.getPhone())
                ));

        // 2️⃣ Find medicine
        Medicine medicine = medicineRepository.findByNameIgnoreCase(request.getMedicineName())
                .orElseThrow(() -> new RuntimeException("Medicine not found: " + request.getMedicineName()));

        // 3️⃣ Check stock
        if (medicine.getQuantity() < request.getQuantity()) {
            throw new StockNotAvailableException(
                    "Not enough stock. Available stock for " + request.getMedicineName() +
                            " is " + medicine.getQuantity()
            );
        }

        // 4️⃣ Deduct stock
        medicine.setQuantity(medicine.getQuantity() - request.getQuantity());
        medicineRepository.save(medicine);

        // ⚠️ Optional: log low stock
        if (medicine.getQuantity() < 10) {
            System.out.println("⚠️ Low Stock: " + medicine.getName() + " (Qty: " + medicine.getQuantity() + ")");
        }

        // 5️⃣ Save purchase with price and total
        double medicinePrice = request.getPrice(); // unit price from frontend
        Purchase purchase = new Purchase(customer, medicine, request.getQuantity(), medicinePrice);
        purchaseRepository.save(purchase);

        // 6️⃣ Dashboard counts
        long todayCount = purchaseRepository.countTodayCustomers();
        long outOfStockCount = medicineService.getOutOfStockCount();

        // 7️⃣ Calculate total purchase price
        double totalPrice = medicinePrice * request.getQuantity();

        // 8️⃣ Return response
        return new PurchaseResponse(
                "Purchase successful",
                todayCount,
                medicine.getQuantity(),
                outOfStockCount,
                totalPrice
        );
    }

    // 📋 Get all purchases
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    // 📋 Get only today’s purchases
    public List<Purchase> getTodayPurchases() {
        return purchaseRepository.findTodayPurchases();
    }

    // 🔍 Get purchases by customer phone
    public List<Purchase> getPurchasesByCustomer(String phone) {
        return purchaseRepository.findByCustomerPhone(phone);
    }

    // 📊 Get today's customer count
    public long getTodayCustomerCount() {
        return purchaseRepository.countTodayCustomers();
    }
}
