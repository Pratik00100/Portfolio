package view;

import controller.InstructorController;
import controller.UnitOfferingController;
import model.Instructor;
import model.UnitOffering;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InstructorPanel extends JPanel {
    private final InstructorController controller;
    private final UnitOfferingController offeringController;
    private JTextField idField, nameField, emailField, deptField;
    private JComboBox<String> instructorCombo, offeringCombo;
    private DefaultTableModel tableModel;

    public InstructorPanel(InstructorController controller, UnitOfferingController offeringController) {
        this.controller = controller;
        this.offeringController = offeringController;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Register section
        JPanel regForm = new JPanel(new GridBagLayout());
        regForm.setBorder(BorderFactory.createTitledBorder("Register Instructor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField(10);
        nameField = new JTextField(15);
        emailField = new JTextField(15);
        deptField = new JTextField(15);

        addRow(regForm, gbc, 0, "ID:", idField);
        addRow(regForm, gbc, 1, "Name:", nameField);
        addRow(regForm, gbc, 2, "Email:", emailField);
        addRow(regForm, gbc, 3, "Department:", deptField);

        JButton regBtn = new JButton("Register");
        regBtn.setBackground(new Color(0, 153, 76));
        regBtn.setForeground(Color.WHITE);
        regBtn.addActionListener(e -> registerInstructor());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        regForm.add(regBtn, gbc);

        // Assign section
        JPanel assignForm = new JPanel(new GridBagLayout());
        assignForm.setBorder(BorderFactory.createTitledBorder("Assign Instructor to Offering"));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(4, 4, 4, 4);
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        instructorCombo = new JComboBox<>();
        offeringCombo = new JComboBox<>();
        refreshCombos();

        gbc2.gridx = 0; gbc2.gridy = 0; gbc2.gridwidth = 1; gbc2.weightx = 0;
        assignForm.add(new JLabel("Instructor:"), gbc2);
        gbc2.gridx = 1; gbc2.weightx = 1;
        assignForm.add(instructorCombo, gbc2);
        gbc2.gridx = 0; gbc2.gridy = 1; gbc2.weightx = 0;
        assignForm.add(new JLabel("Offering:"), gbc2);
        gbc2.gridx = 1; gbc2.weightx = 1;
        assignForm.add(offeringCombo, gbc2);

        JButton assignBtn = new JButton("Assign");
        assignBtn.setBackground(new Color(204, 102, 0));
        assignBtn.setForeground(Color.WHITE);
        assignBtn.addActionListener(e -> assignInstructor());

        JButton refreshBtn = new JButton("Refresh Lists");
        refreshBtn.addActionListener(e -> { refreshCombos(); loadTable(); });

        gbc2.gridx = 0; gbc2.gridy = 2; gbc2.gridwidth = 2;
        JPanel assignBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        assignBtns.add(assignBtn);
        assignBtns.add(refreshBtn);
        assignForm.add(assignBtns, gbc2);

        topPanel.add(regForm);
        topPanel.add(assignForm);

        String[] cols = {"ID", "Name", "Email", "Department"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadTable();
    }

    private void addRow(JPanel p, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        p.add(field, gbc);
    }

    private void refreshCombos() {
        instructorCombo.removeAllItems();
        for (var i : controller.getSortedByName()) {
            instructorCombo.addItem(i.getInstructorId() + " - " + i.getName());
        }
        offeringCombo.removeAllItems();
        for (var o : offeringController.getAllOfferings()) {
            offeringCombo.addItem(o.getOfferingId() + " | " + o.getUnitCode() + " " + o.getSemester() + " " + o.getYear());
        }
    }

    private void registerInstructor() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String dept = deptField.getText().trim();
        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }
        Instructor instructor = new Instructor(id, name, email, dept);
        if (controller.addInstructor(instructor)) {
            JOptionPane.showMessageDialog(this, "Instructor registered successfully.");
            idField.setText(""); nameField.setText(""); emailField.setText(""); deptField.setText("");
            loadTable();
            refreshCombos();
        } else {
            JOptionPane.showMessageDialog(this, "Instructor ID already exists.");
        }
    }

    private void assignInstructor() {
        if (instructorCombo.getSelectedItem() == null || offeringCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select an instructor and an offering.");
            return;
        }
        String instrId = instructorCombo.getSelectedItem().toString().split(" - ")[0];
        String offId = offeringCombo.getSelectedItem().toString().split(" \\| ")[0];
        UnitOffering offering = offeringController.findById(offId);
        if (offering == null) {
            JOptionPane.showMessageDialog(this, "Offering not found.");
            return;
        }
        if (controller.assignToOffering(instrId, offering)) {
            offeringController.updateOffering(offering);
            JOptionPane.showMessageDialog(this, "Instructor assigned successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Assignment failed — instructor not found.");
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Instructor i : controller.getAllInstructors()) {
            tableModel.addRow(new Object[]{i.getInstructorId(), i.getName(), i.getEmail(), i.getDepartment()});
        }
    }
}
