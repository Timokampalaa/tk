package com.electrical.inventory;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Table model powering the JTable view.
 */
public class InventoryTableModel extends AbstractTableModel {
    private List<InventoryItem> items;
    private final String[] cols = {"ID","Name","Category","Qty","Location","Supplier","PurchaseDate","Condition","Notes"};

    public InventoryTableModel(List<InventoryItem> items) { this.items = items; }

    public void setItems(List<InventoryItem> items) { this.items = items; fireTableDataChanged(); }

    public InventoryItem getItemAt(int row) { return items.get(row); }

    @Override
    public int getRowCount() { return items.size(); }

    @Override
    public int getColumnCount() { return cols.length; }

    @Override
    public String getColumnName(int col) { return cols[col]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InventoryItem it = items.get(rowIndex);
        switch(columnIndex) {
            case 0: return it.getId();
            case 1: return it.getName();
            case 2: return it.getCategory();
            case 3: return it.getQuantity();
            case 4: return it.getLocation();
            case 5: return it.getSupplier();
            case 6: return it.getPurchaseDate();
            case 7: return it.getCondition();
            case 8: return it.getNotes();
            default: return "";
        }
    }
}