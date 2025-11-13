package com.electrical.inventory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ItemDialog extends JDialog {
    private final JTextField name = new JTextField(20);
    private final JTextField category = new JTextField(15);
    private final JSpinner quantity = new JSpinner(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
    private final JTextField location = new JTextField(10);
    private final JTextField supplier = new JTextField(15);
    private final JTextField purchaseDate = new JTextField(10); // YYYY-MM-DD
    private final JTextField condition = new JTextField(10);
    private final JTextArea notes = new JTextArea(4,30);
    private boolean confirmed = false;
    private InventoryItem item;

    public ItemDialog(Frame owner, InventoryItem existing) {
        super(owner, true);
        setTitle(existing==null ? "Add Item" : "Edit Item");
        setLayout(new BorderLayout(8,8));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints(); gc.insets = new Insets(4,4,4,4); gc.anchor = GridBagConstraints.WEST;

        int r=0;
        addRow(form, gc, r++, "Name:", name);
        addRow(form, gc, r++, "Category:", category);
        addRow(form, gc, r++, "Quantity:", quantity);
        addRow(form, gc, r++, "Location:", location);
        addRow(form, gc, r++, "Supplier:", supplier);
        addRow(form, gc, r++, "PurchaseDate (YYYY-MM-DD):", purchaseDate);
        addRow(form, gc, r++, "Condition:", condition);

        gc.gridx=0; gc.gridy=r; gc.gridwidth=2;
        form.add(new JScrollPane(notes), gc);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        buttons.add(ok); buttons.add(cancel);
        add(buttons, BorderLayout.SOUTH);

        ok.addActionListener(e -> {
            if (name.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Name is required."); return; }
            confirmed = true;
            if (existing == null) {
                item = new InventoryItem(null, name.getText().trim(), category.getText().trim(), (Integer)quantity.getValue(),
                        location.getText().trim(), supplier.getText().trim(), purchaseDate.getText().trim(), condition.getText().trim(), notes.getText().trim());
            } else {
                item = new InventoryItem(existing.getId(), name.getText().trim(), category.getText().trim(), (Integer)quantity.getValue(),
                        location.getText().trim(), supplier.getText().trim(), purchaseDate.getText().trim(), condition.getText().trim(), notes.getText().trim());
            }
            setVisible(false);
        });

        cancel.addActionListener(e -> { confirmed = false; setVisible(false); });

        if (existing != null) {
            name.setText(existing.getName());
            category.setText(existing.getCategory());
            quantity.setValue(existing.getQuantity());
            location.setText(existing.getLocation());
            supplier.setText(existing.getSupplier());
            purchaseDate.setText(existing.getPurchaseDate());
            condition.setText(existing.getCondition());
            notes.setText(existing.getNotes());
        } else {
            purchaseDate.setText(LocalDate.now().toString());
        }

        pack();
        setLocationRelativeTo(owner);
    }

    private void addRow(JPanel p, GridBagConstraints gc, int row, String label, Component comp) {
        gc.gridx=0; gc.gridy=row; gc.gridwidth=1;
        p.add(new JLabel(label), gc);
        gc.gridx=1; gc.gridy=row; gc.gridwidth=1;
        p.add(comp, gc);
    }

    public boolean isConfirmed() { return confirmed; }
    public InventoryItem getItem() { return item; }
}