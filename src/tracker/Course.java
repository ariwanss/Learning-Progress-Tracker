package tracker;

import java.math.BigDecimal;
import java.util.*;

public class Course {
    private final int MIN_COMPLETION;
    private final String name;
    private Map<Long, Integer> grades;
    private Set<Long> graduatedStudents;
    private int activity;

    public Course(String name) {
        this.name = name;
        this.grades = new HashMap<>();

        switch (name) {
            case "Java":
                MIN_COMPLETION = 600;
                break;
            case "DSA":
                MIN_COMPLETION = 400;
                break;
            case "Databases":
                MIN_COMPLETION = 480;
                break;
            case "Spring":
                MIN_COMPLETION = 550;
                break;
            default:
                MIN_COMPLETION = 0;
        }

        graduatedStudents = new HashSet<>();
    }

    public void incrementGrade(long id, int grade) {
        if (!grades.containsKey(id) && grade == 0) {
            return;
        }

        incrementActivity();
        grades.put(id, grades.getOrDefault(id, 0) + grade);

        if (grades.get(id) >= MIN_COMPLETION) {
            graduatedStudents.add(id);
        }
    }

    public Set<Long> getGraduates() {
        Set<Long> grads = new HashSet<>(graduatedStudents);
        graduatedStudents.clear();
        return grads;
    }

    public String getName() {
        return name;
    }

    public Map<Long, Integer> getGrades() {
        return grades;
    }

    public int getMinCompletion() {
        return MIN_COMPLETION;
    }

    public int getNumberOfStudents() {
        return grades.size();
    }

    public int getActivity() {
        return activity;
    }

    public int getTotalPoints() {
        return grades.values().stream().reduce(0, Integer::sum);
    }

    public BigDecimal getAveragePoints() {
        try {
            double avg = (double) getTotalPoints() / getActivity();
            return BigDecimal.valueOf(avg);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }

    }

    private void incrementActivity() {
        activity += 1;
    }

    public long getTopLearner() {
        return Collections.max(grades.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(name, course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
