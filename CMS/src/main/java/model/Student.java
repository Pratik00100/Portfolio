package model;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private int enrollmentYear;

    public Student(String studentId, String name, String email, int enrollmentYear) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.enrollmentYear = enrollmentYear;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
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
    public String toString() {
        return studentId + " - " + name;
    }
}
