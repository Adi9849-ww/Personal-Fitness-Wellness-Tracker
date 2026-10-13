import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

// ============================================================================
// CO4: CUSTOM EXCEPTION HANDLING (Checked vs Unchecked Exceptions)
// ============================================================================
class InvalidWorkoutDataException extends Exception { // Checked Exception
    public InvalidWorkoutDataException(String message) {
        super(message);
    }
}

class RecordNotFoundException extends RuntimeException { // Unchecked Exception
    public RecordNotFoundException(String message) {
        super(message);
    }
}

// ============================================================================
// CO4: OBJECT-ORIENTED MODELLING, INTERFACES & BLUEPRINTS ("is-a" relationship)
// ============================================================================
interface Trackable {
    double calculateCaloriesBurned();
    String getSummary();
}

abstract class Activity implements Trackable, Comparable<Activity> {
    private final String activityId;
    private String name;
    private int durationMinutes;

    public static final String APP_NAME = "FitPulse Tracker";
    private static int totalActivitiesCount = 0;

    public Activity() {
        this("ACT-000", "General Workout", 30);
    }

    public Activity(String activityId, String name, int durationMinutes) {
        this.activityId = activityId;
        this.name = name;
        this.durationMinutes = durationMinutes;
        totalActivitiesCount++;
    }

    public String getActivityId() { return activityId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public static int getTotalActivitiesCount() { return totalActivitiesCount; }

    // CO5: Comparable implementation for natural ordering (by duration)
    @Override
    public int compareTo(Activity other) {
        return Integer.compare(this.durationMinutes, other.durationMinutes);
    }

    // CO4: toString override for object representation
    @Override
    public String toString() {
        return String.format("[%s] %s | %d mins", activityId, name, durationMinutes);
    }
}

// ============================================================================
// CO4: INHERITANCE, METHOD OVERRIDING & POLYMORPHISM
// ============================================================================
class CardioActivity extends Activity {
    private double distanceKm;

    public CardioActivity(String id, String name, int durationMinutes, double distanceKm) {
        super(id, name, durationMinutes);
        this.distanceKm = distanceKm;
    }

    public double getDistanceKm() { return distanceKm; }

    @Override
    public double calculateCaloriesBurned() {
        // CO1: Expressions, Precedence & Type promotion
        return (getDurationMinutes() * 8.5) + (distanceKm * 12.0);
    }

    @Override
    public String getSummary() {
        return String.format("Cardio: %s, Distance: %.2f km, Duration: %d mins, Burned: %.1f kcal",
                getName(), distanceKm, getDurationMinutes(), calculateCaloriesBurned());
    }
}

class StrengthActivity extends Activity {
    private int sets;
    private int reps;

    public StrengthActivity(String id, String name, int durationMinutes, int sets, int reps) {
        super(id, name, durationMinutes);
        this.sets = sets;
        this.reps = reps;
    }

    @Override
    public double calculateCaloriesBurned() {
        return (getDurationMinutes() * 6.0) + (sets * reps * 0.5);
    }

    @Override
    public String getSummary() {
        return String.format("Strength: %s, Sets: %d, Reps: %d, Duration: %d mins, Burned: %.1f kcal",
                getName(), sets, reps, getDurationMinutes(), calculateCaloriesBurned());
    }
}

// ============================================================================
// MAIN APPLICATION WITH CONSOLE USER INPUT (SCANNER)
// ============================================================================
public class FitnessTrackerApp {

    private static final String DATA_FILE = "fitness_records.txt";

    public static void main(String[] args) {
        // CO1: Scanner for Typed Console Input
        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.printf("       WELCOME TO %s       %n", Activity.APP_NAME);
        System.out.println("==================================================");

        // --- STEP 1: USER PROFILE INPUT (CO1 & CO2) ---
        System.out.print("Enter your Age (years): ");
        int age = scanner.nextInt();

        System.out.print("Enter your Weight (kg): ");
        double weightKg = scanner.nextDouble();

        System.out.print("Enter your Height (cm): ");
        double heightCm = scanner.nextDouble();

        System.out.print("Enter Gender (M/F): ");
        char gender = scanner.next().toUpperCase().charAt(0);

        // CO1: Constants, Operators, Typecasting & Expressions
        final double CM_TO_METERS = 0.01;
        int flags = 0b0001;
        flags = flags << 1; // Bitwise shift
        int priorityScore = (age > 20) ? ++age : age--; // Ternary & Unary

        double heightM = heightCm * CM_TO_METERS; // Widening typecast
        double bmi = weightKg / (heightM * heightM);

        System.out.println("\n----------------- USER METRICS -----------------");
        System.out.printf("Gender: %c | Adjusted Priority Age: %d | Bitwise Flag: %d%n", gender, priorityScore, flags);
        System.out.printf("Calculated BMI: %.2f%n", bmi);
        evaluateBmiCategory(bmi); // CO2: If-Else ladder

        // --- STEP 2: GOAL SELECTION (CO2 SWITCH CASE) ---
        System.out.println("\nSelect Primary Fitness Goal:");
        System.out.println("1. Fat Loss & Calorie Burn");
        System.out.println("2. Hypertrophy & Muscle Strength");
        System.out.println("3. Cardiovascular Stamina");
        System.out.print("Enter Choice (1-3): ");
        int goalChoice = scanner.nextInt();
        handleWorkoutGoalSwitch(goalChoice);

        // --- STEP 3: RECURSIVE STREAK INPUT (CO3) ---
        System.out.print("\nEnter your current active workout streak (days): ");
        int streakDays = scanner.nextInt();
        int streakMultiplier = calculateStreakMultiplier(streakDays);
        System.out.printf("Streak Multiplier (Recursion): %d%n", streakMultiplier);

        // --- STEP 4: 1D & 2D ARRAYS (CO3) ---
        demonstrateArraysAndMatrix(scanner);

        // --- STEP 5: DYNAMIC WORKOUT LOGGING (CO4, CO5, CO6) ---
        List<Activity> activityLog = new ArrayList<>();
        inputWorkouts(scanner, activityLog);

        // File I/O (CO5)
        demonstrateFileIO(activityLog);

        // Streams & Functional Programming (CO6)
        demonstrateFunctionalStreams(activityLog);

        // String Operations & StringBuilder (CO5)
        demonstrateStringManipulations();

        System.out.println("\nSession finished. Keep up the consistency!");
        scanner.close();
    }

    // ========================================================================
    // CO2: CONTROL FLOW (SELECTION & ITERATION)
    // ========================================================================
    public static void evaluateBmiCategory(double bmi) {
        if (bmi < 18.5) {
            System.out.println("Health Category: Underweight");
        } else if (bmi >= 18.5 && bmi < 24.9) {
            System.out.println("Health Category: Normal weight (Optimal)");
        } else if (bmi >= 25.0 && bmi < 29.9) {
            System.out.println("Health Category: Overweight");
        } else {
            System.out.println("Health Category: Obese");
        }
    }

    public static void handleWorkoutGoalSwitch(int choice) {
        switch (choice) {
            case 1:
                System.out.println("Target Activated: High-Intensity Interval Training & Deficit");
                break;
            case 2:
                System.out.println("Target Activated: Progressive Overload & High Protein");
                break;
            case 3:
                System.out.println("Target Activated: Aerobic Capacity & Zone 2 Training");
                break;
            default:
                System.out.println("Target Activated: General Fitness Maintenance");
                break;
        }
    }

    // ========================================================================
    // CO3: DECOMPOSITION WITH METHODS, ARRAYS & RECURSION
    // ========================================================================
    public static void demonstrateArraysAndMatrix(Scanner scanner) {
        System.out.println("\n--- [CO3] Weekly Step Counts (1D Array & Matrix Demo) ---");
        int[] dailySteps = new int[7];
        System.out.println("Enter step counts for the last 7 days:");
        for (int i = 0; i < 7; i++) {
            System.out.printf("  Day %d steps: ", (i + 1));
            dailySteps[i] = scanner.nextInt();
        }

        int totalSteps = 0;
        int maxSteps = dailySteps[0];
        for (int steps : dailySteps) {
            totalSteps += steps;
            if (steps > maxSteps) maxSteps = steps;
        }

        double averageSteps = (double) totalSteps / dailySteps.length;
        System.out.printf("Total Steps: %d | Daily Avg: %.2f | Best Day: %d%n", totalSteps, averageSteps, maxSteps);

        System.out.print("Enter a step milestone to search for: ");
        int target = scanner.nextInt();
        int foundIndex = -1;
        for (int i = 0; i < dailySteps.length; i++) {
            if (dailySteps[i] == target) {
                foundIndex = i;
                break;
            }
        }
        System.out.println(foundIndex != -1 ? "Milestone found on Day " + (foundIndex + 1) : "Milestone not found in log.");

        // 2D Array Matrix Arithmetic
        int[][] userRepMatrix = {
            {30, 45, 25},
            {40, 50, 30}
        };
        System.out.println("Pre-loaded Rep Matrix Traversal (2 Users x 3 Sets):");
        for (int r = 0; r < userRepMatrix.length; r++) {
            for (int c = 0; c < userRepMatrix[r].length; c++) {
                System.out.printf("[%d]", userRepMatrix[r][c]);
            }
            System.out.println();
        }
    }

    // Recursive calculation: Base case and recursive case
    public static int calculateStreakMultiplier(int days) {
        if (days <= 1) return 1; // Base case
        return days * calculateStreakMultiplier(days - 1); // Recursive case
    }

    // ========================================================================
    // USER INPUT DRIVEN WORKOUT LOGGING (CO4)
    // ========================================================================
    public static void inputWorkouts(Scanner scanner, List<Activity> activityLog) {
        System.out.println("\n--- [CO4] Log Custom Workouts ---");
        System.out.print("How many workout sessions would you like to enter? ");
        int count = scanner.nextInt();

        for (int i = 1; i <= count; i++) {
            System.out.printf("%nEntering Activity #%d:%n", i);
            System.out.println("Type: 1. Cardio (Running/Cycling) | 2. Strength (Weights)");
            System.out.print("Select Type (1 or 2): ");
            int type = scanner.nextInt();
            scanner.nextLine(); // Clear newline buffer

            System.out.print("Enter Activity ID (e.g., ACT-01): ");
            String id = scanner.nextLine().trim();

            System.out.print("Enter Activity Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Duration (minutes): ");
            int duration = scanner.nextInt();

            try {
                // CO4: Checked Exception trigger
                if (duration <= 0) {
                    throw new InvalidWorkoutDataException("Duration must be greater than zero minutes!");
                }

                if (type == 1) {
                    System.out.print("Enter Distance Covered (km): ");
                    double distance = scanner.nextDouble();
                    activityLog.add(new CardioActivity(id, name, duration, distance));
                } else {
                    System.out.print("Enter Number of Sets: ");
                    int sets = scanner.nextInt();
                    System.out.print("Enter Repetitions per Set: ");
                    int reps = scanner.nextInt();
                    activityLog.add(new StrengthActivity(id, name, duration, sets, reps));
                }
                System.out.println("Activity logged successfully!");
            } catch (InvalidWorkoutDataException e) {
                System.err.println("Input Error: " + e.getMessage() + ". Skipping this entry.");
            }
        }
    }

    // ========================================================================
    // CO5: STRINGS, STRINGBUILDER & MODERN FILE I/O
    // ========================================================================
    public static void demonstrateStringManipulations() {
        System.out.println("\n--- [CO5] String Tokenization & StringBuilder ---");
        String rawInput = "  RUNNING,06:30_AM,OUTDOORS,HIGH_INTENSITY  ";
        String cleanInput = rawInput.trim();
        String[] tokens = cleanInput.split(",");

        String s1 = new String("FITPULSE");
        String s2 = "FITPULSE";
        System.out.println("Reference Equality (s1 == s2): " + (s1 == s2));
        System.out.println("Value Equality (s1.equals(s2)): " + s1.equalsIgnoreCase(s2));

        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Parsed Tags: ");
        for (String token : tokens) {
            reportBuilder.append("[").append(token).append("] ");
        }
        System.out.println(reportBuilder.toString());
    }

    public static void demonstrateFileIO(List<Activity> activities) {
        System.out.println("\n--- [CO5] Modern File I/O (NIO Files/Path & try-with-resources) ---");
        Path filePath = Paths.get(DATA_FILE);

        // Writing records line by line
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Activity a : activities) {
                writer.write(String.format("%s;%s;%d;%.2f%n",
                        a.getActivityId(), a.getName(), a.getDurationMinutes(), a.calculateCaloriesBurned()));
            }
            System.out.println("Workout entries saved to file: " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("File write error: " + e.getMessage());
        }

        // Reading file and streaming lines
        try {
            System.out.println("Verifying saved data by reading back from disk:");
            Files.lines(filePath)
                 .filter(line -> !line.trim().isEmpty())
                 .forEach(line -> {
                     String[] parts = line.split(";");
                     System.out.printf("  Loaded Record -> ID: %s | Name: %s | Time: %s mins | Calories: %s kcal%n",
                             parts[0], parts[1], parts[2], parts[3]);
                 });
        } catch (IOException e) {
            System.err.println("File read error: " + e.getMessage());
        }
    }

    // ========================================================================
    // CO6: FUNCTIONAL PROGRAMMING WITH COLLECTIONS & STREAMS
    // ========================================================================
    public static void demonstrateFunctionalStreams(List<Activity> activities) {
        System.out.println("\n--- [CO6] Functional APIs & Stream Pipelines ---");
        if (activities.isEmpty()) {
            System.out.println("No activities available for analytics.");
            return;
        }

        // Functional Interfaces: Predicate, Function, Consumer
        Predicate<Activity> isHighBurn = act -> act.calculateCaloriesBurned() > 200.0;
        Function<Activity, String> summaryFunction = Activity::getSummary;
        Consumer<String> printer = System.out::println;

        System.out.println("High Calorie Burn Activities (> 200 kcal):");
        activities.stream()
                  .filter(isHighBurn)
                  .map(summaryFunction)
                  .forEach(printer);

        // Sorting by calories using Comparator
        activities.sort(Comparator.comparingDouble(Activity::calculateCaloriesBurned).reversed());

        // Stream Collector: groupingBy & counting
        Map<String, Long> activityFrequency = activities.stream()
                .collect(Collectors.groupingBy(Activity::getName, Collectors.counting()));
        System.out.println("\nWorkout Frequency Breakdown: " + activityFrequency);

        // Stream Pipeline: distinct, sorted, joining
        String summaryPipeline = activities.stream()
                .map(Activity::getName)
                .distinct()
                .sorted()
                .collect(Collectors.joining(", ", "Distinct Workouts Completed: [", "]"));
        System.out.println(summaryPipeline);

        // Stream Terminal Operation: reduce for total calories burned
        double totalCalories = activities.stream()
                .map(Activity::calculateCaloriesBurned)
                .reduce(0.0, Double::sum);

        System.out.printf("Total Aggregate Energy Expended: %.2f kcal%n", totalCalories);
    }
}
