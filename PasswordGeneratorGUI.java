/**
 * This class generates a random password. It uses a simple interface for users to generate passwords and copy them to clipboard.
 * 
 * @author Jacob Miller and Gavin Yoder
 * 
 */

import javax.swing.*;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PasswordGeneratorGUI {
	private static JTextField passwordField;
    public static void main(String[] args) {
    	try {
    		
    	String intro = "<html><font face='Arial' size='24'>Welcome to <i>PassGen Y!</i></font></html>";
    		
    	// Welcome message
    	JOptionPane.showMessageDialog(null, intro  +  "\n"
    			+ "\nThis is an upgraded version of PassGen X."
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
        

        // add action listener to the button
        generateButton.addActionListener(e -> {
        	try {
        		int wordLength = Integer.parseInt(lengthField.getText());
        		String password = "";
        		String[] options = {"Have a Word", "Random letters", "Quit"};
        		var choice = JOptionPane.showOptionDialog(null, "", "Select an option", 1, 3, null, options, options[0]);
        		if(choice == JOptionPane.YES_OPTION) {		// Have a word
        			String[] buttons = {"Word 1", "Word 2", "Word 3", "Word 4", "Word 5", "Word 6", "Reroll"};
        			List<String> passphrases = new ArrayList<>();
        			passphrases.addAll(findWords(wordLength));
        			StringBuilder sb = new StringBuilder();
        			for(int i = 0; i < passphrases.size(); i++) {
        				sb.append(passphrases.get(i));
        				sb.append("   ");
        			}
        			var choice2 = JOptionPane.showOptionDialog(null, sb, "Select an Option", 1, 3, null, buttons, buttons[0]);
        			String word = "";
        			do {
        				int i = 0;
        				try {
        					switch (choice2) {
                			case 0:
                				word = passphrases.get(i);
                				break;
                			case 1:
                				word = passphrases.get(i+1);
                				break;
                			case 2:
                				word = passphrases.get(i+2);
                				break;
                			case 3:
                				word = passphrases.get(i+3);
                				break;
                			case 4:
                				word = passphrases.get(i+4);
                				break;
                			case 5:
                				word = passphrases.get(i+5);
                				break;
                			case 6:
                				StringBuilder new_sb = new StringBuilder();
                				int j = i+6;
                				while(j < passphrases.size()) {
                					new_sb.append(passphrases.get(j));
                    				new_sb.append("   ");
                				}
                				i = i+6;
                				choice2 = JOptionPane.showOptionDialog(null, new_sb, "Select an Option", 1, 3, null, buttons, buttons[0]);
                				break;
            				} 
        				} catch (IndexOutOfBoundsException g) {
        					JOptionPane.showMessageDialog(null, "There are no more words to use");
        				} catch (IllegalArgumentException l) {
        					JOptionPane.showMessageDialog(null, "Exited Program", "Exited Program", JOptionPane.INFORMATION_MESSAGE);
                			System.exit(0);
        				}
        			} while (choice2 == 6);
        			
                     password = addRandomNumbers();
                     password = addRandomSymbols(password);
                     password = shuffleString(password);
                     password = word.concat(password);
        			 password = addRandomCapitalLetters(password);
        			 password = checkPassword(password, wordLength);
                     passwordField.setText(password);
        		} else if(choice == JOptionPane.NO_OPTION) {		// Random letters
        			String word = generateRandomWord(wordLength);
                    password = addRandomCapitalLetters(word);
                    password = addRandomNumbers(password);
                    password = addRandomSymbols(password);
                    password = shuffleString(password);
                    password = checkPassword(password, wordLength);
                    passwordField.setText(password);
        		} else {
        			JOptionPane.showMessageDialog(null, "Exited Program", "Exited Program", JOptionPane.INFORMATION_MESSAGE);
        			System.exit(0);
        		}
                copyButton.setEnabled(true);
                JOptionPane.showMessageDialog(null, password, "Generated Password", JOptionPane.INFORMATION_MESSAGE);
        	} catch (NumberFormatException f) {
        		JOptionPane.showMessageDialog(null, "Error: No letters allowed", "Error", 0);
        	}   
        });		// END

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
    	} catch (IllegalArgumentException i) {
    		JOptionPane.showMessageDialog(null, "Exited Program", "Exited Program", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
    	}
    }	// END MAIN
    
    private static void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    // existing password generator code
    private static final String DICTIONARY = "abcdefghijklmnopqrstuvwxyz";
    private static final String SYMBOLS = "!@#$%^&*()_+-=[]{};':\",./<>?\\|";
    private static final Random RANDOM = new Random();

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

    private static String addRandomNumbers() {		// use when password is empty
    	StringBuilder sb = new StringBuilder();
    	int numDigits = RANDOM.nextInt(4) + 1; // 1 to 4 random digits
        for (int i = 0; i < numDigits; i++) {
            char ch = Character.forDigit(RANDOM.nextInt(10), 10);
            sb.append(ch);
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
        int numSymbols = RANDOM.nextInt(3) + 1; // 1 to 3 random symbols
        for (int i = 0; i < numSymbols; i++) {
            int index = RANDOM.nextInt(password.length());
            char ch = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
            sb.insert(index, ch);
        }
        return sb.toString();
    }

    private static String shuffleString(String input) {
        char[] array = input.toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
        	int j = RANDOM.nextInt(i + 1);
        	char temp = array[i];
        	array[i] = array[j];
        	array[j] = temp;
        }
        return new String(array);
    }
    
    private static List<String> findWords(int length) {
    	String filePath = "C:\\Users\\garde\\eclipse-workspace\\Mess around\\src\\dictionary.txt";
        String desiredLetters = JOptionPane.showInputDialog(null, "Please enter some sample letters to use", "Letter selection");
        
        if(desiredLetters == null) {
        	JOptionPane.showMessageDialog(null, "Program exited", "Goodbye!", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
        }
        
        for(int i = 0; i < desiredLetters.length(); i++) {
        	if(Character.isDigit(desiredLetters.charAt(i))) {
        		JOptionPane.showMessageDialog(null, "Error: Digits are not allowed", "Error", JOptionPane.INFORMATION_MESSAGE);
    			System.exit(0);
        	}
        }
        
        List<String> matchingWords = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Search for words in each line
            	matchingWords.addAll(findWordsWithExactLetters(line, desiredLetters));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> selectedWords = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
        	int index = RANDOM.nextInt(matchingWords.size());
        	selectedWords.add(matchingWords.get(index));
        }
        return selectedWords;
        
    }		// END findWords
    
    private static List<String> findWordsWithExactLetters(String line, String desiredLetters) {
        String[] words = line.split(" ");
        List<String> matchingWords = new ArrayList<>();
        
        for (String word : words) {
            if (containsExactLetters(word.toLowerCase(), desiredLetters.toLowerCase())) {
                matchingWords.add(word);
            }
        }
        
        return matchingWords;
    }	// END
    
    private static boolean containsExactLetters(String word, String desiredLetters) {
        
        for (char letter : word.toCharArray()) {
            if (desiredLetters.indexOf(letter) == -1) {
                return false;
            }
        }
        return true;
    }	// END
    
    private static String checkPassword(String password, int wordLength) {
    	StringBuilder sb = new StringBuilder(password);
    	if(password.length() > wordLength) {
        	while(sb.length() > wordLength) {
        		sb = sb.deleteCharAt(sb.length() - 1);
        	}
        	return sb.toString();
    	} else if(password.length() < wordLength) {
    		while(password.length() < wordLength) {
    			password = addRandomNumbers(password);
    			password = checkPassword(password, wordLength);
    		}
    		return password;
    	} else {
    		return password;
    	}

    }	// END
    
}	// END MAIN
