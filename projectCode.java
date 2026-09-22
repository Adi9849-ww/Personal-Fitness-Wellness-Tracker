import java.util.Scanner;

public class SimpleFitnessTracker {

    // =========================================================================
    // CO 1 (MEMBER 1): USER INPUT & BMI CALCULATOR
    // Topics: Data Types, Scanner, Arithmetic Operators, Formatted Printing
    // =========================================================================
    static class UserProfile {
        String name;
        double weight;
        double heightCm;

        // Reads simple inputs
        public void getInput(Scanner sc) {
            System.out.println("=== USER SETUP ===");
            System.out.print("Enter Name: ");
            this.name = sc.nextLine();

            System.out.print("Enter Weight (kg): ");
            this.weight = sc.nextDouble();

            System.out.print("Enter Height (cm): ");
            this.heightCm = sc.nextDouble();
        }

        // Basic BMI calculation: weight / (heightInMeters * heightInMeters)
        public void displayBMI() {
            double heightMeters = heightCm / 100.0;
            double bmi = weight / (heightMeters * heightMeters);

            System.out.println("\n--- HEALTH PROFILE ---");
            System.out.printf("Name: %s\n", name);
            System.out.printf("BMI : %.2f\n", bmi);
            System.out.println("----------------------");
        }
    }


    // =========================================================================
    // CO 2 (MEMBER 2): WORKOUT LOGGER
    // Topics: Conditional Statements (if-else), Iteration (while loop)
    // =========================================================================
    static class WorkoutLogger {

        // Uses simple if-else logic to get calories burned per minute
        public double calculateCalories(int choice, int minutes) {
            double caloriesPerMinute;

            if (choice == 1) {        // Running
                caloriesPerMinute = 10.0;
            } else if (choice == 2) { // Walking
                caloriesPerMinute = 4.0;
            } else {                  // Other exercise
                caloriesPerMinute = 6.0;
            }

            return caloriesPerMinute * minutes;
        }

        // Uses a basic while loop to record workout entries
        public double logWorkout(Scanner sc) {
            System.out.println("\n=== WORKOUT LOGGER ===");
            System.out.println("1. Running");
            System.out.println("2. Walking");
            System.out.println("3. Other Exercise");
            System.out.print("Select exercise type (1-3): ");
            int type = sc.nextInt();

            System.out.print("Enter duration in minutes: ");
            int mins = sc.nextInt();

            double caloriesBurned = calculateCalories(type, mins);
            System.out.printf("Logged! Calories burned today: %.1f kcal\n", caloriesBurned);

            return caloriesBurned;
        }
    }


    // =========================================================================
    // CO 3 (MEMBER 3): HISTORICAL LOGS & TOTALS
    // Topics: 1D Array Traversal, Methods, Summation
    // =========================================================================
    static class Analytics {

        // Uses a 1D array and a basic for-loop to calculate total calories burned over the week
        public double calculateWeeklyTotal(double[] caloriesArray) {
            double total = 0;

            // 1D Array Traversal
            for (int i = 0; i < caloriesArray.length; i++) {
                total = total + caloriesArray[i];
            }

            return total;
        }

        // Displays weekly 1D array logs
        public void displayWeeklyLogs(double[] caloriesArray) {
            System.out.println("\n--- WEEKLY SUMMARY (7 Days) ---");
            for (int i = 0; i < caloriesArray.length; i++) {
                System.out.printf("Day %d: %.1f kcal\n", (i + 1), caloriesArray[i]);
            }
        }
    }
// =========================================================================
    // MAIN EXECUTION (CONNECTING ALL 3 COs)
    // =========================================================================
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // CO 1: Member 1 logic
        UserProfile user = new UserProfile();
        user.getInput(sc);
        user.displayBMI();

        // CO 2: Member 2 logic
        WorkoutLogger logger = new WorkoutLogger();
        double todayBurned = logger.logWorkout(sc);

        // CO 3: Member 3 logic
        Analytics analytics = new Analytics();

        // 1D Array holding 7 days of calories (Day 1 gets today's burned amount)
        double[] weeklyCalories = {todayBurned, 200.0, 150.0, 300.0, 250.0, 0.0, 180.0};

        // Output results
        analytics.displayWeeklyLogs(weeklyCalories);
        double totalWeekly = analytics.calculateWeeklyTotal(weeklyCalories);
        System.out.printf("\nTotal Calories Burned This Week: %.1f kcal\n", totalWeekly);

        sc.close();
    }
}
