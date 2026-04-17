package model;

public class UnitOffering {
    private String offeringId;
    private String unitCode;
    private String semester;
    private int year;
    private String instructorId;

    public UnitOffering(String offeringId, String unitCode, String semester, int year, String instructorId) {
        this.offeringId = offeringId;
        this.unitCode = unitCode;
        this.semester = semester;
        this.year = year;
        this.instructorId = instructorId;
    }

    public String getOfferingId() { return offeringId; }
    public void setOfferingId(String offeringId) { this.offeringId = offeringId; }
    public String getUnitCode() { return unitCode; }
    public void setUnitCode(String unitCode) { this.unitCode = unitCode; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }

    public String toCSV() {
        return offeringId + "," + unitCode + "," + semester + "," + year + ","
                + (instructorId != null ? instructorId : "");
    }

    public static UnitOffering fromCSV(String line) {
        String[] parts = line.split(",", 5);
        String instrId = (parts.length == 5 && !parts[4].trim().isEmpty()) ? parts[4].trim() : null;
        return new UnitOffering(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                Integer.parseInt(parts[3].trim()), instrId);
    }

    @Override
    public String toString() {
        return offeringId + " | " + unitCode + " | " + semester + " " + year;
    }
}
