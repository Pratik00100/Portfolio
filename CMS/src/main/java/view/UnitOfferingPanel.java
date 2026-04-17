package view;

import controller.InstructorController;
import controller.UnitController;
import controller.UnitOfferingController;
import model.UnitOffering;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UnitOfferingPanel extends JPanel {
    private final UnitOfferingController controller;
    private final UnitController unitController;
    private final InstructorController instructorController;
    private JTextField idField, semField, yearField;
    private JComboBox<String> unitCombo;
    private DefaultTableModel tableModel;

    public UnitOfferingPanel(UnitOfferingController controller, UnitController unitController,
                              InstructorController instructorController) {
        this.controller = controller;
        this.unitController = unitController;
        this.instructorController = instructorController;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Add Unit Offering"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField(10);
        unitCombo = new JComboBox<>();
        semField = new JTextField(10);
        yearField = new JTextField(6);
        refreshUnitCombo();

        addFormRow(form, gbc, 0, "Offering ID:", idField);
        addComboRow(form, gbc, 1, "Unit:", unitCombo);
        addFormRow(form, gbc, 2, "Semester (e.g. S1):", semField);
        addFormRow(form, gbc, 3, "Year:", yearField);

        JButton addBtn = new JButton("Add Offering");
        addBtn.setBackground(new Color(0, 102, 204));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> addOffering());

        JButton refreshBtn = new JButton("Refresh Units");
        refreshBtn.addActionListener(e -> { refreshUnitCombo(); loadTable(); });

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btns.add(addBtn);
        btns.add(refreshBtn);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        form.add(btns, gbc);

        String[] cols = {"Offering ID", "Unit Code", "Semester", "Year", "Instructor ID"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadTable();
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        p.add(field, gbc);
    }

    private void addComboRow(JPanel p, GridBagConstraints gbc, int row, String label, JComboBox<String> combo) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        p.add(combo, gbc);
    }

    private void refreshUnitCombo() {
        unitCombo.removeAllItems();
        for (var u : unitController.getAllUnits()) {
            unitCombo.addItem(u.getUnitCode() + " - " + u.getUnitName());
        }
    }

    private void addOffering() {
        String id = idField.getText().trim();
        String sem = semField.getText().trim();
        String yearStr = yearField.getText().trim();
        if (id.isEmpty() || sem.isEmpty() || yearStr.isEmpty() || unitCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }
        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year must be a number.");
            return;
        }
        String unitCode = unitCombo.getSelectedItem().toString().split(" - ")[0];
        UnitOffering offering = new UnitOffering(id, unitCode, sem, year, null);
        if (controller.addOffering(offering)) {
            JOptionPane.showMessageDialog(this, "Unit offering added successfully.");
            idField.setText(""); semField.setText(""); yearField.setText("");
            loadTable();
        } else {
            JOptionPane.showMessageDialog(this, "Offering ID already exists.");
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (UnitOffering o : controller.getAllOfferings()) {
            tableModel.addRow(new Object[]{
                o.getOfferingId(), o.getUnitCode(), o.getSemester(), o.getYear(),
                o.getInstructorId() != null ? o.getInstructorId() : "Not assigned"
            });
        }
    }
}
