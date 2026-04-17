package controller;

import model.Student;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentControllerTest {

    private StudentController controller;
    private static final String TEST_FILE = "data/test_students.csv";

    @BeforeEach
    void setUp() {
        new File(TEST_FILE).delete();
        controller = new StudentController(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    @Order(1)
    @DisplayName("Register a new student successfully")
    void testAddStudent() {
        Student student = new Student("S001", "Alice Johnson", "alice@college.edu", 2024);
        boolean result = controller.addStudent(student);
        assertTrue(result, "Expected addStudent to return true");
        assertEquals(1, controller.getAllStudents().size());
    }

    @Test
    @Order(2)
    @DisplayName("Reject duplicate student ID")
    void testAddDuplicateStudent() {
        controller.addStudent(new Student("S001", "Alice Johnson", "alice@college.edu", 2024));
        boolean result = controller.addStudent(new Student("S001", "Bob Smith", "bob@college.edu", 2024));
        assertFalse(result, "Expected false when registering with an existing ID");
        assertEquals(1, controller.getAllStudents().size());
    }

    @Test
    @Order(3)
    @DisplayName("Find student by ID using linear search")
    void testFindById() {
        controller.addStudent(new Student("S002", "Bob Smith", "bob@college.edu", 2023));
        Student found = controller.findById("S002");
        assertNotNull(found);
        assertEquals("Bob Smith", found.getName());
        assertEquals("bob@college.edu", found.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("Return null for a non-existent student ID")
    void testFindByIdNotFound() {
        Student found = controller.findById("S999");
        assertNull(found);
    }

    @Test
    @Order(5)
    @DisplayName("Bubble sort returns students in alphabetical name order")
    void testSortedByName() {
        controller.addStudent(new Student("S003", "Zara Ahmed", "z@college.edu", 2024));
        controller.addStudent(new Student("S001", "Alice Johnson", "a@college.edu", 2024));
        controller.addStudent(new Student("S002", "Mike Chen", "m@college.edu", 2024));
        ArrayList<Student> sorted = controller.getSortedByName();
        assertEquals("Alice Johnson", sorted.get(0).getName());
        assertEquals("Mike Chen", sorted.get(1).getName());
        assertEquals("Zara Ahmed", sorted.get(2).getName());
    }

    @Test
    @Order(6)
    @DisplayName("Search students by name keyword")
    void testSearchStudents() {
        controller.addStudent(new Student("S001", "Alice Johnson", "a@college.edu", 2024));
        controller.addStudent(new Student("S002", "Bob Smith", "b@college.edu", 2024));
        ArrayList<Student> results = controller.searchStudents("alice");
        assertEquals(1, results.size());
        assertEquals("S001", results.get(0).getStudentId());
    }
}
