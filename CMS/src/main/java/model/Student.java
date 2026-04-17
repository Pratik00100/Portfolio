package model;

import java.util.Objects;

public class Student implements Person {
    private String studentId;
    private String name;
    private String email;
    private int enrollmentYear;

    public Student(String studentId, String name, String email, int enrollmentYear) {
        if (studentId == null || studentId.isBlank()) throw new IllegalArgumentException("Student ID cannot be empty");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty");
        this.studentId = studentId.trim();
        this.name = name.trim();
        this.email = email != null ? email.trim() : "";
        this.enrollmentYear = enrollmentYear;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    @Override public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    @Override public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(int enrollmentYear) { this.enrollmentYear = enrollmentYear; }

    public String toCSV() {
        return studentId + "," + name + "," + email + "," + enrollmentYear;
    }

    public static Student fromCSV(String line) {
        String[] parts = line.split(",", 4);
        return new Student(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                Integer.parseInt(parts[3].trim()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student s)) return false;
        return studentId.equalsIgnoreCase(s.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId.toLowerCase());
    }

    @Override
    public String toString() {
        return studentId + " - " + name;
    }
}
