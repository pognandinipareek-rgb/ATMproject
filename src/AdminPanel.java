import javax.swing.*;

public class AdminPanel extends JFrame {

    public AdminPanel() {

        setTitle("Admin Panel");
        setSize(400,300);

        JTextArea area = new JTextArea();

        for(Account a : BankDatabase.accounts.values()) {
            area.append("Card: " + a.cardNumber +
                    "  Balance: ₹" + a.balance + "\n");
        }

        add(new JScrollPane(area));

        setVisible(true);
    }
}