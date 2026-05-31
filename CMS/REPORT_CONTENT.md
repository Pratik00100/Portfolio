# BN231 — Assignment 2 Report
## Design and Implementation of a College Management System

---

**Melbourne Institute of Technology**
**Campus:** Melbourne
**Trimester:** T1 2026
**Unit Code:** BN231
**Unit Title:** Software Development Skills and Tools
**Assessment:** Assignment 2 — Group Assignment
**Lecturer:** Imtiazuddin Ali Khan
**Due Date:** 31 May 2026

| Student Name | Student ID |
|---|---|
| Pratik Dhami | MIT231942 |
| Joy Nyaundi | MIT223649 |
| Mishel kenath | MIT231379 |

---

## Table of Contents

1. Part A — Q1: OOD Principles and Design Patterns
2. Part A — Q2: UML Class Diagram
3. Part A — Q3: Test Plan with JUnit
4. Part B — Implementation Summary
5. References
6. Individual Contribution Table

---

## Part A — Q1: Object-Oriented Design Principles [3 Marks]

### 1.1 Low Coupling and High Cohesion

**Low Coupling** means minimising dependencies between classes so that changes in one class have minimal impact on others. **High Cohesion** means each class has a single, clearly defined responsibility.

In our College Management System (CMS), these principles are applied throughout:

**Low Coupling Example:**
The `UnitController` class depends only on the `Unit` model and `FileManager` utility. It has no reference to any Swing component or panel. If the entire GUI is redesigned, `UnitController` requires zero changes. Controllers communicate with views only through return values (e.g., `ArrayList<Unit> getAllUnits()`), never through direct GUI references.

```java
// UnitController has NO knowledge of JPanel or Swing
public ArrayList<Unit> getAllUnits() {
    return units;  // View decides how to display this
}
```

**High Cohesion Example:**
`FileManager.java` has exactly one responsibility — reading and writing CSV data to disk. It performs no sorting, validation, or GUI operations. `SearchSortUtil.java` contains only algorithm implementations. Each class "does one thing well."

### 1.2 SOLID Principles

**S — Single Responsibility Principle**
Each class has one reason to change. `StudentController` manages only student business logic. `StudentPanel` manages only how students are displayed. `FileManager` manages only persistence.

```java
// StudentController — only handles student data logic
public boolean addStudent(Student student) {
    for (Student s : students)
        if (s.getStudentId().equalsIgnoreCase(student.getStudentId())) return false;
    students.add(student);
    FileManager.saveStudents(students, filePath);
    return true;
}
```

**O — Open/Closed Principle**
The system is open for extension but closed for modification. A new entity type (e.g., `Assessment`) can be added by creating new Model, Controller, and View classes without modifying any existing class.

**L — Liskov Substitution Principle**
`Student` and `Instructor` both implement the `Person` interface. Anywhere a `Person` reference is used, either object can be substituted without breaking the system.

```java
public interface Person {
    String getName();
    String getEmail();
}

public class Student implements Person { ... }
public class Instructor implements Person { ... }

// Works correctly with either Student or Instructor
private void display(Person p) {
    System.out.println(p.getName() + " — " + p.getEmail());
}
```

**I — Interface Segregation Principle**
The `Person` interface exposes only `getName()` and `getEmail()` — a small, focused interface. Classes are not forced to implement methods they do not need.

**D — Dependency Inversion Principle**
Controllers depend on the `ArrayList<Model>` abstraction and the `FileManager` utility, not on concrete storage implementations. The storage mechanism (CSV files) could be changed to a database without modifying any controller logic.

---

### 1.3 Design Pattern Types [3 Marks]

Design patterns are reusable, proven solutions to common software design problems. They are classified into three categories:

#### Creational Patterns
Control how objects are created to increase flexibility and reuse of existing code.

**Example — Factory Method:**
Each model class provides a static `fromCSV(String line)` factory method. The caller (`FileManager`) does not need to know the internal structure of the model; it simply calls the factory method.

```java
// In Unit.java — Factory Method pattern
public static Unit fromCSV(String line) {
    String[] parts = line.split(",", 5);
    List<String> prereqs = new ArrayList<>();
    if (parts.length == 5 && !parts[4].isEmpty())
        for (String p : parts[4].split(";")) prereqs.add(p.trim());
    return new Unit(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], prereqs);
}

// FileManager uses factory method — no knowledge of Unit internals needed
Unit u = Unit.fromCSV(line);
```

#### Structural Patterns
Deal with object composition and simplifying relationships between entities.

**Example — Facade:**
The `EnrollmentController` acts as a Facade that hides the complexity of the enrolment subsystem from the `EnrollmentPanel` view. The view calls a single method without knowing about duplicate checking, ID generation, date stamping, or file persistence.

```java
// EnrollmentController — Facade pattern
// View calls ONE method; internal complexity is hidden
public boolean enroll(String studentId, String offeringId) {
    // Step 1: Check for duplicate enrolment
    for (Enrollment e : enrollments)
        if (e.getStudentId().equals(studentId) && e.getOfferingId().equals(offeringId))
            return false;
    // Step 2: Generate unique ID
    String id = "ENR" + (enrollments.size() + 1);
    // Step 3: Stamp current date
    String date = LocalDate.now().toString();
    // Step 4: Persist to file
    enrollments.add(new Enrollment(id, studentId, offeringId, date));
    FileManager.saveEnrollments(enrollments, filePath);
    return true;
}
```

#### Behavioral Patterns
Focus on communication and the assignment of responsibilities between objects.

**Example — Strategy:**
`SearchSortUtil` implements multiple interchangeable algorithm strategies. The caller selects which algorithm to apply at runtime depending on context — bubble sort for units, insertion sort for instructors, linear search for quick lookups, and binary search for sorted datasets. This follows the Strategy pattern, where the algorithm is chosen independently of the data it operates on.

```java
// Strategy pattern — caller selects algorithm at runtime
SearchSortUtil.bubbleSortUnits(unitList);       // Strategy A
SearchSortUtil.insertionSortInstructors(list);  // Strategy B
SearchSortUtil.linearSearchStudent(list, id);   // Strategy C
SearchSortUtil.binarySearchUnit(sorted, code);  // Strategy D
```

---
### 1.4 OOD Principles and Design Patterns

The College Management System applies low coupling and high cohesion through a structured MVC architecture. Controllers interact with models and utilities without depending on Swing components, improving maintainability and scalability. SOLID principles are demonstrated through dedicated responsibilities, interface-based design, extensibility, and abstraction. The system also incorporates Factory Method, Facade, and Strategy design patterns to simplify object creation, encapsulate subsystem complexity, and provide interchangeable search and sorting algorithms.


## Part A — Q2: UML Class Diagram [6 Marks]

The UML design models the relationships between Students, Instructors, Units, Unit Offerings, and Enrolments. The diagram reflects MVC architecture by separating Models, Views, Controllers, and Utility classes. The Person interface supports inheritance and polymorphism, while Enrollment resolves the many-to-many relationship between students and offerings.

The UML Class Diagram is provided in the file `UML_DIAGRAM.puml` in the project repository.

To render it: paste the contents into **https://www.plantuml.com/plantuml/uml/**

**Key design decisions reflected in the diagram:**

- `Student` and `Instructor` implement the `Person` interface — demonstrates Liskov Substitution and Interface Segregation (SOLID).
- `Enrollment` resolves the many-to-many relationship between `Student` and `UnitOffering` — a standard association class pattern.
- `UnitOffering` references `Unit` via `unitCode` and optionally references an `Instructor` — loose coupling through IDs rather than object references.
- Each Controller class manages one `ArrayList` of its corresponding Model type, ensuring Single Responsibility.
- `FileManager` and `SearchSortUtil` are utility classes used by Controllers — they are independent of the View layer, enforcing the MVC boundary.
- `MainFrame` composes all View panels — the single entry point into the View layer.

The MVC architecture is clearly visible:
- **Model package:** `Unit`, `UnitOffering`, `Instructor`, `Student`, `Enrollment`, `Person`
- **View package:** `MainFrame`, `UnitPanel`, `UnitOfferingPanel`, `InstructorPanel`, `StudentPanel`, `EnrollmentPanel`, `ReportPanel`, `AlgorithmsDemoPanel`
- **Controller package:** `UnitController`, `UnitOfferingController`, `InstructorController`, `StudentController`, `EnrollmentController`
- **Util package:** `FileManager`, `SearchSortUtil`

---

## Part A — Q3: Test Plan with JUnit [3 Marks]

### JUnit Testing
JUnit 5 was used to validate controller functionality. Tests verify duplicate prevention, searching, sorting, filtering, and record retrieval. Three controller test suites were executed: UnitControllerTest, StudentControllerTest, and EnrollmentControllerTest. All 18 tests completed successfully with zero failures and zero errors.

### Test Objectives

Verify that the three core controller classes correctly:
1. Enforce business rules (reject duplicates, validate IDs)
2. Return accurate results from search and sort operations
3. Correctly filter and retrieve data

### Test Environment

| Item | Detail |
|---|---|
| Framework | JUnit 5 (Jupiter) |
| Build Tool | Maven 3.9 |
| Java Version | OpenJDK 21 |
| Test isolation | Each test creates a fresh controller with a temporary CSV file deleted before and after (`@BeforeEach` / `@AfterEach`) |

---

### Test Class 1: `UnitControllerTest`

**Test Objective:** Verify that units can be added, duplicates are rejected, search works case-insensitively, and bubble sort returns correct alphabetical order.

| Test ID | Method | Scenario | Input | Expected Result |
|---|---|---|---|---|
| UC-01 | `testAddUnit()` | Add new unit | Code: "CS101", Name: "Intro to Computing" | Returns `true`; list size = 1 |
| UC-02 | `testAddDuplicateUnit()` | Add duplicate code | Code: "CS101" again | Returns `false`; list size stays 1 |
| UC-03 | `testFindByCode()` | Linear search — found | Code: "BN201" | Returns correct `Unit` object |
| UC-04 | `testFindByCodeNotFound()` | Linear search — not found | Code: "NOTEXIST" | Returns `null` |
| UC-05 | `testSortedByName()` | Bubble sort | 3 units in random order | Returns list in ascending alphabetical order |
| UC-06 | `testSearchUnits()` | Keyword search | Keyword: "BN" | Returns only units matching "BN" |

---

### Test Class 2: `StudentControllerTest`

**Test Objective:** Verify student registration, duplicate prevention, linear search by ID, bubble sort by name, and keyword search.

| Test ID | Method | Scenario | Input | Expected Result |
|---|---|---|---|---|
| SC-01 | `testAddStudent()` | Register new student | ID: "S001", Name: "Alice Johnson" | Returns `true`; list size = 1 |
| SC-02 | `testAddDuplicateStudent()` | Register duplicate ID | ID: "S001" again | Returns `false`; list size stays 1 |
| SC-03 | `testFindById()` | Linear search — found | ID: "S002" | Returns correct `Student` object with matching name |
| SC-04 | `testFindByIdNotFound()` | Linear search — not found | ID: "S999" | Returns `null` |
| SC-05 | `testSortedByName()` | Bubble sort | 3 students: Zara, Alice, Mike | Returns: Alice, Mike, Zara |
| SC-06 | `testSearchStudents()` | Case-insensitive search | Keyword: "alice" | Returns only Alice Johnson |

---

### Test Class 3: `EnrollmentControllerTest`

**Test Objective:** Verify enrolment creation, duplicate prevention, filtering by student and offering, and retrieval by enrolment ID.

| Test ID | Method | Scenario | Input | Expected Result |
|---|---|---|---|---|
| EC-01 | `testEnroll()` | Enrol student | S001 → OFF001 | Returns `true`; 1 enrolment recorded |
| EC-02 | `testDuplicateEnroll()` | Enrol same student twice | S001 → OFF001 again | Returns `false`; prevents duplicate |
| EC-03 | `testStudentMultipleOfferings()` | Same student, different offering | S001 → OFF002 | Returns `true`; correctly allows |
| EC-04 | `testFilterByStudent()` | Filter by student | S001 has 2 enrolments | Returns exactly 2 records, all for S001 |
| EC-05 | `testFilterByOffering()` | Filter by offering | OFF001 has 2 students | Returns exactly 2 records, all for OFF001 |
| EC-06 | `testFindById()` | Retrieve by ID | ID: "ENR1" | Returns correct `Enrollment` with studentId = "S001" |

**Test Results: 18 tests — 0 failures, 0 errors (Maven Surefire output confirmed)**

---

## Part B — Implementation Summary [35 Marks]

The application follows MVC architecture. Data is stored in ArrayLists during execution and persisted using CSV files through FileManager. SearchSortUtil implements Linear Search, Binary Search, Bubble Sort, and Insertion Sort. GitHub was used for version control, collaboration, and maintaining project history throughout development.

### System Architecture

The application follows strict **MVC (Model-View-Controller)** architecture:

| Layer | Package | Classes | Responsibility |
|---|---|---|---|
| Model | `model` | Unit, UnitOffering, Instructor, Student, Enrollment, Person | Data representation and CSV serialisation |
| View | `view` | MainFrame + 7 panels | All Swing GUI components |
| Controller | `controller` | 5 controller classes | Business logic, data management |
| Utility | `util` | FileManager, SearchSortUtil | File I/O and algorithm implementations |

### Data Persistence (Q7)

All data is stored in memory using `ArrayList<Model>` and persisted to CSV files in the `data/` directory using `FileManager`. Data is loaded on application startup and saved after every modification.

```
data/units.csv
data/offerings.csv
data/instructors.csv
data/students.csv
data/enrollments.csv
```

### Search and Sort Algorithms (Q9)

Implemented in `SearchSortUtil.java` and demonstrated interactively in the **Algorithms tab**:

| Algorithm | Type | Applied To | Complexity |
|---|---|---|---|
| Linear Search | Search | Units, Students, Instructors, Enrollments | O(n) |
| Binary Search | Search | Sorted unit/student lists | O(log n) |
| Bubble Sort | Sort | Units by name, Students by name | O(n²) |
| Insertion Sort | Sort | Instructors by name | O(n²) avg |

The Algorithms tab allows users to visually run each algorithm against live data and see step-by-step output including comparison counts and swap counts.

### Version Control (Q8)

The project uses **Git** with the repository hosted on **GitHub** (`Pratik00100/Portfolio`). The CMS is developed on branch `claude/assignment-file-upload-LdaKg`. Commits include meaningful messages describing each change.

---

## References

[1] E. Gamma, R. Helm, R. Johnson, and J. Vlissides, *Design Patterns: Elements of Reusable Object-Oriented Software*. Reading, MA: Addison-Wesley, 1994.

[2] R. C. Martin, *Clean Code: A Handbook of Agile Software Craftsmanship*. Upper Saddle River, NJ: Prentice Hall, 2008.

[3] Oracle Corporation, "The Java Tutorials — Interfaces," Oracle Java Documentation, 2023. [Online]. Available: https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html. [Accessed: 17-Apr-2026].

[4] T. H. Cormen, C. E. Leiserson, R. L. Rivest, and C. Stein, *Introduction to Algorithms*, 4th ed. Cambridge, MA: MIT Press, 2022.

[5] G. Booch, R. Maksimchuk, M. Engle, B. Young, J. Conallen, and K. Houston, *Object-Oriented Analysis and Design with Applications*, 3rd ed. Boston, MA: Addison-Wesley, 2007.

[6] JUnit Team, "JUnit 5 User Guide," JUnit.org, 2023. [Online]. Available: https://junit.org/junit5/docs/current/user-guide/. [Accessed: 17-Apr-2026].

---

## Individual Contribution Table

| Student Name | Student ID | Contributions |
|---|---|---|
| Pratik Dhami | MIT231942 | System architecture and MVC design, Controller package implementation, File I/O and data persistence, Search and sort algorithm implementation (SearchSortUtil), Algorithms Demo panel, Git version control and repository management |
| Joy Nyaundi | MIT223649 | Model package design and implementation, View package (all Swing GUI panels), JUnit test plan design and implementation, UML class diagram, Part A written report (OOD principles, design patterns) |
| Mishel Kenath | MIT231379 | Prepared and reviewed the final assessment report content, improved the GitHub Markdown formatting, added screenshot-based evidence for the implemented CMS features, tested the main user interface screens including students, instructors, offerings, enrolment, reports, and algorithm demo panels, checked that the documentation matched the completed system, and contributed to final presentation preparation and submission quality control. |

*Both group members contributed equally to the overall design, testing, and presentation preparation.*

---

*Word count: ~950 words (excluding code snippets, tables, and references)*
