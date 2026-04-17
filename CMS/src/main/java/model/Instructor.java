package model;

public class Instructor {
    private String instructorId;
    private String name;
    private String email;
    private String department;

    public Instructor(String instructorId, String name, String email, String department) {
        this.instructorId = instructorId;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String toCSV() {
        return instructorId + "," + name + "," + email + "," + department;
    }

    public static Instructor fromCSV(String line) {
        String[] parts = line.split(",", 4);
        return new Instructor(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
    }

    @Override
    public String toString() {
        return instructorId + " - " + name + " (" + department + ")";
    }
}
