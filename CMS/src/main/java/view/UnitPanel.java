package view;

import controller.UnitController;
import model.Unit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class UnitPanel extends JPanel {
    private final UnitController controller;
    private JTextField codeField, nameField, creditsField, descField, prereqField, searchField;
    private DefaultTableModel tableModel;

    public UnitPanel(UnitController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Add Unit"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        codeField = new JTextField(10);
        nameField = new JTextField(20);
        creditsField = new JTextField(5);
        descField = new JTextField(20);
        prereqField = new JTextField(20);

        addFormRow(form, gbc, 0, "Unit Code:", codeField);
        addFormRow(form, gbc, 1, "Unit Name:", nameField);
        addFormRow(form, gbc, 2, "Credits:", creditsField);
        addFormRow(form, gbc, 3, "Description:", descField);
        addFormRow(form, gbc, 4, "Prerequisites (;-separated):", prereqField);

        JButton addBtn = new JButton("Add Unit");
        addBtn.setBackground(new Color(0, 102, 204));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> addUnit());

        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> loadTable(controller.searchUnits(searchField.getText().trim())));

        JButton sortBtn = new JButton("Sort by Name");
        sortBtn.addActionListener(e -> loadTable(controller.getSortedByName()));

        JButton showAllBtn = new JButton("Show All");
        showAllBtn.addActionListener(e -> loadTable(controller.getAllUnits()));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btns.add(addBtn);
        btns.add(new JSeparator(SwingConstants.VERTICAL));
        btns.add(new JLabel("Search:"));
        btns.add(searchField);
        btns.add(searchBtn);
        btns.add(sortBtn);
        btns.add(showAllBtn);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        form.add(btns, gbc);

        String[] cols = {"Code", "Name", "Credits", "Description", "Prerequisites"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadTable(controller.getAllUnits());
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        p.add(field, gbc);
    }

    private void addUnit() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String credStr = creditsField.getText().trim();
        String desc = descField.getText().trim();
        String prereqStr = prereqField.getText().trim();

        if (code.isEmpty() || name.isEmpty() || credStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Unit Code, Name and Credits are required.");
            return;
        }
        int credits;
        try {
            credits = Integer.parseInt(credStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credits must be a whole number.");
            return;
        }
        ArrayList<String> prereqs = new ArrayList<>();
        if (!prereqStr.isEmpty()) prereqs.addAll(Arrays.asList(prereqStr.split(";")));

        Unit unit = new Unit(code, name, credits, desc, prereqs);
        if (controller.addUnit(unit)) {
            JOptionPane.showMessageDialog(this, "Unit added successfully.");
            clearForm();
            loadTable(controller.getAllUnits());
        } else {
            JOptionPane.showMessageDialog(this, "A unit with that code already exists.");
        }
    }

    private void loadTable(ArrayList<Unit> units) {
        tableModel.setRowCount(0);
        for (Unit u : units) {
            tableModel.addRow(new Object[]{
                u.getUnitCode(), u.getUnitName(), u.getCredits(),
                u.getDescription(), String.join("; ", u.getPrerequisites())
            });
        }
    }

    private void clearForm() {
        codeField.setText(""); nameField.setText(""); creditsField.setText("");
        descField.setText(""); prereqField.setText("");
    }
}
