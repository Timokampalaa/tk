package com.electrical.inventory;

import java.time.LocalDate;
import java.util.UUID;

public class InventoryItem {
    private final String id;
    private String name;
    private String category;
    private int quantity;
    private String location;
    private String supplier;
    private String purchaseDate; // YYYY-MM-DD
    private String condition;
    private String notes;

    public InventoryItem(String id, String name, String category, int quantity, String location, String supplier, String purchaseDate, String condition, String notes) {
        this.id = id == null || id.isEmpty() ? UUID.randomUUID().toString() : id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.location = location;
        this.supplier = supplier;
        this.purchaseDate = purchaseDate;
        this.condition = condition;
        this.notes = notes;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getCategory() { return category; }
    public void setCategory(String c) { this.category = c; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int q) { this.quantity = q; }
    public String getLocation() { return location; }
    public void setLocation(String l) { this.location = l; }
    public String getSupplier() { return supplier; }
    public void setSupplier(String s) { this.supplier = s; }
    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String d) { this.purchaseDate = d; }
    public String getCondition() { return condition; }
    public void setCondition(String c) { this.condition = c; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { this.notes = n; }

    @Override
    public String toString() {
        return name + " (" + category + ") x" + quantity;
    }
}