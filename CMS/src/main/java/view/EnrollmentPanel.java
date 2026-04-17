package view;

import controller.EnrollmentController;
import controller.StudentController;
import controller.UnitOfferingController;
import model.Enrollment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class EnrollmentPanel extends JPanel {
    private final EnrollmentController controller;
    private final StudentController studentController;
    private final UnitOfferingController offeringController;
    private JComboBox<String> studentCombo, offeringCombo;
    private JTextField filterStudentField, filterOfferingField;
    private DefaultTableModel tableModel;

    public EnrollmentPanel(EnrollmentController controller, StudentController studentController,
                            UnitOfferingController offeringController) {
        this.controller = controller;
        this.studentController = studentController;
        this.offeringController = offeringController;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Enrol form
        JPanel enrollForm = new JPanel(new GridBagLayout());
        enrollForm.setBorder(BorderFactory.createTitledBorder("Enrol Student"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        studentCombo = new JComboBox<>();
        offeringCombo = new JComboBox<>();
        refreshCombos();

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        enrollForm.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        enrollForm.add(studentCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        enrollForm.add(new JLabel("Offering:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        enrollForm.add(offeringCombo, gbc);

        JButton enrollBtn = new JButton("Enrol");
        enrollBtn.setBackground(new Color(0, 102, 204));
        enrollBtn.setForeground(Color.WHITE);
        enrollBtn.addActionListener(e -> enroll());

        JButton refreshBtn = new JButton("Refresh Lists");
        refreshBtn.addActionListener(e -> { refreshCombos(); loadTable(controller.getAllEnrollments()); });

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel enrollBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enrollBtns.add(enrollBtn);
        enrollBtns.add(refreshBtn);
        enrollForm.add(enrollBtns, gbc);

        // Filter form
        JPanel filterForm = new JPanel(new GridBagLayout());
        filterForm.setBorder(BorderFactory.createTitledBorder("Filter Enrolment History"));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(4, 4, 4, 4);
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        filterStudentField = new JTextField(10);
        filterOfferingField = new JTextField(10);

        gbc2.gridx = 0; gbc2.gridy = 0; gbc2.weightx = 0;
        filterForm.add(new JLabel("Student ID:"), gbc2);
        gbc2.gridx = 1; gbc2.weightx = 1;
        filterForm.add(filterStudentField, gbc2);
        gbc2.gridx = 0; gbc2.gridy = 1; gbc2.weightx = 0;
        filterForm.add(new JLabel("Offering ID:"), gbc2);
        gbc2.gridx = 1; gbc2.weightx = 1;
        filterForm.add(filterOfferingField, gbc2);

        JButton filterStudBtn = new JButton("Filter by Student");
        filterStudBtn.addActionListener(e ->
                loadTable(controller.filterByStudent(filterStudentField.getText().trim())));
        JButton filterOffBtn = new JButton("Filter by Offering");
        filterOffBtn.addActionListener(e ->
                loadTable(controller.filterByOffering(filterOfferingField.getText().trim())));
        JButton allBtn = new JButton("Show All");
        allBtn.addActionListener(e -> loadTable(controller.getAllEnrollments()));

        gbc2.gridx = 0; gbc2.gridy = 2; gbc2.gridwidth = 2;
        JPanel filterBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterBtns.add(filterStudBtn);
        filterBtns.add(filterOffBtn);
        filterBtns.add(allBtn);
        filterForm.add(filterBtns, gbc2);

        topPanel.add(enrollForm);
        topPanel.add(filterForm);

        String[] cols = {"Enrolment ID", "Student ID", "Offering ID", "Date Enrolled"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadTable(controller.getAllEnrollments());
    }

    private void refreshCombos() {
        studentCombo.removeAllItems();
        for (var s : studentController.getAllStudents()) {
            studentCombo.addItem(s.getStudentId() + " - " + s.getName());
        }
        offeringCombo.removeAllItems();
        for (var o : offeringController.getAllOfferings()) {
            offeringCombo.addItem(o.getOfferingId() + " | " + o.getUnitCode() + " " + o.getSemester() + " " + o.getYear());
        }
    }

    private void enroll() {
        if (studentCombo.getSelectedItem() == null || offeringCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a student and an offering.");
            return;
        }
        String studentId = studentCombo.getSelectedItem().toString().split(" - ")[0];
        String offeringId = offeringCombo.getSelectedItem().toString().split(" \\| ")[0];
        if (controller.enroll(studentId, offeringId)) {
            JOptionPane.showMessageDialog(this, "Student enrolled successfully.");
            loadTable(controller.getAllEnrollments());
        } else {
            JOptionPane.showMessageDialog(this, "Student is already enrolled in this offering.");
        }
    }

    private void loadTable(ArrayList<Enrollment> enrollments) {
        tableModel.setRowCount(0);
        for (Enrollment e : enrollments) {
            tableModel.addRow(new Object[]{e.getEnrollmentId(), e.getStudentId(), e.getOfferingId(), e.getDate()});
        }
    }
}
