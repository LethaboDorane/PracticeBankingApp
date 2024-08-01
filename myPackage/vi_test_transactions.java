//Transaction: Perform transactions on this page
package myPackage;

import java.sql.*;
import java.util.Scanner;

public class vi_test_transactions {

    public static void performTransactions(Scanner scanner) {
        System.out.println("Performing transactions...");
        System.out.print("To begin transacting, enter your account number: ");
        String account_id = scanner.nextLine(); // Prompt user to enter account number
        System.out.println("");
        try (Connection conn = DriverManager.getConnection(vii_AbstractBankAccount.JDBC_URL, vii_AbstractBankAccount.USERNAME, vii_AbstractBankAccount.PASSWORD)) {
            vii_AbstractBankAccount account = getAccountDetails(conn, account_id);            

            while (true) {

                transactionMenu();

                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    scanner.nextLine();
                    transactionMenu();
                }

                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        System.out.println("Checking balance...");
                        System.out.println("Current balance: R" + account.balance);
                        break;
                    case 2:
                        System.out.print("Enter amount to deposit: ");
                        double depositAmount = scanner.nextDouble();
                        scanner.nextLine();
                        account.deposit(depositAmount);
                        updateAccountBalance(conn, account.getAccount_id(), account.balance);
                        break;
                    case 3:
                        System.out.print("Enter amount to withdraw: ");
                        double withdrawAmount = scanner.nextDouble();
                        scanner.nextLine();
                        account.withdraw(withdrawAmount);
                        updateAccountBalance(conn, account.getAccount_id(), account.balance);
                        break;
                    case 4:
                        System.out.print("Enter amount to transfer: ");
                        double transferAmount = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Enter recipient's account number: ");
                        String recipientAccount_id = scanner.nextLine();
                        vii_AbstractBankAccount recipientAccount = getAccountDetails(conn, recipientAccount_id);
                        if (recipientAccount != null) {
                            account.transferFunds(recipientAccount, transferAmount);
                            updateAccountBalance(conn, account.getAccount_id(), account.balance);
                            updateAccountBalance(conn, recipientAccount.getAccount_id(), recipientAccount.balance);
                        } else {
                            System.out.println("Recipient account not found.");
                        }
                        break;
                    case 5:
                        System.out.println("Returning to previous menu...");
                        return;
                    default:
                        System.out.println("Invalid entry. Please try again.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex);
        }
    }

    private static void transactionMenu() {
        System.out.println("What would you like to do?");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit Cash");
        System.out.println("3. Withdraw Cash");
        System.out.println("4. Transfer Funds");
        System.out.println("5. Go Back");
        System.out.print("Choose an option: ");
    }

    private static vii_AbstractBankAccount getAccountDetails(Connection conn, String account_id) throws SQLException {
        String sql = "SELECT * FROM bank_accounts WHERE account_id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, account_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String account_type = resultSet.getString("account_type");
                    double amount = resultSet.getDouble("amount");
                    System.out.println("Account User: " + username);
                    System.out.println("Account Number: " + account_id);
                    System.out.println("Account Type: " + account_type);
                    //System.out.println("Amount Available: R" + amount);
                    System.out.println("---------------------------");
                    System.out.println("");

                    return new vii_AbstractBankAccount(account_id, amount, conn) {
                        @Override
                        public double checkBalance(){
                            return amount;
                        }
                    };
                } else {
                    System.out.println("No account found with account number: " + account_id);
                    throw new SQLException("No account with number: " + account_id);
                }
            }
        }
    }

    private static void updateAccountBalance(Connection conn, String account_id, double newBalance) throws SQLException {
        String sql = "UPDATE bank_accounts SET amount = ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, account_id);
            preparedStatement.executeUpdate();
        }
    }
}
