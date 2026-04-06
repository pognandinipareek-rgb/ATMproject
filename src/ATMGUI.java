import javax.swing.*;
import java.awt.*;

public class ATMGUI extends JFrame {

    Account currentAccount;
    int loginAttempts = 0;

    JTextField cardField;
    JPasswordField pinField;
    JTextArea screen;

    public ATMGUI() {

        setTitle("ATM Machine");
        setSize(700,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // HEADER
        JLabel title = new JLabel("BANK ATM MACHINE", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBackground(new Color(0,102,204));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);

        add(title, BorderLayout.NORTH);

        // ATM SCREEN
        screen = new JTextArea();
        screen.setFont(new Font("Monospaced", Font.BOLD, 18));
        screen.setBackground(Color.BLACK);
        screen.setForeground(Color.GREEN);
        screen.setEditable(false);

        add(new JScrollPane(screen), BorderLayout.CENTER);

        // LOGIN PANEL
        JPanel loginPanel = new JPanel(new GridLayout(3,2,10,10));
        loginPanel.setBorder(BorderFactory.createTitledBorder("Insert Card"));

        loginPanel.add(new JLabel("Card Number"));
        cardField = new JTextField();
        loginPanel.add(cardField);

        loginPanel.add(new JLabel("PIN"));
        pinField = new JPasswordField();
        loginPanel.add(pinField);

        JButton loginBtn = new JButton("LOGIN");

        loginPanel.add(new JLabel());
        loginPanel.add(loginBtn);

        add(loginPanel, BorderLayout.WEST);

        // ATM BUTTONS
        JPanel buttons = new JPanel(new GridLayout(3,2,10,10));
        buttons.setBorder(BorderFactory.createTitledBorder("ATM Options"));

        JButton balance = new JButton("Balance");
        JButton deposit = new JButton("Deposit");
        JButton withdraw = new JButton("Withdraw");
        JButton transfer = new JButton("Transfer");
        JButton history = new JButton("History");
        JButton admin = new JButton("Admin");

        buttons.add(balance);
        buttons.add(deposit);
        buttons.add(withdraw);
        buttons.add(transfer);
        buttons.add(history);
        buttons.add(admin);

        add(buttons, BorderLayout.EAST);

        // KEYPAD
        JPanel keypad = new JPanel(new GridLayout(4,3,10,10));
        keypad.setBorder(BorderFactory.createTitledBorder("Keypad"));

        for(int i=1;i<=9;i++){

            JButton btn = new JButton(String.valueOf(i));

            btn.addActionListener(e -> {
                pinField.setText(pinField.getText() + btn.getText());
            });

            keypad.add(btn);
        }

        JButton zero = new JButton("0");

        zero.addActionListener(e -> {
            pinField.setText(pinField.getText() + "0");
        });

        JButton clear = new JButton("Clear");

        clear.addActionListener(e -> {
            pinField.setText("");
        });

        JButton enter = new JButton("Enter");

        enter.addActionListener(e -> login());

        keypad.add(zero);
        keypad.add(clear);
        keypad.add(enter);

        add(keypad, BorderLayout.SOUTH);

        // ACTIONS
        loginBtn.addActionListener(e -> login());
        balance.addActionListener(e -> showBalance());
        deposit.addActionListener(e -> deposit());
        withdraw.addActionListener(e -> withdraw());
        transfer.addActionListener(e -> transfer());
        history.addActionListener(e -> history());
        admin.addActionListener(e -> new AdminPanel());

        setVisible(true);
    }

    void loadingAnimation(){

        screen.setText("Processing");

        new Thread(() -> {
            try{

                for(int i=0;i<5;i++){

                    Thread.sleep(400);
                    screen.append(".");
                }

                screen.append("\nPlease wait");

            }catch(Exception e){}
        }).start();
    }

    void login() {

        String card = cardField.getText();
        int pin = Integer.parseInt(new String(pinField.getPassword()));

        if(BankDatabase.accounts.containsKey(card) &&
                BankDatabase.accounts.get(card).pin == pin) {

            currentAccount = BankDatabase.accounts.get(card);

            screen.setText("Login Successful\nWelcome!");

            loginAttempts = 0;

        } else {

            loginAttempts++;
            screen.setText("Invalid PIN");

            if(loginAttempts >= 3)
                screen.setText("Card Blocked!");
        }
    }

    void showBalance() {

        if(currentAccount != null)
            screen.setText("Balance: ₹" + currentAccount.balance);
    }

    void deposit() {

        loadingAnimation();

        double amt = Double.parseDouble(
                JOptionPane.showInputDialog("Enter amount"));

        currentAccount.deposit(amt);
        BankDatabase.saveAccounts();

        screen.setText("Deposited ₹" + amt);
    }

    void withdraw() {

        loadingAnimation();

        double amt = Double.parseDouble(
                JOptionPane.showInputDialog("Enter amount"));

        if(currentAccount.withdraw(amt))
            screen.setText("Withdrawn ₹" + amt);
        else
            screen.setText("Insufficient Balance");

        BankDatabase.saveAccounts();
    }

    void transfer() {

        loadingAnimation();

        String targetCard =
                JOptionPane.showInputDialog("Target Card Number");

        double amt =
                Double.parseDouble(JOptionPane.showInputDialog("Amount"));

        Account target = BankDatabase.accounts.get(targetCard);

        if(target != null) {

            currentAccount.transfer(target, amt);
            screen.setText("Transfer Successful");

        } else
            screen.setText("Account not found");

        BankDatabase.saveAccounts();
    }

    void history() {

        StringBuilder h = new StringBuilder();

        for(String s : currentAccount.history)
            h.append(s).append("\n");

        screen.setText(h.toString());
    }

    public static void main(String[] args) {

        BankDatabase.loadAccounts();

        new ATMGUI();
    }

}