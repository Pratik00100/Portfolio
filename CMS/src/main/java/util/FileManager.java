package util;

import model.*;

import java.io.*;
import java.util.ArrayList;

public class FileManager {

    private static void ensureDir(String path) {
        File dir = new File(path).getParentFile();
        if (dir != null && !dir.exists()) dir.mkdirs();
    }

    public static ArrayList<Unit> loadUnits(String path) {
        ArrayList<Unit> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) list.add(Unit.fromCSV(line.trim()));
            }
        } catch (IOException ignored) {}
        return list;
    }

    public static void saveUnits(ArrayList<Unit> units, String path) {
        ensureDir(path);
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (Unit u : units) pw.println(u.toCSV());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static ArrayList<UnitOffering> loadOfferings(String path) {
        ArrayList<UnitOffering> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) list.add(UnitOffering.fromCSV(line.trim()));
            }
        } catch (IOException ignored) {}
        return list;
    }

    public static void saveOfferings(ArrayList<UnitOffering> offerings, String path) {
        ensureDir(path);
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (UnitOffering o : offerings) pw.println(o.toCSV());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static ArrayList<Instructor> loadInstructors(String path) {
        ArrayList<Instructor> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) list.add(Instructor.fromCSV(line.trim()));
            }
        } catch (IOException ignored) {}
        return list;
    }

    public static void saveInstructors(ArrayList<Instructor> instructors, String path) {
        ensureDir(path);
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (Instructor i : instructors) pw.println(i.toCSV());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static ArrayList<Student> loadStudents(String path) {
        ArrayList<Student> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) list.add(Student.fromCSV(line.trim()));
            }
        } catch (IOException ignored) {}
        return list;
    }

    public static void saveStudents(ArrayList<Student> students, String path) {
        ensureDir(path);
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (Student s : students) pw.println(s.toCSV());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static ArrayList<Enrollment> loadEnrollments(String path) {
        ArrayList<Enrollment> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) list.add(Enrollment.fromCSV(line.trim()));
            }
        } catch (IOException ignored) {}
        return list;
    }

    public static void saveEnrollments(ArrayList<Enrollment> enrollments, String path) {
        ensureDir(path);
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (Enrollment e : enrollments) pw.println(e.toCSV());
        } catch (IOException e) { e.printStackTrace(); }
    }
}
