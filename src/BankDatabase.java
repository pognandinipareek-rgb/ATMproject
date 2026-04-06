import java.io.*;
import java.util.*;

public class BankDatabase {

    public static HashMap<String, Account> accounts = new HashMap<>();

    public static void loadAccounts() {

        try {

            BufferedReader br = new BufferedReader(new FileReader("accounts.txt"));
            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|");

                String card = data[0];
                int pin = Integer.parseInt(data[1]);
                double balance = Double.parseDouble(data[2]);

                accounts.put(card, new Account(card, pin, balance));
            }

            br.close();

        } catch (Exception e) {
            System.out.println("Error loading accounts.");
        }
    }

    public static void saveAccounts() {

        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter("accounts.txt"));

            for (Account a : accounts.values()) {

                bw.write(a.cardNumber + "|" + a.pin + "|" + a.balance);
                bw.newLine();

            }

            bw.close();

        } catch (Exception e) {
            System.out.println("Error saving accounts.");
        }
    }
}