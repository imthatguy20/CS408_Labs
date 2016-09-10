import java.awt.Point;
import java.util.*;

class PlayfairCipher {
    private static char[][] charTable;
    private static Point[] positions;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Switch statement that will allow the user to either encode or decode
        switch (args[0]){
          case "-e":
            String key = prompt("Enter an encryption key (min length 1, max length 10): ", sc, 1); // Key to be used
            String txt = prompt("Enter the message: ", sc, 1); // PT to be encrypted
            String jti = prompt("Replace J with I? y/n: ", sc, 1); // Prompt to change 'i' to 'j' as would be normal
            boolean changeJtoI = jti.equalsIgnoreCase("y");
            createTable(key, changeJtoI); // Creates the table to be used for encryption
            String enc = encode(prepareText(txt, changeJtoI)); // Encodes the message
            System.out.printf("%nEncoded message: %n%s%n", enc);
            break;
          case "-d":
            System.out.println("decode");
            String keyDec = prompt("Enter the key used for encoding: ", sc, 1);
            String txtDec = prompt("Enter the encoded message: ", sc, 1);
            String jtiDec = prompt("Was J replaced with I? y/n: ", sc, 1);
            boolean changeJtoIDec = jtiDec.equalsIgnoreCase("y");
            createTable(keyDec, changeJtoIDec);
            System.out.printf("%nDecoded message: %n%s%n", decode(txtDec));
            break;
          default:
            throw new IllegalArgumentException("Error:  Not a valid argument");
        }
    }

    private static String prompt(String promptText, Scanner sc, int minLen) {
        String s;
        do {
            System.out.print(promptText);
            s = sc.nextLine().trim();
        } while (s.length() < minLen);
        return s;
    }

    private static String prepareText(String s, boolean changeJtoI) {
        s = s.toUpperCase().replaceAll("[^A-Z]", "");
        return changeJtoI ? s.replace("J", "I") : s.replace("Q", "");
    }

    private static void createTable(String key, boolean changeJtoI) {
        charTable = new char[5][5]; // 5 x 5 Matrix creation for the table
        positions = new Point[26]; // Represnt the 26 different letters of the English Alphabet

        // Prepares the text to be used for the encryption
        String s = prepareText(key + "ABCDEFGHIJKLMNOPQRSTUVWXYZ", changeJtoI);

        String key = prompt("Enter an encryption key (min length 1): ", sc, 1);
        String txt = prompt("Enter the message: ", sc, 1);
        String jti = prompt("Replace J with I? y/n: ", sc, 1);

        boolean changeJtoI = jti.equalsIgnoreCase("y");

        createTable(key, changeJtoI);

        String enc = encode(prepareText(txt, changeJtoI));

        System.out.printf("%nEncoded message: %n%s%n", enc);
        System.out.printf("%nDecoded message: %n%s%n", decode(enc));
    }

    private static String prompt(String promptText, Scanner sc, int minLen) {
        String s;
        do {
            System.out.print(promptText);
            s = sc.nextLine().trim();
        } while (s.length() < minLen);
        return s;
    }

    private static String prepareText(String s, boolean changeJtoI) {
        s = s.toUpperCase().replaceAll("[^A-Z]", "");
        return changeJtoI ? s.replace("J", "I") : s.replace("Q", "");
    }

    private static void createTable(String key, boolean changeJtoI) {
        charTable = new char[5][5]; // 5 x 5 Matrix creation for the table
        positions = new Point[26]; // Represnt the 26 different letters of the English Alphabet

        // Prepares the text to be used for the encryption
        String s = prepareText(key + "ABCDEFGHIJKLMNOPQRSTUVWXYZ", changeJtoI);

        for (int i = 0, k = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (positions[c - 'A'] == null) {
                charTable[k / 5][k % 5] = c; // Sets the position in the 2D array to be used for the matrix
                positions[c - 'A'] = new Point(k % 5, k / 5);
                positions[c - 'A'] = new Point(k % 5, k / 5);
                k++;
            }
        }
    }

    /* Responsible for encoding the string and making sure that the encoded text
    will be properly formatted when when passed to the codec to finish encoding
    the plaintext message */
    private static String encode(String s) {
        StringBuilder sb = new StringBuilder(s); // Consists of the pre-formatted text made earlier

        for (int i = 0; i < sb.length(); i += 2) {
            // If the length of the cipher text is odd it will append an 'X' to the end
            if (i == sb.length() - 1)
                sb.append(sb.length() % 2 == 1 ? 'X' : "");

            // Inserts an 'X' wherever there is a set of duplicate letter like 'SS'
            else if (sb.charAt(i) == sb.charAt(i + 1))
                sb.insert(i + 1, 'X');
        }
        return codec(sb, 1);
    }

    // Decodes the string using the codec method defined later
    private static String decode(String s) {
        return codec(new StringBuilder(s), 4);
    }

    /* Codec will be used for both the encryption and decryption
    the direc denotes the way that the shift will move. */
    private static String codec(StringBuilder text, int direc) {
        int len = text.length();
        for (int i = 0; i < len; i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);

            int row1 = positions[a - 'A'].y;
            int row2 = positions[b - 'A'].y;
            int colOne = positions[a - 'A'].x;
            int colTwo = positions[b - 'A'].x;

            // Shifts the columns to the right if the rows are equivalent
            if (row1 == row2) {
                colOne = (colOne + direc) % 5;
                colTwo = (colTwo + direc) % 5;

            // Shifts the rows down if the columns are equivalent
            } else if (colOne == colTwo) {
                row1 = (row1 + direc) % 5;
                row2 = (row2 + direc) % 5;

            } else {
                int tmp = colOne;
                colOne = colTwo;
                colTwo = tmp;
            }

            // Gets the character from the table and sets it to the appropriate position
            text.setCharAt(i, charTable[row1][colOne]); // First
            text.setCharAt(i + 1, charTable[row2][colTwo]); // Second
        }
        return text.toString();
    }
}
