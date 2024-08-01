//Banking transaction functions
package myPackage;
import java.sql.*;

public abstract class vii_AbstractBankAccount implements viii_BankingOperations{

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/mzanzi_bank_system";
    static final String USERNAME = "root";
    static final String PASSWORD = "";

    protected double balance;
    protected String account_id;
    protected Connection conn;

    public vii_AbstractBankAccount(String account_id, double balance, Connection conn) {
        this.account_id = account_id;
        this.balance = balance;
        this.conn = conn;
    }
    
    public String getAccount_id() {
        return account_id;
    }

    protected void updateAccountBalance(double newBalance) throws SQLException {
        String sql = "update bank_accounts set amount = ? where account_id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, account_id);
            preparedStatement.executeUpdate();
        }
    }

    public void BankAccount (double initialBalance) {
        this.balance = initialBalance;
    }
    @Override
    public double checkBalance() {
        return this.balance;
    }
    @Override
    public void deposit(double amount) {
        this.balance+=amount;
        System.out.println("R" + amount + " deposit successful.");
        System.out.println("Current balance is: R" + balance + ".");
        try {
            updateAccountBalance(balance);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex);
        }
    }
    @Override
    public void withdraw(double amount) {
        System.out.println("Enter amount: " + amount);
        if (this.balance >= amount) {
            this.balance-=amount;
            System.out.println("R" + amount + " withdrawal successful.");
            System.out.println("Remaining balance is: R" + balance + ".");
            try {
                updateAccountBalance(balance);
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("Error: " + ex);
            }
        }
        else {
            System.out.println("");
            System.out.println("Insufficient balance. Please try again.");
        }
    }
    @Override
    public void transferFunds(vii_AbstractBankAccount targetAccount, double amount) {
        if (this.balance >= amount) {
            this.withdraw(amount);
            targetAccount.deposit(amount);
            System.out.println("R" + amount + " sent to " + targetAccount + ".");
            System.out.println("Remaining balance is: R" + balance + ".");
        }
        else {
            System.out.println("");
            System.out.println("Insufficient balance. Please try again.");
        }
    }
}
