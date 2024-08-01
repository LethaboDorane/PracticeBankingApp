//Authentication: Accessing existing user credentials
package myPackage;
import java.util.*;
import java.sql.*;

public class iii_jdbcTestLogin extends iv_createAccount {

    //Variable to store the currently logged in user
    static String loggedInUser = null;

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/mzanzi_bank_system";
    static final String USERNAME = "root";
    static final String PASSWORD = "";

    //Method to handle the user login
    public static void login(Scanner scanner) {
        //Prompts the user to enter their username
        System.out.print("Enter username: " );
        String username = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {

            String sql = "select password from users where username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                String storedPassword = resultSet.getString("password");

                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                if (storedPassword.equals(password)) {
                    loggedInUser = username;
                    System.out.println("Login successful.");
                    System.out.println("Welcome " + loggedInUser + "!");
                    while (true) {
                        System.out.println("Menu:");
                        System.out.println("1. Create a new account");
                        System.out.println("2. View existing accounts");
                        System.out.println("3. Perform transactions");
                        System.out.println("4. Logout");
                        System.out.print("Choose an option: ");
        
                        // Read the user's choice
                        int option = scanner.nextInt();
                        scanner.nextLine();
        
                        //Switch case to handle different menu options
                        switch (option) {
                            case 1:
                                createNewAccount(scanner);
                            break;
                            case 2:
                                viewExistingAccounts(scanner);
                            break;
                            case 3:
                                performTransactions(scanner);
                            break;
                            case 4:
                                System.out.println("You have been logged out.");
                                return;
                            default:
                                System.out.println("Invalid entry. Please try again.");
                        }            
                    
                    }
                
                } else {
                    System.out.println("Incorrect password. Please try again.");
                    } 
            } else {
                System.out.println("Username not found. Please try again.");
                }     
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex);
            }
        
    }
}
