package com.electrical.inventory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReportDialog {
    public static void showReport(Frame owner, java.util.List<InventoryItem> items, int threshold) {
        JDialog dlg = new JDialog(owner, "Low Stock Report (< " + threshold + ")", true);
        dlg.setLayout(new BorderLayout(8,8));

        StringBuilder sb = new StringBuilder();
        if (items.isEmpty()) sb.append("No items below threshold.");
        else {
            for (InventoryItem it : items) {
                sb.append(String.format("%s — %s — Qty: %d\n", it.getName(), it.getCategory(), it.getQuantity()));
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        dlg.add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("Close");
        ok.addActionListener(e -> dlg.setVisible(false));
        btns.add(ok);
        dlg.add(btns, BorderLayout.SOUTH);

        dlg.setSize(400,300);
        dlg.setLocationRelativeTo(owner);
        dlg.setVisible(true);
    }
}