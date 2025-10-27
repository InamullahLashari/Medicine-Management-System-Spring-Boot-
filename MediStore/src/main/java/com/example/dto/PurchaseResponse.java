package com.example.dto;

public class PurchaseResponse {
    private String message;
    private long todayCustomerCount;
    private int updatedStock;       // stock for purchased medicine
    private long outOfStockCount;   // total medicines with stock=0
    private double purchasePrice;   // ✅ total price of this purchase

    public PurchaseResponse() {}

    public PurchaseResponse(String message, long todayCustomerCount, int updatedStock, long outOfStockCount, double purchasePrice) {
        this.message = message;
        this.todayCustomerCount = todayCustomerCount;
        this.updatedStock = updatedStock;
        this.outOfStockCount = outOfStockCount;
        this.purchasePrice = purchasePrice;
    }

    // Getters & Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getTodayCustomerCount() { return todayCustomerCount; }
    public void setTodayCustomerCount(long todayCustomerCount) { this.todayCustomerCount = todayCustomerCount; }

    public int getUpdatedStock() { return updatedStock; }
    public void setUpdatedStock(int updatedStock) { this.updatedStock = updatedStock; }

    public long getOutOfStockCount() { return outOfStockCount; }
    public void setOutOfStockCount(long outOfStockCount) { this.outOfStockCount = outOfStockCount; }

    public double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(double purchasePrice) { this.purchasePrice = purchasePrice; }
}
