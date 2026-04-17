package view;

import controller.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        UnitController unitController = new UnitController();
        UnitOfferingController offeringController = new UnitOfferingController();
        InstructorController instructorController = new InstructorController();
        StudentController studentController = new StudentController();
        EnrollmentController enrollmentController = new EnrollmentController();

        setTitle("College Management System — BN231");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("College Management System", SwingConstants.CENTER);
        header.setFont(new Font("Calibri", Font.BOLD, 24));
        header.setBackground(new Color(0, 70, 140));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Calibri", Font.PLAIN, 13));

        tabs.addTab("📋 Units", new UnitPanel(unitController));
        tabs.addTab("📅 Offerings", new UnitOfferingPanel(offeringController, unitController, instructorController));
        tabs.addTab("👨‍🏫 Instructors", new InstructorPanel(instructorController, offeringController));
        tabs.addTab("🎓 Students", new StudentPanel(studentController));
        tabs.addTab("📝 Enrolment", new EnrollmentPanel(enrollmentController, studentController, offeringController));
        tabs.addTab("📊 Reports", new ReportPanel(enrollmentController, studentController, offeringController, unitController, instructorController));
        tabs.addTab("⚙ Algorithms", new AlgorithmsDemoPanel(unitController, studentController));

        add(tabs, BorderLayout.CENTER);

        JLabel footer = new JLabel("BN231 — Software Development Skills and Tools  |  MVC Architecture  |  ArrayList + File I/O  |  Search & Sort Algorithms", SwingConstants.CENTER);
        footer.setFont(new Font("Calibri", Font.ITALIC, 11));
        footer.setForeground(new Color(80, 80, 80));
        footer.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }
}
