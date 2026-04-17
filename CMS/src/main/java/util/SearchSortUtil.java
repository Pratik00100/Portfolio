package util;

import model.*;

import java.util.ArrayList;

public class SearchSortUtil {

    // --- Linear Search ---

    public static Unit linearSearchUnit(ArrayList<Unit> units, String code) {
        for (Unit u : units) {
            if (u.getUnitCode().equalsIgnoreCase(code)) return u;
        }
        return null;
    }

    public static Student linearSearchStudent(ArrayList<Student> students, String id) {
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(id)) return s;
        }
        return null;
    }

    public static Instructor linearSearchInstructor(ArrayList<Instructor> instructors, String id) {
        for (Instructor i : instructors) {
            if (i.getInstructorId().equalsIgnoreCase(id)) return i;
        }
        return null;
    }

    public static Enrollment linearSearchEnrollment(ArrayList<Enrollment> enrollments, String id) {
        for (Enrollment e : enrollments) {
            if (e.getEnrollmentId().equalsIgnoreCase(id)) return e;
        }
        return null;
    }

    // --- Binary Search (requires list sorted by unitCode) ---

    public static Unit binarySearchUnit(ArrayList<Unit> sortedUnits, String code) {
        int left = 0, right = sortedUnits.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = sortedUnits.get(mid).getUnitCode().compareToIgnoreCase(code);
            if (cmp == 0) return sortedUnits.get(mid);
            else if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }

    // --- Bubble Sort ---

    public static void bubbleSortUnits(ArrayList<Unit> units) {
        int n = units.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (units.get(j).getUnitName().compareToIgnoreCase(units.get(j + 1).getUnitName()) > 0) {
                    Unit tmp = units.get(j);
                    units.set(j, units.get(j + 1));
                    units.set(j + 1, tmp);
                }
            }
        }
    }

    public static void bubbleSortStudents(ArrayList<Student> students) {
        int n = students.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (students.get(j).getName().compareToIgnoreCase(students.get(j + 1).getName()) > 0) {
                    Student tmp = students.get(j);
                    students.set(j, students.get(j + 1));
                    students.set(j + 1, tmp);
                }
            }
        }
    }

    // --- Insertion Sort ---

    public static void insertionSortInstructors(ArrayList<Instructor> instructors) {
        int n = instructors.size();
        for (int i = 1; i < n; i++) {
            Instructor key = instructors.get(i);
            int j = i - 1;
            while (j >= 0 && instructors.get(j).getName().compareToIgnoreCase(key.getName()) > 0) {
                instructors.set(j + 1, instructors.get(j));
                j--;
            }
            instructors.set(j + 1, key);
        }
    }
}
