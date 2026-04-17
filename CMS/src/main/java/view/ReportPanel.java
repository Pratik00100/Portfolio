package view;

import controller.*;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ReportPanel extends JPanel {
    private final EnrollmentController enrollmentController;
    private final StudentController studentController;
    private final UnitOfferingController offeringController;
    private final UnitController unitController;
    private final InstructorController instructorController;
    private JTextArea reportArea;

    public ReportPanel(EnrollmentController enrollmentController, StudentController studentController,
                        UnitOfferingController offeringController, UnitController unitController,
                        InstructorController instructorController) {
        this.enrollmentController = enrollmentController;
        this.studentController = studentController;
        this.offeringController = offeringController;
        this.unitController = unitController;
        this.instructorController = instructorController;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBorder(BorderFactory.createTitledBorder("Generate Reports"));

        JButton enrolReport = new JButton("Student Enrolment Report");
        enrolReport.addActionListener(e -> generateEnrolmentReport());

        JButton classReport = new JButton("Class Allocation Report");
        classReport.addActionListener(e -> generateClassReport());

        JButton summaryReport = new JButton("System Summary");
        summaryReport.addActionListener(e -> generateSummary());

        btnPanel.add(enrolReport);
        btnPanel.add(classReport);
        btnPanel.add(summaryReport);

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportArea.setMargin(new Insets(8, 8, 8, 8));

        add(btnPanel, BorderLayout.NORTH);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        generateSummary();
    }

    private void generateEnrolmentReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=================================================\n");
        sb.append("           STUDENT ENROLMENT REPORT              \n");
        sb.append("=================================================\n\n");
        for (Student student : studentController.getAllStudents()) {
            sb.append("Student: ").append(student.getName())
              .append("  (ID: ").append(student.getStudentId()).append(")\n");
            ArrayList<Enrollment> enrolments = enrollmentController.filterByStudent(student.getStudentId());
            if (enrolments.isEmpty()) {
                sb.append("  [No enrolments recorded]\n");
            } else {
                for (Enrollment e : enrolments) {
                    UnitOffering off = offeringController.findById(e.getOfferingId());
                    String offStr = off != null
                            ? off.getUnitCode() + " (" + off.getSemester() + " " + off.getYear() + ")"
                            : e.getOfferingId();
                    sb.append("  - ").append(offStr).append("  |  Enrolled: ").append(e.getDate()).append("\n");
                }
            }
            sb.append("\n");
        }
        reportArea.setText(sb.toString());
    }

    private void generateClassReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=================================================\n");
        sb.append("           CLASS ALLOCATION REPORT               \n");
        sb.append("=================================================\n\n");
        for (UnitOffering offering : offeringController.getAllOfferings()) {
            sb.append("Offering: ").append(offering.getOfferingId())
              .append("  |  Unit: ").append(offering.getUnitCode())
              .append("  |  ").append(offering.getSemester()).append(" ").append(offering.getYear()).append("\n");
            String instrId = offering.getInstructorId();
            if (instrId != null && !instrId.isEmpty()) {
                Instructor instr = instructorController.findById(instrId);
                sb.append("  Instructor: ").append(instr != null ? instr.getName() + " (" + instrId + ")" : instrId).append("\n");
            } else {
                sb.append("  Instructor: Not assigned\n");
            }
            ArrayList<Enrollment> enrolments = enrollmentController.filterByOffering(offering.getOfferingId());
            sb.append("  Students Enrolled: ").append(enrolments.size()).append("\n");
            for (Enrollment e : enrolments) {
                Student st = studentController.findById(e.getStudentId());
                sb.append("    * ").append(st != null ? st.getName() : e.getStudentId()).append("\n");
            }
            sb.append("\n");
        }
        reportArea.setText(sb.toString());
    }

    private void generateSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=================================================\n");
        sb.append("                SYSTEM SUMMARY                   \n");
        sb.append("=================================================\n\n");
        sb.append(String.format("  %-25s %d%n", "Total Units:", unitController.getAllUnits().size()));
        sb.append(String.format("  %-25s %d%n", "Total Unit Offerings:", offeringController.getAllOfferings().size()));
        sb.append(String.format("  %-25s %d%n", "Total Instructors:", instructorController.getAllInstructors().size()));
        sb.append(String.format("  %-25s %d%n", "Total Students:", studentController.getAllStudents().size()));
        sb.append(String.format("  %-25s %d%n", "Total Enrolments:", enrollmentController.getAllEnrollments().size()));
        reportArea.setText(sb.toString());
    }
}
