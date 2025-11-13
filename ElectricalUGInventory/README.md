# Electrical UG Inventory — Enhanced Edition

This is an improved, professional-looking Swing-based Java inventory system.
It's designed to run with modern JDKs (including JDK 24) without external GUI libraries.

## Features
- Add / Edit / Delete inventory items
- Search & live-filter items
- Sortable table view
- Import / Export CSV
- Low-stock report
- Autosave on exit (saved to: `~/.electrical_inventory_autosave.csv`)

## How to compile & run (simple)
From project root:

```bash
# compile
javac -d out src/com/electrical/inventory/*.java

# run
java -cp out com.electrical.inventory.Main
```

If you prefer an IDE, import the `src` folder as a Java project and run `com.electrical.inventory.Main`.

## Notes
- CSV format is simple and does not support commas inside fields.
- Autosave file location: `~/.electrical_inventory_autosave.csv`