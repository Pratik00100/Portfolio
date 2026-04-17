package controller;

import model.Enrollment;
import util.FileManager;
import util.SearchSortUtil;

import java.time.LocalDate;
import java.util.ArrayList;

public class EnrollmentController {
    private ArrayList<Enrollment> enrollments;
    private final String filePath;

    public EnrollmentController() {
        this("data/enrollments.csv");
    }

    public EnrollmentController(String filePath) {
        this.filePath = filePath;
        enrollments = FileManager.loadEnrollments(filePath);
    }

    public boolean enroll(String studentId, String offeringId) {
        for (Enrollment e : enrollments) {
            if (e.getStudentId().equals(studentId) && e.getOfferingId().equals(offeringId)) return false;
        }
        String id = "ENR" + (enrollments.size() + 1);
        Enrollment enrollment = new Enrollment(id, studentId, offeringId, LocalDate.now().toString());
        enrollments.add(enrollment);
        FileManager.saveEnrollments(enrollments, filePath);
        return true;
    }

    public ArrayList<Enrollment> getAllEnrollments() {
        return enrollments;
    }

    public ArrayList<Enrollment> filterByOffering(String offeringId) {
        ArrayList<Enrollment> result = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.getOfferingId().equals(offeringId)) result.add(e);
        }
        return result;
    }

    public ArrayList<Enrollment> filterByStudent(String studentId) {
        ArrayList<Enrollment> result = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.getStudentId().equals(studentId)) result.add(e);
        }
        return result;
    }

    public Enrollment findById(String enrollmentId) {
        return SearchSortUtil.linearSearchEnrollment(enrollments, enrollmentId);
    }
}
