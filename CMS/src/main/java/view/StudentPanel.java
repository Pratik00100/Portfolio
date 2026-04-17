package view;

import controller.StudentController;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentPanel extends JPanel {
    private final StudentController controller;
    private JTextField idField, nameField, emailField, yearField, searchField;
    private DefaultTableModel tableModel;

    public StudentPanel(StudentController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Register Student"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField(10);
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        yearField = new JTextField(6);
        searchField = new JTextField(15);

        addFormRow(form, gbc, 0, "Student ID:", idField);
        addFormRow(form, gbc, 1, "Full Name:", nameField);
        addFormRow(form, gbc, 2, "Email:", emailField);
        addFormRow(form, gbc, 3, "Enrolment Year:", yearField);

        JButton regBtn = new JButton("Register");
        regBtn.setBackground(new Color(0, 153, 76));
        regBtn.setForeground(Color.WHITE);
        regBtn.addActionListener(e -> registerStudent());

        JButton sortBtn = new JButton("Sort by Name");
        sortBtn.addActionListener(e -> loadTable(controller.getSortedByName()));

        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> loadTable(controller.searchStudents(searchField.getText().trim())));

        JButton showAllBtn = new JButton("Show All");
        showAllBtn.addActionListener(e -> loadTable(controller.getAllStudents()));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btns.add(regBtn);
        btns.add(new JLabel("  Search:"));
        btns.add(searchField);
        btns.add(searchBtn);
        btns.add(sortBtn);
        btns.add(showAllBtn);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        form.add(btns, gbc);

        String[] cols = {"Student ID", "Name", "Email", "Enrolment Year"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadTable(controller.getAllStudents());
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        p.add(field, gbc);
    }

    private void registerStudent() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String yearStr = yearField.getText().trim();
        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || yearStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }
        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enrolment Year must be a number.");
            return;
        }
        Student student = new Student(id, name, email, year);
        if (controller.addStudent(student)) {
            JOptionPane.showMessageDialog(this, "Student registered successfully.");
            idField.setText(""); nameField.setText(""); emailField.setText(""); yearField.setText("");
            loadTable(controller.getAllStudents());
        } else {
            JOptionPane.showMessageDialog(this, "Student ID already exists.");
        }
    }

    private void loadTable(ArrayList<Student> students) {
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{s.getStudentId(), s.getName(), s.getEmail(), s.getEnrollmentYear()});
        }
    }
}
