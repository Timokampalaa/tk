package com.electrical.inventory;

import java.time.LocalDate;
import java.util.*;

/**
 * Very small CSV utility. CSV format:
 * id,name,category,quantity,location,supplier,purchaseDate,condition,notes
 * Commas in fields are not supported in this simple format.
 */
public class CSVUtil {
    public static String toCsv(InventoryItem it) {
        return String.join(",",
            safe(it.getId()),
            safe(it.getName()),
            safe(it.getCategory()),
            String.valueOf(it.getQuantity()),
            safe(it.getLocation()),
            safe(it.getSupplier()),
            safe(it.getPurchaseDate()),
            safe(it.getCondition()),
            safe(it.getNotes())
        );
    }

    public static InventoryItem fromCsv(String line) {
        String[] parts = line.split(",", -1);
        String id = parts.length>0?parts[0]:"";
        String name = parts.length>1?parts[1]:"";
        String category = parts.length>2?parts[2]:"";
        int qty = 0;
        try { if (parts.length>3) qty = Integer.parseInt(parts[3]); } catch (Exception ignored) {}
        String location = parts.length>4?parts[4]:"";
        String supplier = parts.length>5?parts[5]:"";
        String purchaseDate = parts.length>6?parts[6]:"";
        String condition = parts.length>7?parts[7]:"";
        String notes = parts.length>8?parts[8]:"";
        return new InventoryItem(id, name, category, qty, location, supplier, purchaseDate, condition, notes);
    }

    private static String safe(String s) { return s==null?"":s.replace("\n"," ").replace("\r"," "); }
}