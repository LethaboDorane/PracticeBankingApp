package myPackage;

public interface viii_BankingOperations {

    double checkBalance();
    void deposit(double amount);
    void withdraw(double amount);
    void transferFunds(vii_AbstractBankAccount targetAccount, double amount);

}
