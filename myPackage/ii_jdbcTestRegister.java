//Registration: Storing new user credentials
package myPackage;
import java.util.*;
import java.sql.*;

public class ii_jdbcTestRegister extends iii_jdbcTestLogin {

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/mzanzi_bank_system";
    static final String USERNAME = "root";
    static final String PASSWORD = "";

    //Method to handle the user registration
    public static void register(Scanner scanner) {
        String fname, surname, username, password;
        
        do {
            System.out.print("Enter name: ");
            fname = scanner.nextLine();
            if (fname.length() < 2) {
                System.out.println("Username must contain at least 2 characters. Please try again.");
                return;
            } else if (fname.contains(" ")) {
                System.out.println("Username cannot contain spaces. Please try again.");
                return;
            }            
        } while (fname.length() < 2 || fname.contains(" "));
        
        do {
            System.out.print("Enter surname: ");
            surname = scanner.nextLine();
            if (surname.length() < 2) {
                System.out.println("Surname must contain at least 2 characters. Please try again.");
                return;
            } else if (surname.contains(" ")) {
                System.out.println("Username cannot contain spaces. Please try again.");
                return;
            }            
        } while (surname.length() < 2 || surname.contains(" "));

        //Prompt the user to enter a username
        do {
            System.out.print("Enter username (without spaces): ");
            username = scanner.nextLine();
            if (username.length() < 7) {
                System.out.println("Username must contain at least 7 characters. Please try again.");
                return;
            } else if (username.contains(" ")) {
                System.out.println("Username cannot contain spaces. Please try again.");
                return;
            }            
        } while (username.length() < 7 || username.contains(" "));


        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String checkSql = "select * from users where username = ?";
            PreparedStatement checkStatement = conn.prepareStatement(checkSql);
            checkStatement.setString(1, username);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Username already exists.");
                return;
            }

            //Prompts users to enter password
            do {
                System.out.print("Enter password (without spaces): ");
                password = scanner.nextLine();
                if (password.length() < 10) {
                    System.out.println("Password must contain at least 10 characters. Please try again.");
                } else if (password.contains(" ")) {
                    System.out.println("Password cannot contain spaces. Please try again.");
                }            
            } while (password.length() < 10 || password.contains(" "));
            
            //inserts credentials of new user into 'users' database
            String sql = "insert into users (fname, surname, username, password) values (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, fname);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Registration successful.");
            }
            else{
                System.out.println("Failed to register user.");
            }

        } catch(SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex);
        }
    }
}
