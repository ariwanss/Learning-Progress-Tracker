package tracker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LearningProgressTracker {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Pattern NAME_PATTERN = Pattern.compile("^(?:\\w+['-]?)+\\w+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.\\-_]+@[\\w.\\-_]+\\.[\\w]+$");
    private static final Database database = new Database();

    public void run() {
        System.out.println("Learning Progress Tracker");

        while (true) {
            String command = scanner.nextLine().strip();

            switch (command) {
                case "":
                    System.out.println("No input.");
                    continue;
                case "add students":
                    addStudents();
                    break;
                case "list":
                    printStudentList();
                    break;
                case "add points":
                    addPoints();
                    break;
                case "find":
                    findStudents();
                    break;
                case "statistics":
                    getStatistics();
                    break;
                case "notify":
                    doNotify();
                    break;
                case "back":
                    System.out.println("Enter 'exit' to exit the program.");
                    break;
                case "exit":
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Error: unknown command!");
            }
        }
    }

    private void addStudents() {
        System.out.println("Enter student credentials or 'back' to return");
        int counter = 0;

        while (true) {
            String response = scanner.nextLine();

            if ("back".equals(response)) {
                System.out.println("Total " + counter + " students have been added.");
                return;
            }

            String[] credentials = response.split("\\s");

            if (credentials.length < 3) {
                System.out.println("Incorrect credentials");
                continue;
            }

            String firstName = credentials[0];
            Matcher nameMatcher = NAME_PATTERN.matcher(firstName);

            if (!nameMatcher.find()) {
                System.out.println("Incorrect first name.");
                continue;
            }

            String lastNames = combineLastNames(Arrays.copyOfRange(credentials, 1, credentials.length - 1));

            if (lastNames.isEmpty()) {
                System.out.println("Incorrect last name.");
                continue;
            }

            String email = credentials[credentials.length - 1];
            Matcher emailMatcher = EMAIL_PATTERN.matcher(email);

            if (!emailMatcher.find()) {
                System.out.println("Incorrect email.");
                continue;
            }

            if (database.studentExists(email)) {
                System.out.println("This email is already taken.");
                continue;
            }

            database.addStudent(new User(firstName, lastNames, email));
            counter += 1;
            System.out.println("The student has been added.");
        }
    }

    private void printStudentList() {
        database.printStudentsList();
    }

    private void addPoints() {
        System.out.println("Enter an id and points or 'back' to return:");

        while (true) {
            String[] response = scanner.nextLine().split("\\s");

            if ("back".equals(response[0])) {
                return;
            }

            if (response.length != 5) {
                System.out.println("Incorrect points format");
                continue;
            }

            long id;

            try {
                id = Long.parseLong(response[0]);
            } catch (NumberFormatException e) {
                System.out.println("No student is found for id=" + response[0]);
                continue;
            }

            if (!database.studentExists(id)) {
                System.out.println("No student is found for id=" + id);
                continue;
            }

            int pointsJava;
            int pointsDsa;
            int pointsDatabase;
            int pointsSpring;

            try {
                pointsJava = Integer.parseInt(response[1]);
                pointsDsa = Integer.parseInt(response[2]);
                pointsDatabase = Integer.parseInt(response[3]);
                pointsSpring = Integer.parseInt(response[4]);
            } catch (NumberFormatException e) {
                System.out.println("Incorrect points format");
                continue;
            }

            if (pointsJava < 0 || pointsDsa < 0 || pointsDatabase < 0 || pointsSpring < 0) {
                System.out.println("Incorrect points format");
                continue;
            }

            database.update(id, pointsJava, pointsDsa, pointsDatabase, pointsSpring);
            System.out.println("Points updated");
        }
    }

    public void findStudents() {
        System.out.println("Enter an id or 'back' to return");

        while (true) {
            String response = scanner.nextLine();

            if ("back".equals(response)) {
                return;
            }

            long id = Long.parseLong(response);
            User user = database.getStudent(id);

            if (user == null) {
                System.out.println("No student is found for id=" + id);
            } else {
                System.out.println(user);
            }
        }
    }

    public void getStatistics() {
        System.out.println("Type the name of a course to see details or 'back' to quit");
        List<String> mostPopular = database.getMostPopularCourse();
        List<String> leastPopular = database.getLeastPopularCourse();
        List<String> mostActive = database.getMostActiveCourse();
        List<String> leastActive = database.getLeastActiveCourse();
        List<String> easiest = database.getEasiestCourse();
        List<String> hardest = database.getHardestCourse();

        System.out.print("Most popular: ");
        if (mostPopular.isEmpty()) {
            System.out.print("n/a");
        } else {
            mostPopular.forEach(x -> System.out.print(x + " "));
        }
        System.out.println();

        System.out.print("Least popular: ");
        if (leastPopular.isEmpty()) {
            System.out.print("n/a");
        } else {
            leastPopular.forEach(x -> System.out.print(x + " "));
        }
        System.out.println();

        System.out.print("Highest activity: ");
        if (mostActive.isEmpty()) {
            System.out.print("n/a");
        } else {
            mostActive.forEach(x -> System.out.print(x + " "));
        }
        System.out.println();

        System.out.print("Lowest activity: ");
        if (leastActive.isEmpty()) {
            System.out.print("n/a");
        } else {
            leastActive.forEach(x -> System.out.print(x + " "));
        }
        System.out.println();

        System.out.print("Easiest course: ");
        if (easiest.isEmpty()) {
            System.out.print("n/a");
        } else {
            easiest.forEach(x -> System.out.print(x + " "));
        }
        System.out.println();

        System.out.print("Hardest course: ");
        if (hardest.isEmpty()) {
            System.out.print("n/a");
        } else {
            hardest.forEach(x -> System.out.print(x + " "));
        }
        System.out.println();

        while (true) {
            String courseName = scanner.nextLine();

            if ("back".equals(courseName)) {
                return;
            }

            if (!database.courseExists(courseName)) {
                System.out.println("Unknown course");
                continue;
            }

            System.out.println(courseName);
            System.out.println("id points completed");

            Course course = database.getCourse(courseName);
            Map<Long, Integer> grades = course.getGrades();
            Map<Long, Double> gradesSorted = new LinkedHashMap<>();

            List<Map.Entry<Long, Integer>> mapEntry = new ArrayList<>(grades.entrySet());
            mapEntry.sort(Comparator.comparing(Map.Entry<Long, Integer>::getValue).reversed().thenComparing(Map.Entry::getKey));
            mapEntry.forEach(entry -> gradesSorted.put(entry.getKey(), (double) entry.getValue() / course.getMinCompletion() * 100));

            gradesSorted.forEach((key, value) -> {
                System.out.print(key + " ");
                System.out.print(grades.get(key) + " ");
                System.out.print(BigDecimal.valueOf(value).setScale(1, RoundingMode.HALF_UP) + "%");
                System.out.println();
            });
        }
    }

    public void doNotify() {
        Set<Long> javaGrad = database.getCourse("Java").getGraduates();
        Set<Long> dsaGrad = database.getCourse("DSA").getGraduates();
        Set<Long> databaseGrad = database.getCourse("Databases").getGraduates();
        Set<Long> springGrad = database.getCourse("Spring").getGraduates();

        javaGrad.forEach(x -> {
            User user = database.getStudent(x);
            System.out.println("To: " + user.getEmail());
            System.out.println("Re: " + "Your Learning Progress");
            System.out.println("Hello, " + user.getName() + "! You have accomplished our Java course!");
        });

        dsaGrad.forEach(x -> {
            User user = database.getStudent(x);
            System.out.println("To: " + user.getEmail());
            System.out.println("Re: " + "Your Learning Progress");
            System.out.println("Hello, " + user.getName() + "! You have accomplished our DSA course!");
        });

        databaseGrad.forEach(x -> {
            User user = database.getStudent(x);
            System.out.println("To: " + user.getEmail());
            System.out.println("Re: " + "Your Learning Progress");
            System.out.println("Hello, " + user.getName() + "! You have accomplished our Databases course!");
        });

        springGrad.forEach(x -> {
            User user = database.getStudent(x);
            System.out.println("To: " + user.getEmail());
            System.out.println("Re: " + "Your Learning Progress");
            System.out.println("Hello, " + user.getName() + "! You have accomplished our Spring course!");
        });

        javaGrad.addAll(dsaGrad);
        javaGrad.addAll(databaseGrad);
        javaGrad.addAll(springGrad);
        System.out.println("Total " + javaGrad.size() + " students have been notified.");
    }

    private String combineLastNames(String[] names) {
        StringBuilder builder = new StringBuilder();

        for (String name : names) {
            Matcher nameMatcher = NAME_PATTERN.matcher(name);

            if (!nameMatcher.find()) {
                return "";
            }

            builder.append(name);
            builder.append(" ");
        }

        return builder.toString().strip();
    }
}
