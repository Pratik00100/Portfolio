package model;

public class Enrollment {
    private String enrollmentId;
    private String studentId;
    private String offeringId;
    private String date;

    public Enrollment(String enrollmentId, String studentId, String offeringId, String date) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.offeringId = offeringId;
        this.date = date;
    }

    public String getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(String enrollmentId) { this.enrollmentId = enrollmentId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getOfferingId() { return offeringId; }
    public void setOfferingId(String offeringId) { this.offeringId = offeringId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String toCSV() {
        return enrollmentId + "," + studentId + "," + offeringId + "," + date;
    }

    public static Enrollment fromCSV(String line) {
        String[] parts = line.split(",", 4);
        return new Enrollment(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
    }

    @Override
    public String toString() {
        return enrollmentId + " | Student: " + studentId + " | Offering: " + offeringId;
    }
}
