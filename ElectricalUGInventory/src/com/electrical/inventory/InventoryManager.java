package com.electrical.inventory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple manager storing items in memory and saving/loading CSV autosave.
 */
public class InventoryManager {
    private final List<InventoryItem> items = new ArrayList<>();
    private final Path autosave = Paths.get(System.getProperty("user.home"), ".electrical_inventory_autosave.csv");

    public List<InventoryItem> getAll() {
        return new ArrayList<>(items);
    }

    public void add(InventoryItem item) {
        items.add(item);
    }

    public void update(InventoryItem item) {
        for (int i=0;i<items.size();i++) {
            if (items.get(i).getId().equals(item.getId())) {
                items.set(i, item);
                return;
            }
        }
    }

    public void delete(String id) {
        items.removeIf(i -> i.getId().equals(id));
    }

    public List<InventoryItem> search(String q) {
        if (q == null || q.isEmpty()) return getAll();
        String l = q.toLowerCase();
        return items.stream().filter(i ->
            (i.getName()!=null && i.getName().toLowerCase().contains(l)) ||
            (i.getCategory()!=null && i.getCategory().toLowerCase().contains(l)) ||
            (i.getLocation()!=null && i.getLocation().toLowerCase().contains(l)) ||
            (i.getSupplier()!=null && i.getSupplier().toLowerCase().contains(l)) ||
            (i.getNotes()!=null && i.getNotes().toLowerCase().contains(l))
        ).collect(Collectors.toList());
    }

    public void importCsv(Path p) throws IOException {
        List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            InventoryItem it = CSVUtil.fromCsv(line);
            add(it);
        }
    }

    public void exportCsv(Path p) throws IOException {
        List<String> lines = new ArrayList<>();
        for (InventoryItem it : items) lines.add(CSVUtil.toCsv(it));
        Files.write(p, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void saveAutosave() throws IOException {
        exportCsv(autosave);
    }

    public void loadAutosave() throws IOException {
        if (Files.exists(autosave)) {
            items.clear();
            importCsv(autosave);
        }
    }
}