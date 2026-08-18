import java.io.*;
import java.util.*;

public class BankManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Bank bank = new Bank();

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("       WELCOME TO JAVA NATIONAL BANK");
        System.out.println("==========================================");
        loadData();
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    bank.displayAllAccounts();
                    break;
                case 4:
                    searchAccount();
                    break;
                case 5:
                    bank.showBankStatistics();
                    break;
                case 6:
                    saveData();
                    System.out.println("Data saved successfully.");
                    break;
                case 7:
                    saveData();
                    System.out.println("Thank you for using Java National Bank!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    private static void printMainMenu() {
        System.out.println("\n==========================================");
        System.out.println("                 MAIN MENU");
        System.out.println("==========================================");
        System.out.println("1. Create Account");
        System.out.println("2. Login");
        System.out.println("3. Display All Accounts");
        System.out.println("4. Search Account");
        System.out.println("5. Bank Statistics");
        System.out.println("6. Save Data");
        System.out.println("7. Exit");
        System.out.println("==========================================");
    }
    private static void createAccount() {
        System.out.println("\n========== CREATE ACCOUNT ==========");
        String name = getString("Enter your name: ");
        String phone = getString("Enter phone number: ");
        String email = getString("Enter email: ");
        String password = getString("Create password: ");
        System.out.println("\nSelect account type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        int type = getInt("Choice: ");
        Account account;
        String accountNumber = bank.generateAccountNumber();
        if (type == 1) {
            account = new SavingsAccount(accountNumber, name, phone, email, password, 5.0);
        } else if (type == 2) {
            account = new CurrentAccount(accountNumber, name, phone, email, password, 50000);
        } else {
            System.out.println("Invalid account type.");
            return;
        }
        bank.addAccount(account);
        System.out.println("\nAccount created successfully!");
        System.out.println("------------------------------------------");
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Name: " + account.getName());
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("------------------------------------------");
    }
    private static void login() {
        System.out.println("\n========== LOGIN ==========");
        String accountNumber = getString("Enter account number: ");
        String password = getString("Enter password: ");
        Account account = bank.login(accountNumber, password);
        if (account == null) {
            System.out.println("Invalid account number or password.");
            return;
        }
        System.out.println("\nLogin successful!");
        accountMenu(account);
    }
    private static void accountMenu(Account account) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n==========================================");
            System.out.println("             ACCOUNT MENU");
            System.out.println("==========================================");
            System.out.println("Welcome, " + account.getName());
            System.out.println("\n1. View Profile");
            System.out.println("2. Check Balance");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Transfer Money");
            System.out.println("6. Transaction History");
            System.out.println("7. Apply Interest");
            System.out.println("8. Change Password");
            System.out.println("9. Logout");
            int choice = getInt("Choice: ");
            switch (choice) {
                case 1:
                    account.displayProfile();
                    break;
                case 2:
                    account.displayBalance();
                    break;
                case 3:
                    depositMoney(account);
                    break;
                case 4:
                    withdrawMoney(account);
                    break;
                case 5:
                    transferMoney(account);
                    break;
                case 6:
                    account.showTransactions();
                    break;
                case 7:
                    account.applyInterest();
                    break;
                case 8:
                    changePassword(account);
                    break;
                case 9:
                    System.out.println("Logged out successfully.");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    private static void depositMoney(Account account) {
        double amount = getDouble("Enter amount to deposit: ");
        try {
            account.deposit(amount);
            System.out.println("Deposit successful.");
            account.displayBalance();
        } catch (InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void withdrawMoney(Account account) {
        double amount = getDouble("Enter amount to withdraw: ");
        try {
            account.withdraw(amount);
            System.out.println("Withdrawal successful.");
            account.displayBalance();
        } catch (InsufficientBalanceException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void transferMoney(Account sender) {
        String receiverNumber = getString("Enter receiver account number: ");
        Account receiver = bank.findAccount(receiverNumber);
        if (receiver == null) {
            System.out.println("Receiver account not found.");
            return;
        }
        double amount = getDouble("Enter amount: ");
        try {
            sender.transfer(receiver, amount);
            System.out.println("Transfer successful!");
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }
    private static void changePassword(Account account) {
        String oldPassword = getString("Enter old password: ");
        if (!account.checkPassword(oldPassword)) {
            System.out.println("Incorrect password.");
            return;
        }
        String newPassword = getString("Enter new password: ");
        account.setPassword(newPassword);
        System.out.println("Password changed successfully.");
    }
    private static void searchAccount() {
        String number = getString("Enter account number: ");
        Account account = bank.findAccount(number);
        if (account == null) {
            System.out.println("Account not found.");
        } else {
            account.displayProfile();
        }
    }
    private static void saveData() {
        try {
            FileOutputStream file = new FileOutputStream("bank_data.dat");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(bank);
            output.close();
            file.close();
        } catch (IOException e) {
            System.out.println("Could not save data.");
        }
    }
    private static void loadData() {
        File file = new File("bank_data.dat");
        if (!file.exists()) {
            return;
        }
        try {
            FileInputStream inputFile = new FileInputStream(file);
            ObjectInputStream input = new ObjectInputStream(inputFile);
            Bank loadedBank = (Bank) input.readObject();
            bank.copyFrom(loadedBank);
            input.close();
            inputFile.close();
            System.out.println("Previous data loaded.");
        } catch (Exception e) {
            System.out.println("Could not load previous data.");
        }
    }
    private static String getString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
    private static int getInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    private static double getDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }
}
enum AccountType {
    SAVINGS,
    CURRENT
}
enum TransactionType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER_SENT,
    TRANSFER_RECEIVED,
    INTEREST
}
class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private TransactionType type;
    private double amount;
    private String description;
    private Date date;
    public Transaction(TransactionType type, double amount, String description) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = new Date();
    }
    public void display() {
        System.out.println(date + " | " + type + " | Rs. " + amount + " | " + description);
    }
}
abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accountNumber;
    private String name;
    private String phone;
    private String email;
    private String password;
    protected double balance;
    private AccountType accountType;
    private ArrayList<Transaction> transactions;
    public Account(String accountNumber, String name, String phone, String email, String password, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public double getBalance() {
        return balance;
    }
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
        balance += amount;
        transactions.add(new Transaction(TransactionType.DEPOSIT, amount, "Cash deposited"));
    }
    public abstract void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException;
    public abstract void applyInterest();
    public void transfer(Account receiver, double amount) throws InvalidAmountException, InsufficientBalanceException {
        if (receiver == this) {
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }
        withdraw(amount);
        receiver.receiveTransfer(amount);
        transactions.add(new Transaction(TransactionType.TRANSFER_SENT, amount, "Transferred to " + receiver.getAccountNumber()));
    }
    private void receiveTransfer(double amount) {
        balance += amount;
        transactions.add(new Transaction(TransactionType.TRANSFER_RECEIVED, amount, "Money received"));
    }
    public void displayProfile() {
        System.out.println("\n================================");
        System.out.println("          ACCOUNT PROFILE");
        System.out.println("================================");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Account Type: " + accountType);
        System.out.printf("Balance: Rs. %.2f%n", balance);
        System.out.println("================================");
    }
    public void displayBalance() {
        System.out.printf("\nCurrent Balance: Rs. %.2f%n", balance);
    }
    public void showTransactions() {
        System.out.println("\n========== TRANSACTION HISTORY ==========");
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        for (Transaction transaction : transactions) {
            transaction.display();
        }
    }
}
class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    private double interestRate;
    public SavingsAccount(String accountNumber, String name, String phone, String email, String password, double interestRate) {
        super(accountNumber, name, phone, email, password, AccountType.SAVINGS);
        this.interestRate = interestRate;
    }
    @Override
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
        if (amount > balance) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }
        balance -= amount;
    }
    @Override
    public void applyInterest() {
        double interest = balance * interestRate / 100;
        balance += interest;
        System.out.printf("Interest added: Rs. %.2f%n", interest);
    }
}
class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;
    private double overdraftLimit;
    public CurrentAccount(String accountNumber, String name, String phone, String email, String password, double overdraftLimit) {
        super(accountNumber, name, phone, email, password, AccountType.CURRENT);
        this.overdraftLimit = overdraftLimit;
    }
    @Override
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
        if (amount > balance + overdraftLimit) {
            throw new InsufficientBalanceException("Amount exceeds overdraft limit.");
        }
        balance -= amount;
    }
    @Override
    public void applyInterest() {
        System.out.println("Current accounts do not receive interest.");
    }
}
class Bank implements Serializable {
    private static final long serialVersionUID = 1L;
    private HashMap<String, Account> accounts;
    private int accountCounter;
    public Bank() {
        accounts = new HashMap<>();
        accountCounter = 1000;
    }
    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }
    public String generateAccountNumber() {
        accountCounter++;
        return "PK" + accountCounter;
    }
    public Account findAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
    public Account login(String accountNumber, String password) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            return null;
        }
        if (account.checkPassword(password)) {
            return account;
        }
        return null;
    }
    public void displayAllAccounts() {
        System.out.println("\n========== ALL ACCOUNTS ==========");
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }
        for (Account account : accounts.values()) {
            System.out.println("--------------------------------");
            System.out.println("Account: " + account.getAccountNumber());
            System.out.println("Name: " + account.getName());
            System.out.println("Type: " + account.getAccountType());
            System.out.printf("Balance: Rs. %.2f%n", account.getBalance());
        }
    }
    public void showBankStatistics() {
        int totalAccounts = accounts.size();
        int savings = 0;
        int current = 0;
        double totalMoney = 0;
        for (Account account : accounts.values()) {
            totalMoney += account.getBalance();
            if (account.getAccountType() == AccountType.SAVINGS) {
                savings ++;
            else {
                current ++;
            }
        }
        System.out.println("\n========== BANK STATISTICS ==========");
        System.out.println("Total Accounts: " + totalAccounts);
        System.out.println("Savings Accounts: " + savings);
        System.out.println("Current Accounts: " + current);
        System.out.printf("Total Bank Balance: Rs. %.2f%n", totalMoney);
    }
    public void copyFrom(Bank other) {
        this.accounts = other.accounts;
        this.accountCounter = other.accountCounter;
    }
}
class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
