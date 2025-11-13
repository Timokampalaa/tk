package com.electrical.inventory;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private JFrame frame;
    private JTable table;
    private InventoryTableModel tableModel;
    private JTextField searchField;
    private InventoryManager manager;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Use Nimbus look & feel if available for a modern look
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ignored) {}
            new Main().createAndShowGui();
        });
    }

    private void createAndShowGui() {
        manager = new InventoryManager();
        frame = new JFrame("Electrical UG Inventory — Professional Edition (JDK24 compatible)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);

        tableModel = new InventoryTableModel(manager.getAll());
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);

        JPanel topPanel = new JPanel(new BorderLayout(8,8));
        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String q = searchField.getText().trim();
                List<InventoryItem> filtered = manager.search(q);
                tableModel.setItems(filtered);
            }
        });

        topPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        JButton importBtn = new JButton("Import CSV");
        JButton exportBtn = new JButton("Export CSV");
        JButton reportBtn = new JButton("Low Stock Report");

        addBtn.addActionListener(e -> showItemDialog(null));
        editBtn.addActionListener(e -> {
            int idx = table.getSelectedRow();
            if (idx == -1) { JOptionPane.showMessageDialog(frame, "Select a row to edit."); return; }
            int modelIdx = table.convertRowIndexToModel(idx);
            InventoryItem it = tableModel.getItemAt(modelIdx);
            showItemDialog(it);
        });
        delBtn.addActionListener(e -> {
            int idx = table.getSelectedRow();
            if (idx == -1) {
                JOptionPane.showMessageDialog(frame, "Select a row to delete.");
                return;
            }

            int modelIdx = table.convertRowIndexToModel(idx);
            InventoryItem it = tableModel.getItemAt(modelIdx);

            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Delete item \"" + it.getName() + "\"?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                manager.delete(it.getId());
                tableModel.setItems(manager.getAll());
            }
        });


        importBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int res = fc.showOpenDialog(frame);
            if (res == JFileChooser.APPROVE_OPTION) {
                Path p = fc.getSelectedFile().toPath();
                try { manager.importCsv(p); tableModel.setItems(manager.getAll()); JOptionPane.showMessageDialog(frame, "Imported successfully."); }
                catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Import failed: "+ex.getMessage()); }
            }
        });

        exportBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int res = fc.showSaveDialog(frame);
            if (res == JFileChooser.APPROVE_OPTION) {
                Path p = fc.getSelectedFile().toPath();
                try { manager.exportCsv(p); JOptionPane.showMessageDialog(frame, "Exported to "+p.toString()); }
                catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Export failed: "+ex.getMessage()); }
            }
        });

        reportBtn.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(frame, "Show items with quantity less than (enter integer):", "10");
            if (s==null) return;
            try {
                int threshold = Integer.parseInt(s.trim());
                List<InventoryItem> low = manager.getAll().stream().filter(i -> i.getQuantity() < threshold).collect(Collectors.toList());
                ReportDialog.showReport(frame, low, threshold);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter an integer.");
            }
        });

        actions.add(addBtn);
        actions.add(editBtn);
        actions.add(delBtn);
        actions.add(importBtn);
        actions.add(exportBtn);
        actions.add(reportBtn);

        frame.getContentPane().setLayout(new BorderLayout(8,8));
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
        frame.getContentPane().add(actions, BorderLayout.SOUTH);


        try { manager.loadAutosave(); tableModel.setItems(manager.getAll()); } catch (Exception ignored) {}

       
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try { manager.saveAutosave(); } catch (Exception ignored) {}
            }
        });

        frame.setVisible(true);
    }

    private void showItemDialog(InventoryItem existing) {
        ItemDialog dlg = new ItemDialog(frame, existing);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            InventoryItem it = dlg.getItem();
            if (existing == null) manager.add(it);
            else manager.update(it);
            tableModel.setItems(manager.getAll());
        }
    }
}