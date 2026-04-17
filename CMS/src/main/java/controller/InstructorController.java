package controller;

import model.Instructor;
import model.UnitOffering;
import util.FileManager;
import util.SearchSortUtil;

import java.util.ArrayList;

public class InstructorController {
    private ArrayList<Instructor> instructors;
    private final String filePath;

    public InstructorController() {
        this("data/instructors.csv");
    }

    public InstructorController(String filePath) {
        this.filePath = filePath;
        instructors = FileManager.loadInstructors(filePath);
    }

    public boolean addInstructor(Instructor instructor) {
        for (Instructor i : instructors) {
            if (i.getInstructorId().equalsIgnoreCase(instructor.getInstructorId())) return false;
        }
        instructors.add(instructor);
        FileManager.saveInstructors(instructors, filePath);
        return true;
    }

    public ArrayList<Instructor> getAllInstructors() {
        return instructors;
    }

    public Instructor findById(String id) {
        return SearchSortUtil.linearSearchInstructor(instructors, id);
    }

    public boolean assignToOffering(String instructorId, UnitOffering offering) {
        if (findById(instructorId) == null) return false;
        offering.setInstructorId(instructorId);
        return true;
    }

    public ArrayList<Instructor> getSortedByName() {
        ArrayList<Instructor> copy = new ArrayList<>(instructors);
        SearchSortUtil.insertionSortInstructors(copy);
        return copy;
    }
}
