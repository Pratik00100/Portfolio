package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Unit {
    private String unitCode;
    private String unitName;
    private int credits;
    private String description;
    private List<String> prerequisites;

    public Unit(String unitCode, String unitName, int credits, String description, List<String> prerequisites) {
        if (unitCode == null || unitCode.isBlank()) throw new IllegalArgumentException("Unit code cannot be empty");
        if (unitName == null || unitName.isBlank()) throw new IllegalArgumentException("Unit name cannot be empty");
        if (credits <= 0) throw new IllegalArgumentException("Credits must be positive");
        this.unitCode = unitCode.trim().toUpperCase();
        this.unitName = unitName.trim();
        this.credits = credits;
        this.description = description != null ? description.trim() : "";
        this.prerequisites = prerequisites != null ? new ArrayList<>(prerequisites) : new ArrayList<>();
    }

    public String getUnitCode() { return unitCode; }
    public void setUnitCode(String unitCode) { this.unitCode = unitCode; }
    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getPrerequisites() { return prerequisites; }
    public void setPrerequisites(List<String> prerequisites) { this.prerequisites = prerequisites; }

    public String toCSV() {
        return unitCode + "," + unitName + "," + credits + "," + description + ","
                + String.join(";", prerequisites);
    }

    public static Unit fromCSV(String line) {
        String[] parts = line.split(",", 5);
        List<String> prereqs = new ArrayList<>();
        if (parts.length == 5 && !parts[4].trim().isEmpty()) {
            for (String p : parts[4].split(";")) prereqs.add(p.trim());
        }
        return new Unit(parts[0].trim(), parts[1].trim(),
                Integer.parseInt(parts[2].trim()), parts[3].trim(), prereqs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit u)) return false;
        return unitCode.equalsIgnoreCase(u.unitCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitCode.toLowerCase());
    }

    @Override
    public String toString() {
        return unitCode + " - " + unitName + " (" + credits + " credits)";
    }
}
