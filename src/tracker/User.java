package tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private static long counter = 0;
    private final long id;
    private String firstName;
    private String lastName;
    private String email;
    private Map<String, Integer> grades;

    public User(String firstName, String lastName, String email) {
        this.id = ++counter;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.grades = new HashMap<>();
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public void incrementGrade(String courseName, int grade) {
        if (!grades.containsKey(courseName) && grade == 0) {
            return;
        }

        grades.put(courseName, grades.getOrDefault(courseName, 0) + grade);
    }

    public boolean isEnrolledIn(String courseName) {
        return grades.containsKey(courseName);
    }

    @Override
    public String toString() {
        return id + " points: " +
                "Java=" + grades.getOrDefault("Java", 0) +
                "; DSA=" + grades.getOrDefault("DSA", 0) +
                "; Databases=" + grades.getOrDefault("Databases", 0) +
                "; Spring=" + grades.getOrDefault("Spring", 0);
    }
}
