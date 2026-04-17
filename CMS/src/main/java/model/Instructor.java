package model;

import java.util.Objects;

public class Instructor implements Person {
    private String instructorId;
    private String name;
    private String email;
    private String department;

    public Instructor(String instructorId, String name, String email, String department) {
        if (instructorId == null || instructorId.isBlank()) throw new IllegalArgumentException("Instructor ID cannot be empty");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty");
        this.instructorId = instructorId.trim();
        this.name = name.trim();
        this.email = email != null ? email.trim() : "";
        this.department = department != null ? department.trim() : "";
    }

    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }
    @Override public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    @Override public String getEmail() { return email; }
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instructor i)) return false;
        return instructorId.equalsIgnoreCase(i.instructorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instructorId.toLowerCase());
    }

    @Override
    public String toString() {
        return instructorId + " - " + name + " (" + department + ")";
    }
}
