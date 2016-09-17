import java.awt.Point;
import java.util.*;

class PlayfairCipher {
    private static char[][] charMatrix;
    private static Point[] xyPoints;
    private static String punctuation = ",./'|;:<>()@#$%^&*!?~-+_=";
    private static char[] outText;
    private static boolean qRemoved = false;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Switch statement that will allow the user to either encodePlainText or decodeCipherText
        switch (args[0]){
          case "-e":
            String keyEnc = prompt("Enter an encryption key (min length 1, max length 10): ", sc, 1, 10); // Key to be used
            String txtEnc = prompt("Enter the message: ", sc, 1, 1000); // PT to be encrypted
            txtEnc = txtEnc.replaceAll("\\s", ""); // Trims the whitespace from the text
            outText = new char[txtEnc.length()]; // Sets the output text to be the size of the original text
            /*
              Checks to see if the text contains any punctuation
              and if it does, moves it to the output text for both the
              encodePlainTextd text and decodeCipherTextd text.
            */
            for(char c: txtEnc.toCharArray()){
              if(punctuation.indexOf(c) != -1){
                outText[txtEnc.indexOf(c)] = c;
              }
            }
            System.out.println(Arrays.toString(outText));
            createTable(keyEnc); // Creates the table to be used for encryption
            String enc = encodePlainText(formatTextForMatrix(txtEnc)); // encodePlainTexts the message
            System.out.printf("%nencoded message: %n%s%n", enc);
            break;
          case "-d":
            String keyDec = prompt("Enter the key used for encoding: ", sc, 1, 10);
            String txtDec = prompt("Enter the encoded message: ", sc, 1, 1000);
            outText = new char[txtDec.length()]; // Sets the output text to be the size of the original text
            // Same function as when decoding but we don't need to trim the string
            for(char c: txtDec.toCharArray()){
              if(punctuation.indexOf(c) != -1){
                outText[txtDec.indexOf(c)] = c;
              }
            }
            System.out.println(Arrays.toString(outText));
            createTable(keyDec);
            txtDec = txtDec.replaceAll("\\p{P}", ""); // Removes all non-alphanumeric characters
            System.out.printf("%ndecodeCipherTextd message: %n%s%n", decodeCipherText(txtDec));
            break;
          default:
            throw new IllegalArgumentException("Error:  Not a valid argument");
        }
    }

    private static String prompt(String promptText, Scanner sc, int minLen, int maxLen) {
        String s;
        do {
            System.out.print(promptText);
            s = sc.nextLine().trim();
        } while (s.length() < minLen || s.length() > maxLen);
        return s;
    }

    private static String formatTextForMatrix(String s) {
        s = s.toUpperCase().replaceAll("[^A-Z]", "");
        return s.replace("J", "I").replace("Q", "");
    }

    private static void createTable(String key) {
        charMatrix = new char[5][5]; // 5 x 5 Matrix creation for the table
        xyPoints = new Point[26]; // Represnt the 26 different letters of the English Alphabet

        // Prepares the text to be used for the encryption
        String s = formatTextForMatrix(key + "ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        for (int i = 0, k = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (xyPoints[c - 'A'] == null) {
                charMatrix[k / 5][k % 5] = c; // Sets the position in the 2D array to be used for the matrix
                xyPoints[c - 'A'] = new Point(k % 5, k / 5);
                k++;
            }
        }
    }

    /* Responsible for encoding the string and making sure that the encodePlainTextd text
    will be properly formatted when when passed to the codec to finish encoding
    the plaintext message */
    private static String encodePlainText(String s) {
        StringBuilder encodedMessage = new StringBuilder(s); // Consists of the pre-formatted text made earlier

        for (int i = 0; i < encodedMessage.length(); i += 2) {
            // If the length of the cipher text is odd it will append an 'X' to the end
            if (i == encodedMessage.length() - 1)
                encodedMessage.append(encodedMessage.length() % 2 == 1 ? 'X' : "");

            // Inserts an 'X' wherever there is a set of duplicate letter like 'SS'
            else if (encodedMessage.charAt(i) == encodedMessage.charAt(i + 1))
                encodedMessage.insert(i + 1, 'X');
        }
        return playfairCodec(encodedMessage, 1);
    }

    // decodeCipherTexts the string using the codec method defined later
    private static String decodeCipherText(String cipherText) {
        return playfairCodec(new StringBuilder(cipherText), 4);
    }

    /* Codec will be used for both the encryption and decryption
    the direction denotes the way that the shift will move. */
    private static String playfairCodec(StringBuilder temp, int direction) {
        int len = temp.length();
        for (int i = 0; i < len; i += 2) {
            char a = temp.charAt(i);
            char b = temp.charAt(i + 1);

            int rowOne = xyPoints[a - 'A'].y;
            int rowTwo = xyPoints[b - 'A'].y;
            int colOne = xyPoints[a - 'A'].x;
            int colTwo = xyPoints[b - 'A'].x;

            // Shifts the columns to the right if the rows are equivalent
            if (rowOne == rowTwo) {
                colOne = (colOne + direction) % 5;
                colTwo = (colTwo + direction) % 5;

            // Shifts the rows down if the columns are equivalent
            } else if (colOne == colTwo) {
                rowOne = (rowOne + direction) % 5;
                rowTwo = (rowTwo + direction) % 5;

            } else {
                int tmp = colOne;
                colOne = colTwo;
                colTwo = tmp;
            }
            // Gets the character from the table and sets it to the appropriate position
            temp.setCharAt(i, charMatrix[rowOne][colOne]); // First
            temp.setCharAt(i + 1, charMatrix[rowTwo][colTwo]); // Second
        }
        /*
          Inserts the punctuation back into the string at the index
	  it belongs in.
        */
	int i = 0;
        for(char c: outText){
            if(c != 0){
                temp.insert(i, c);
            }
	    i++;
        }
        return temp.toString();
    }
}
