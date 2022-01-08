package tracker;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Database {
    private final Map<Long, User> users;
    private final Map<String, Course> courses;

    public Database() {
        users = new HashMap<>();
        courses = new HashMap<>();
        courses.put("Java", new Course("Java"));
        courses.put("DSA", new Course("DSA"));
        courses.put("Databases", new Course("Databases"));
        courses.put("Spring", new Course("Spring"));
    }

    public void addStudent(User user) {
        users.put(user.getId(), user);
    }

    public boolean studentExists(String email) {
        return users.values().stream().anyMatch(user -> email.equals(user.getEmail()));
    }

    public boolean studentExists(long id) {
        return users.containsKey(id);
    }

    public User getStudent(long id) {
        return users.get(id);
    }

    public void update(long id, int java, int dsa, int db, int spring) {
        User user = users.get(id);
        user.incrementGrade("Java", java);
        user.incrementGrade("DSA", dsa);
        user.incrementGrade("Databases", db);
        user.incrementGrade("Spring", spring);

        courses.get("Java").incrementGrade(id, java);
        courses.get("DSA").incrementGrade(id, dsa);
        courses.get("Databases").incrementGrade(id, db);
        courses.get("Spring").incrementGrade(id, spring);
    }

    public void printStudentsList() {
        if (users.isEmpty()) {
            System.out.println("No students found");
        } else {
            System.out.println("Students:");
            users.keySet().forEach(System.out::println);
        }
    }

    public boolean courseExists(String courseName) {
        return courses.containsKey(courseName);
    }

    public Course getCourse(String courseName) {
        return courses.get(courseName);
    }

    public List<String> getMostPopularCourse() {
        int max = Collections.max(courses.values(), Comparator.comparing(Course::getNumberOfStudents)).getNumberOfStudents();
        if (max == 0) {
            return Collections.emptyList();
        }
        return courses.values().stream().filter(course -> course.getNumberOfStudents() == max)
                .map(Course::getName).collect(Collectors.toList());
    }

    public List<String> getLeastPopularCourse() {
        int min = Collections.min(courses.values(), Comparator.comparing(Course::getNumberOfStudents)).getNumberOfStudents();
        if (min == 0) {
            return Collections.emptyList();
        }
        List<String> mostPopular = getMostPopularCourse();
        List<String> leastPopular = courses.values().stream().filter(course -> course.getNumberOfStudents() == min)
                .map(Course::getName).collect(Collectors.toList());
        leastPopular.removeAll(mostPopular);
        return leastPopular;
    }

    public List<String> getMostActiveCourse() {
        int max = Collections.max(courses.values(), Comparator.comparing(Course::getActivity)).getActivity();
        if (max == 0) {
            return Collections.emptyList();
        }
        return courses.values().stream().filter(course -> course.getActivity() == max)
                .map(Course::getName).collect(Collectors.toList());
    }

    public List<String> getLeastActiveCourse() {
        int min = Collections.min(courses.values(), Comparator.comparing(Course::getActivity)).getActivity();
        if (min == 0) {
            return Collections.emptyList();
        }
        List<String> mostActive = getMostActiveCourse();
        List<String> leastActive = courses.values().stream().filter(course -> course.getActivity() == min)
                .map(Course::getName).collect(Collectors.toList());
        leastActive.removeAll(mostActive);
        return leastActive;
    }

    public List<String> getEasiestCourse() {
        BigDecimal max = Collections.max(courses.values(), Comparator.comparing(Course::getAveragePoints)).getAveragePoints();
        if (max.equals(BigDecimal.ZERO)) {
            return Collections.emptyList();
        }
        return courses.values().stream().filter(course -> Objects.equals(course.getAveragePoints(), max))
                .map(Course::getName).collect(Collectors.toList());
    }

    public List<String> getHardestCourse() {
        BigDecimal min = Collections.min(courses.values(), Comparator.comparing(Course::getAveragePoints)).getAveragePoints();
        if (min.equals(BigDecimal.ZERO)) {
            return Collections.emptyList();
        }
        List<String> easiest = getEasiestCourse();
        List<String> hardest = courses.values().stream().filter(course -> Objects.equals(course.getAveragePoints(), min))
                .map(Course::getName).collect(Collectors.toList());
        hardest.removeAll(easiest);
        return hardest;
    }
}
