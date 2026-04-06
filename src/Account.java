import java.util.*;

public class Account {

    String cardNumber;
    int pin;
    double balance;
    List<String> history = new ArrayList<>();

    public Account(String cardNumber, int pin, double balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public void deposit(double amount) {
        balance += amount;
        history.add("Deposit: ₹" + amount);
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            history.add("Withdraw: ₹" + amount);
            return true;
        }
        history.add("Failed withdraw: ₹" + amount);
        return false;
    }

    public void transfer(Account target, double amount) {
        if (withdraw(amount)) {
            target.deposit(amount);
            history.add("Transfer to " + target.cardNumber + ": ₹" + amount);
        }
    }
}