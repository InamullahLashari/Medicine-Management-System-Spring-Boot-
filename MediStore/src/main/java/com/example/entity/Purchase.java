package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Customer who bought
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Medicine purchased
    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    // Quantity purchased
    @Column(nullable = false)
    private int quantity;

    // Price per unit at purchase time
    @Column(nullable = false)
    private double price;

    // Total price = quantity * price
    @Column(nullable = false)
    private double totalPrice;

    // Purchase timestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDateTime;

    @PrePersist
    public void onCreate() {
        this.purchaseDateTime = LocalDateTime.now(); // set current timestamp automatically
    }

    // Default constructor
    public Purchase() {}

    // Constructor with quantity and price
    public Purchase(Customer customer, Medicine medicine, int quantity, double price) {
        this.customer = customer;
        this.medicine = medicine;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = price * quantity; // calculate total price automatically
    }

    // Getters & Setters
    public Long getId() { return id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Medicine getMedicine() { return medicine; }
    public void setMedicine(Medicine medicine) { this.medicine = medicine; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = this.price * quantity; // recalc total if quantity changes
    }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        this.price = price;
        this.totalPrice = this.quantity * price; // recalc total if price changes
    }

    public double getTotalPrice() { return totalPrice; }

    public LocalDateTime getPurchaseDateTime() { return purchaseDateTime; }
    public void setPurchaseDateTime(LocalDateTime purchaseDateTime) { this.purchaseDateTime = purchaseDateTime; }
}
