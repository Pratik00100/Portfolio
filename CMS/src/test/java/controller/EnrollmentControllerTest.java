package controller;

import model.Enrollment;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EnrollmentControllerTest {

    private EnrollmentController controller;
    private static final String TEST_FILE = "data/test_enrollments.csv";

    @BeforeEach
    void setUp() {
        new File(TEST_FILE).delete();
        controller = new EnrollmentController(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    @Order(1)
    @DisplayName("Enrol a student in an offering successfully")
    void testEnroll() {
        boolean result = controller.enroll("S001", "OFF001");
        assertTrue(result, "Expected enrol to succeed");
        assertEquals(1, controller.getAllEnrollments().size());
    }

    @Test
    @Order(2)
    @DisplayName("Prevent duplicate enrolment in the same offering")
    void testDuplicateEnroll() {
        controller.enroll("S001", "OFF001");
        boolean result = controller.enroll("S001", "OFF001");
        assertFalse(result, "Expected false when enrolling same student in same offering twice");
        assertEquals(1, controller.getAllEnrollments().size());
    }

    @Test
    @Order(3)
    @DisplayName("Same student can enrol in different offerings")
    void testStudentMultipleOfferings() {
        controller.enroll("S001", "OFF001");
        boolean result = controller.enroll("S001", "OFF002");
        assertTrue(result, "Expected student to enrol in a second offering");
        assertEquals(2, controller.getAllEnrollments().size());
    }

    @Test
    @Order(4)
    @DisplayName("Filter enrolments by student ID")
    void testFilterByStudent() {
        controller.enroll("S001", "OFF001");
        controller.enroll("S001", "OFF002");
        controller.enroll("S002", "OFF001");
        ArrayList<Enrollment> result = controller.filterByStudent("S001");
        assertEquals(2, result.size(), "Expected 2 enrolments for S001");
        result.forEach(e -> assertEquals("S001", e.getStudentId()));
    }

    @Test
    @Order(5)
    @DisplayName("Filter enrolments by offering ID")
    void testFilterByOffering() {
        controller.enroll("S001", "OFF001");
        controller.enroll("S002", "OFF001");
        controller.enroll("S001", "OFF002");
        ArrayList<Enrollment> result = controller.filterByOffering("OFF001");
        assertEquals(2, result.size(), "Expected 2 enrolments in OFF001");
        result.forEach(e -> assertEquals("OFF001", e.getOfferingId()));
    }

    @Test
    @Order(6)
    @DisplayName("Find an enrolment record by its ID")
    void testFindById() {
        controller.enroll("S001", "OFF001");
        Enrollment found = controller.findById("ENR1");
        assertNotNull(found);
        assertEquals("S001", found.getStudentId());
        assertEquals("OFF001", found.getOfferingId());
    }
}
