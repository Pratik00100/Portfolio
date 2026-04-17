package controller;

import model.Unit;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnitControllerTest {

    private UnitController controller;
    private static final String TEST_FILE = "data/test_units.csv";

    @BeforeEach
    void setUp() {
        new File(TEST_FILE).delete();
        controller = new UnitController(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    @Order(1)
    @DisplayName("Add a new unit successfully")
    void testAddUnit() {
        Unit unit = new Unit("CS101", "Intro to Computing", 3, "Fundamentals of CS", new ArrayList<>());
        boolean result = controller.addUnit(unit);
        assertTrue(result, "Expected addUnit to return true for a new unit");
        assertEquals(1, controller.getAllUnits().size(), "Expected exactly one unit in the list");
    }

    @Test
    @Order(2)
    @DisplayName("Reject duplicate unit code")
    void testAddDuplicateUnit() {
        controller.addUnit(new Unit("CS101", "Intro to Computing", 3, "", new ArrayList<>()));
        boolean result = controller.addUnit(new Unit("CS101", "Duplicate Unit", 3, "", new ArrayList<>()));
        assertFalse(result, "Expected addUnit to return false for a duplicate code");
        assertEquals(1, controller.getAllUnits().size(), "List should still have only one unit");
    }

    @Test
    @Order(3)
    @DisplayName("Find a unit by its code using linear search")
    void testFindByCode() {
        controller.addUnit(new Unit("BN201", "Business Networking", 3, "Core unit", new ArrayList<>()));
        Unit found = controller.findByCode("BN201");
        assertNotNull(found, "Expected to find the unit by code");
        assertEquals("Business Networking", found.getUnitName());
    }

    @Test
    @Order(4)
    @DisplayName("Return null when unit code does not exist")
    void testFindByCodeNotFound() {
        Unit found = controller.findByCode("NOTEXIST");
        assertNull(found, "Expected null for a code that does not exist");
    }

    @Test
    @Order(5)
    @DisplayName("Bubble sort returns units in alphabetical name order")
    void testSortedByName() {
        controller.addUnit(new Unit("CS301", "Zebra Networks", 3, "", new ArrayList<>()));
        controller.addUnit(new Unit("CS101", "Alpha Computing", 3, "", new ArrayList<>()));
        controller.addUnit(new Unit("CS201", "Middle Theory", 3, "", new ArrayList<>()));
        ArrayList<Unit> sorted = controller.getSortedByName();
        assertEquals("Alpha Computing", sorted.get(0).getUnitName());
        assertEquals("Middle Theory", sorted.get(1).getUnitName());
        assertEquals("Zebra Networks", sorted.get(2).getUnitName());
    }

    @Test
    @Order(6)
    @DisplayName("Search units returns matching results only")
    void testSearchUnits() {
        controller.addUnit(new Unit("BN101", "Business Networks", 3, "", new ArrayList<>()));
        controller.addUnit(new Unit("CS101", "Computer Science", 3, "", new ArrayList<>()));
        ArrayList<Unit> results = controller.searchUnits("BN");
        assertEquals(1, results.size());
        assertEquals("BN101", results.get(0).getUnitCode());
    }
}
