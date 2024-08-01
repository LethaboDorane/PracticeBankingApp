//Functionality: View existing accounts 
package myPackage;
import java.util.*;
import java.sql.*;

public class v_existingAccount extends vi_test_transactions {

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/mzanzi_bank_system";
    static final String USERNAME = "root";
    static final String PASSWORD = "";

    public static void viewExistingAccounts(Scanner scanner) {
        
        System.out.println("Viewing existing accounts...");
       
        existingAccountMenu();

        // Check if there is input available
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.nextLine(); // Consume the invalid input
            existingAccountMenu();
            System.out.print("Choose an option: ");
        }

        // Read the user's choice
        int option = scanner.nextInt();
        scanner.nextLine();
        
        // Switch case to handle different menu options
        switch (option) {
            case 1:
                System.out.println("Viewing existing accounts...");
                System.out.println("");
                viewAccounts();
                break;
            case 2:
                System.out.println("Close account...");
                viewAccounts();
                closeAccount(scanner);
                break;
            case 3:
                System.out.println("Returning to previous menu...");
                // Go back to banking menu
                return;
            default:
                System.out.println("Invalid entry. Please try again.");
        }
    }
    
    private static void existingAccountMenu() {
        System.out.println("What would you like to do?");
        System.out.println("1. View existing accounts");
        System.out.println("2. Close account");
        System.out.println("3. Go Back");
        System.out.print("Choose an option: ");
    }

    private static void viewAccounts() {
        String loggedInUser = iii_jdbcTestLogin.loggedInUser;
        if (loggedInUser == null) {
            System.out.println("No user logged in. Please log in.");
            return;
        }
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "select * from bank_accounts where username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, loggedInUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean accountsFound = false;
                while (resultSet.next()) {
                    String accountNum = resultSet.getString("account_id");
                    String username = resultSet.getString("username");
                    String account_type = resultSet.getString("account_type");
                    double amount = resultSet.getDouble("amount"); // Assuming amount is stored as double
                    System.out.println("Account User: " + username);
                    System.out.println("Account Number: " + accountNum);
                    System.out.println("Account Type: " + account_type);
                    System.out.println("Amount Available: R" + amount);
                    System.out.println("---------------------------");
                    System.out.println("");
                    accountsFound = true;
                }
            if (!accountsFound) {
                System.out.println("No accounts available.");
                System.out.println("---------------------------");
                return;
            }
        } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Error: " + ex);
            }
    }

    private static void closeAccount(Scanner scanner) {
        boolean accountsFound = false;
        if (!accountsFound) {
            System.out.println("");
            return;
        } else {
            System.out.println("NB: Please ensure you have withdrawn all funds before closing account.");
            System.out.print("Enter the account number of the account you want to close: ");
            // Check if the user's input is an integer
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid account number.");
                scanner.next(); // Consume the non-integer input
                return;
            }
            int account_id = scanner.nextInt();

            try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                String sql = "delete from bank_accounts where account_id = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, account_id);

                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Account successfully closed.");
                } else {
                    System.out.println("Failed to close account. Check if an account exists before attempting to close it.");
                }
            } 
                catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Error: " + ex);
                }
        }
    }
}
