package model;

/**
 * Common interface for people in the system (Student, Instructor).
 * Demonstrates Interface Segregation and Dependency Inversion (SOLID).
 */
public interface Person {
    String getName();
    String getEmail();
}
