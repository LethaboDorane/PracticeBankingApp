//Creates the Mzanzi Bank backend system.
import java.sql.*;
import myPackage.*;


public class createTables extends i_startingMenu {

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/";
    static final String DATABASE_NAME = "mzansi_bank_system";
    static final String USERNAME = "root";
    static final String PASSWORD = "";

    public static void main(String[] args) {
        createDatabase();
        createUserAccountTables();
    }
    //Creates the mzanzi_bank_system database
    private static void createDatabase() {

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement()) {
                String sql = "create database if not exists " + DATABASE_NAME;
                statement.executeUpdate(sql);
                System.out.println("Database successfully created.");
            }    
            catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("Error:" + ex);
                System.out.println("Please start-up your MySQL server before running this application.");
                System.exit(0);
            }
    }
    //Creates the user and account tables 
    private static void createUserAccountTables() {

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement()) {
                String sql1 = "use mzansi_bank_system;";
                String sql2 = "create table if not exists users (\r\n" + //
                                        "    user_id int auto_increment primary key,\r\n" + //
                                        "    fname varchar(50) not null check (length(fname) >= 2),\r\n" + //
                                        "    surname varchar(50) not null check (length(surname) >= 2),\r\n" + //
                                        "    username varchar(50) unique not null check (length(username) >= 7),\r\n" + //
                                        "    password varchar(50) not null check (length(password) >= 10)\r\n" + //
                                        ");";
                String sql3 = "create table if not exists bank_accounts (\r\n" + //
                                        "    account_id varchar(10) primary key,\r\n" + //
                                        "    username varchar(50) references users(username),\r\n" + //
                                        "    account_type ENUM('Savings', 'Cheque', 'Current', 'Fixed-Deposit') not null,\r\n" + //
                                        "    amount decimal(10,2) default 0.00,\r\n" + //
                                        "    foreign key (username) references users(username)\r\n" + //
                                        ");";

                statement.executeUpdate(sql1);
                statement.executeUpdate(sql2);
                statement.executeUpdate(sql3);
                System.out.println("Tables successfully created.");
            }    
            catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("Error: " + ex);
                System.out.println("Please start-up your MySQL server before running this application.");
                System.exit(0);
            }
    }
}
