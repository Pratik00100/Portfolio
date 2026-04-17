package controller;

import model.Student;
import util.FileManager;
import util.SearchSortUtil;

import java.util.ArrayList;

public class StudentController {
    private ArrayList<Student> students;
    private final String filePath;

    public StudentController() {
        this("data/students.csv");
    }

    public StudentController(String filePath) {
        this.filePath = filePath;
        students = FileManager.loadStudents(filePath);
    }

    public boolean addStudent(Student student) {
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(student.getStudentId())) return false;
        }
        students.add(student);
        FileManager.saveStudents(students, filePath);
        return true;
    }

    public ArrayList<Student> getAllStudents() {
        return students;
    }

    public Student findById(String id) {
        return SearchSortUtil.linearSearchStudent(students, id);
    }

    public ArrayList<Student> getSortedByName() {
        ArrayList<Student> copy = new ArrayList<>(students);
        SearchSortUtil.bubbleSortStudents(copy);
        return copy;
    }

    public ArrayList<Student> searchStudents(String keyword) {
        ArrayList<Student> result = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (Student s : students) {
            if (s.getStudentId().toLowerCase().contains(kw) || s.getName().toLowerCase().contains(kw)) {
                result.add(s);
            }
        }
        return result;
    }
}
