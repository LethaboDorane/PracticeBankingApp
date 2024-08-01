//Main Menu: Register, log-in, close app
package myPackage;
import java.util.*;

public class i_startingMenu extends ii_jdbcTestRegister {

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/mzanzi_bank_system";
    static final String USERNAME = "root";
    static final String PASSWORD = "";
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        while (true) {
            // Display the main menu
            System.out.println("WELCOME TO MZANSI BANKING SYSTEM");
            mainMenu();

            // Check if the user's input is an integer
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next(); // Consume the non-integer input
                // Display the main menu
                mainMenu();
            }
    
            // Read the user's choice from the console
            int option = scanner.nextInt();
            scanner.nextLine();           
    
            // Switch case to handle different menu options
            switch (option) {
                case 1:
                    register(scanner); // Call the register method
                    break;
                case 2:
                    login(scanner); // Call the login method
                    break;
                case 3:
                    System.out.println("Goodbye!"); // Exit the program
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid entry. Please try again.");
            }
        }    
    }
    private static void mainMenu(){
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }
}
