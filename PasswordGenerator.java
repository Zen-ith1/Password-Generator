import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Random;
import javax.swing.*;

public class PasswordGenerator {
	
    private static final String DICTIONARY = "abcdefghijklmnopqrstuvwxyz";
    private static final String SYMBOLS = "!@#$%^&*()_+-=[]{};':\",./<>?\\|";
    private static final Random RANDOM = new Random();
    private static JTextField passwordField;

    public static void main(String[] args) {
    	
    	String intro = "<html><font face='Arial' size='24'>Welcome to <i>PassGen X!</i></font></html>";
    	
    	// Welcome message
    	JOptionPane.showMessageDialog(null, intro  +  "\n"
    			+ "\nWe recommend a minimum of 12 characters for a password length\n"
    			+ "\n*NOTE*"
    			+ "\nThis is still a working in progress, so there may still be some bugs in the system\n"
    			+ "\n \nCreators: Gavin Yoder and Jacob Miller", "Welcome!", JOptionPane.INFORMATION_MESSAGE);
    	
    	 JFrame frame = new JFrame("Password Generator");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         // create a label for the length of the password
         JLabel lengthLabel = new JLabel("Enter a Minimum Length for the Password:");
         lengthLabel.setBounds(10, 50, 250, 30);

         // create a text field for user input
         JTextField lengthField = new JTextField();
         lengthField.setBounds(260, 50, 50, 30);

         // create a button to generate the password
         JButton generateButton = new JButton("Generate");
         generateButton.setBounds(110, 80, 100, 30);
         
         JButton copyButton = new JButton("Copy to clipboard");
         copyButton.setBounds(90, 110, 140, 30);
         
         generateButton.addActionListener(e -> {
        	 int wordLength = Integer.parseInt(lengthField.getText());
        	 String word = generateRandomWord(wordLength);
             String password = addRandomCapitalLetters(word);
             password = addRandomNumbers(password);
             password = addRandomSymbols(password);
             password = shuffleString(password);
             copyButton.setEnabled(true);
             JOptionPane.showMessageDialog(null, password, "Generated Password", JOptionPane.INFORMATION_MESSAGE);
         });
    	
         copyButton.addActionListener(e -> {
             String password = passwordField.getText();
             copyToClipboard(password);
             JOptionPane.showMessageDialog(null, "Password copied to clipboard");
         });
         
        passwordField = new JTextField();
     	passwordField.setBounds(10, 150, 300, 30);
     	passwordField.setEditable(false); // Set the field as read-only
     	frame.add(passwordField);
         // add components to the frame
         frame.add(lengthLabel);
         frame.add(lengthField);
         frame.add(generateButton);
         frame.add(copyButton);
         frame.setSize(330, 180);
         frame.setLayout(null);
         frame.setVisible(true);
        
    }
    
    private static void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private static String generateRandomWord(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(DICTIONARY.length());
            sb.append(DICTIONARY.charAt(index));
        }
        return sb.toString();
    }

    private static String addRandomCapitalLetters(String password) {
        StringBuilder sb = new StringBuilder(password);
        int numCaps = RANDOM.nextInt(3) + 1; // 1 to 3 random capital letters
        for (int i = 0; i < numCaps; i++) {
            int index = RANDOM.nextInt(password.length());
            char ch = Character.toUpperCase(password.charAt(index));
            sb.setCharAt(index, ch);
        }
        return sb.toString();
    }

    private static String addRandomNumbers(String password) {
        StringBuilder sb = new StringBuilder(password);
        int numDigits = RANDOM.nextInt(4) + 1; // 1 to 4 random digits
        for (int i = 0; i < numDigits; i++) {
            int index = RANDOM.nextInt(password.length());
            char ch = Character.forDigit(RANDOM.nextInt(10), 10);
            sb.insert(index, ch);
        }
        return sb.toString();
    }

    private static String addRandomSymbols(String password) {
        StringBuilder sb = new StringBuilder(password);
        int numSymbols = RANDOM.nextInt(2) + 1; // 1 to 2 random symbols
        for (int i = 0; i < numSymbols; i++) {
            int index = RANDOM.nextInt(password.length());
            char ch = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
            sb.insert(index, ch);
        }
        return sb.toString();
    }

    private static String shuffleString(String password) {
        char[] chars = password.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int j = RANDOM.nextInt(chars.length);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
}