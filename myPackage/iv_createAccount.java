//Functionality: Create bank accounts 
package myPackage;
import java.util.*;
import java.sql.*;

public class iv_createAccount extends v_existingAccount {

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/mzanzi_bank_system";
    static final String USERNAME = "root";
    static final String PASSWORD = "";
    //Method generates the accountNumber which goes into account_id in the bank_accounts table
    private static String accountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    public static void createNewAccount(Scanner scanner) {
        System.out.println("Creating a new account...");
        try {
            //Use the user ID of the currently logged in user
            String loggedInUser = iii_jdbcTestLogin.loggedInUser;
            if (loggedInUser == null) {
                System.out.println("No user logged in. Please log in first.");
                return;
            }
            newAccountMenu();

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Consume the invalid input
                newAccountMenu();
            }

            // Read the user's choice
            int option = scanner.nextInt();
            scanner.nextLine();

            // Switch case to handle different menu options
            String account_type = " ";
            switch (option) {
                case 1:
                    System.out.println("Creating savings account...");
                    account_type = "Savings";
                break;
                case 2:
                    System.out.println("Creating cheque account...");
                    account_type = "Cheque";
                break;
                case 3:
                    System.out.println("Creating current account...");
                    account_type = "Current";
                break;
                case 4:
                    System.out.println("Creating fixed deposit account...");
                    account_type = "Fixed-Deposit";
                break;
                case 5:
                    System.out.println("Returning to previous menu...");
                    // Go back to banking menu
                return;
                default:
                    System.out.println("Invalid entry. Please try again.");
            }
            //Limits user to creating only one of each account type
            if (accountExists(loggedInUser, account_type)) {
                System.out.println("You already have a " + account_type + " account.");
                return;
            }

            String account_id = accountNumber();
            while (accountNumExists(account_id)) {
                account_id = accountNumber();
            }

            try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {

                String sql = "insert into bank_accounts (account_id, username, account_type) values (?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, account_id);
                preparedStatement.setString(2, loggedInUser);
                preparedStatement.setString(3, account_type);
                
                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Account created successfully.");
                } else {
                    System.out.println("Failed to create account.");
                }
            } 
                catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println("Error: " + ex);
                }            
        }   
            catch(NoSuchElementException ex) {
                ex.printStackTrace();
                System.out.println("Error: " + ex);
            }
    }

    private static void newAccountMenu() {
        System.out.println("What kind of account do you want to open?");
        System.out.println("1. Savings Account");
        System.out.println("2. Cheque Account");
        System.out.println("3. Current Account");
        System.out.println("4. Fixed Deposit Account");
        System.out.println("5. Go Back");
        System.out.print("Choose an option: ");
    }

    private static boolean accountExists(String loggedInUser, String account_type) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)){
            String sql = "select count(*) from bank_accounts where username = ? and account_type = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, loggedInUser);
            preparedStatement.setString(2, account_type);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
            
        } catch (Exception ex) {
            ex.getStackTrace();
            System.out.println("Error: " + ex);
        }
        return false;
    }

    private static boolean accountNumExists(String account_id) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "select count(*) as count from bank_accounts where account_id = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, account_id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt("count");
                        return count > 0;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex);    
        }   
        return false;
    } 
}
