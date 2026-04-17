# BN231 Assignment 2 — Report Content
## College Management System (CMS)

---

## Part A: Conceptual Questions and Software Design [15 Marks]

---

### Q1: Object-Oriented Design Principles [3 Marks]

#### 1.1 Low Coupling and High Cohesion

**Low Coupling** refers to minimising dependencies between classes so that changes in one class have minimal impact on others. **High Cohesion** means each class has a well-defined, focused responsibility.

In the CMS:

- **Low Coupling example:** The `UnitController` class depends only on the `Unit` model and `FileManager` utility. It has no knowledge of any `JPanel` or GUI component. If the view is completely redesigned, `UnitController` requires zero changes. This is demonstrated in our code where controllers communicate with views only through method return values (e.g., `ArrayList<Unit> getAllUnits()`), never through direct GUI references.

- **High Cohesion example:** `FileManager.java` has a single responsibility — reading and writing CSV data to disk. It does not perform sorting, validation, or GUI operations. Similarly, `SearchSortUtil.java` only contains algorithm implementations. Each class "does one thing well."

#### 1.2 SOLID Principles

| Principle | Description | CMS Example |
|---|---|---|
| **S** — Single Responsibility | A class should have only one reason to change | `StudentController` only manages student business logic; `StudentPanel` only handles display |
| **O** — Open/Closed | Open for extension, closed for modification | New data types (e.g., `Assessment`) can be added with new Model/Controller/View classes without changing existing ones |
| **L** — Liskov Substitution | Subtypes must be substitutable for their base types | `Student` and `Instructor` both implement the `Person` interface; anywhere a `Person` is expected, either can be used |
| **I** — Interface Segregation | Clients should not be forced to depend on interfaces they do not use | The `Person` interface exposes only `getName()` and `getEmail()` — small and focused, not a "fat" interface |
| **D** — Dependency Inversion | Depend on abstractions, not concretions | Controllers depend on `ArrayList<Model>` abstraction; storage could change from CSV to a database without modifying controller logic |

**Code example — Liskov Substitution:**
```java
public interface Person {
    String getName();
    String getEmail();
}

public class Student implements Person { ... }
public class Instructor implements Person { ... }

// Any method can accept Person and work with either:
private void displayPerson(Person p) {
    System.out.println(p.getName() + " — " + p.getEmail());
}
```

---

### Q2 (Part 1): Design Pattern Types [3 Marks]

Design patterns are reusable solutions to commonly occurring software design problems. They are classified into three types:

#### Creational Patterns
Control object creation to increase flexibility and reuse.

**Example — Factory Method:** Rather than constructing objects directly with `new`, a factory method is used. In our CMS, each model class provides a static `fromCSV(String line)` factory method:

```java
// Factory Method — creates a Unit from a data string
public static Unit fromCSV(String line) {
    String[] parts = line.split(",", 5);
    return new Unit(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], prereqs);
}
```
The caller (`FileManager`) does not need to know the internal structure of `Unit`; it just calls `Unit.fromCSV(line)`.

#### Structural Patterns
Concerned with object composition and simplifying structure.

**Example — Facade:** The `EnrollmentController` acts as a Facade between the complex subsystem (file I/O, duplicate checking, ID generation, date stamping) and the `EnrollmentPanel` view. The view simply calls `controller.enroll(studentId, offeringId)` without knowing about the internal steps:

```java
// Facade — hides complexity from the view
public boolean enroll(String studentId, String offeringId) {
    // 1. Duplicate check
    // 2. Generate unique ID
    // 3. Stamp current date
    // 4. Persist to file
    // All hidden from the caller
}
```

#### Behavioral Patterns
Focus on communication and responsibilities between objects.

**Example — Strategy:** `SearchSortUtil` implements multiple interchangeable sorting strategies (Bubble Sort and Insertion Sort) and search strategies (Linear and Binary). The caller can choose which algorithm to apply at runtime, e.g., `SearchSortUtil.bubbleSortUnits(list)` vs `SearchSortUtil.insertionSortInstructors(list)`. This follows the Strategy pattern — the algorithm is selected independently of the data it operates on.

---

### Q3: Test Plan with JUnit [3 Marks]

#### Test Objectives
Verify that the three core controller classes correctly manage data integrity, enforce business rules (no duplicates), and return accurate results from search and sort operations.

#### Test Environment
- **Framework:** JUnit 5 (Jupiter)
- **Build Tool:** Maven 3.9
- **Isolation:** Each test uses a unique temporary CSV file that is deleted before and after each test method (`@BeforeEach` / `@AfterEach`)

---

#### Class 1: `UnitController`

| Test ID | Test Method | Input | Expected Outcome |
|---|---|---|---|
| UC-01 | `testAddUnit()` | New unit "CS101" | Returns `true`; list size = 1 |
| UC-02 | `testAddDuplicateUnit()` | Duplicate code "CS101" | Returns `false`; list size stays 1 |
| UC-03 | `testFindByCode()` | Code "BN201" | Returns correct `Unit` object |
| UC-04 | `testFindByCodeNotFound()` | Code "NOTEXIST" | Returns `null` |
| UC-05 | `testSortedByName()` | 3 units in random order | Returns list in ascending alphabetical order |
| UC-06 | `testSearchUnits()` | Keyword "BN" | Returns only units matching the keyword |

---

#### Class 2: `StudentController`

| Test ID | Test Method | Input | Expected Outcome |
|---|---|---|---|
| SC-01 | `testAddStudent()` | New student "S001" | Returns `true`; list size = 1 |
| SC-02 | `testAddDuplicateStudent()` | Duplicate ID "S001" | Returns `false`; list size stays 1 |
| SC-03 | `testFindById()` | ID "S002" | Returns correct `Student` object |
| SC-04 | `testFindByIdNotFound()` | ID "S999" | Returns `null` |
| SC-05 | `testSortedByName()` | 3 students in random order | Returns alphabetically sorted list |
| SC-06 | `testSearchStudents()` | Keyword "alice" (case-insensitive) | Returns matching student only |

---

#### Class 3: `EnrollmentController`

| Test ID | Test Method | Input | Expected Outcome |
|---|---|---|---|
| EC-01 | `testEnroll()` | Student "S001", Offering "OFF001" | Returns `true`; 1 enrollment recorded |
| EC-02 | `testDuplicateEnroll()` | Same student + offering | Returns `false`; prevents duplicate |
| EC-03 | `testStudentMultipleOfferings()` | Same student, different offering | Returns `true`; correctly allows |
| EC-04 | `testFilterByStudent()` | Filter by "S001" | Returns only enrollments for S001 |
| EC-05 | `testFilterByOffering()` | Filter by "OFF001" | Returns only enrollments in OFF001 |
| EC-06 | `testFindById()` | ID "ENR1" | Returns correct `Enrollment` object |

**Total: 18 tests — all passing (0 failures, 0 errors)**

---

### Q2 (Part 2): UML Class Diagram

See the file `UML_DIAGRAM.puml` for the complete PlantUML source.
To render: paste the content into https://www.plantuml.com/plantuml/uml/ or use the PlantUML VS Code extension.

Key relationships:
- `Student` and `Instructor` implement the `Person` interface
- `Enrollment` associates `Student` with `UnitOffering` (many-to-many resolved)
- `UnitOffering` references `Unit` (via unitCode) and optionally `Instructor`
- Each controller manages one `ArrayList` of its corresponding model type
- `FileManager` is used by all controllers for persistence
- `SearchSortUtil` is used by `UnitController`, `StudentController`, and `InstructorController`

---

## References (IEEE Style)

[1] E. Gamma, R. Helm, R. Johnson, and J. Vlissides, *Design Patterns: Elements of Reusable Object-Oriented Software*. Reading, MA: Addison-Wesley, 1994.

[2] R. Martin, *Clean Code: A Handbook of Agile Software Craftsmanship*. Upper Saddle River, NJ: Prentice Hall, 2008.

[3] Oracle, "The Java Tutorials — Interfaces," Oracle Java Documentation, 2023. [Online]. Available: https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html

[4] T. H. Cormen, C. E. Leiserson, R. L. Rivest, and C. Stein, *Introduction to Algorithms*, 4th ed. Cambridge, MA: MIT Press, 2022.

[5] G. Booch, R. Maksimchuk, M. Engle, B. Young, J. Conallen, and K. Houston, *Object-Oriented Analysis and Design with Applications*, 3rd ed. Boston, MA: Addison-Wesley, 2007.

[6] JUnit Team, "JUnit 5 User Guide," JUnit.org, 2023. [Online]. Available: https://junit.org/junit5/docs/current/user-guide/
