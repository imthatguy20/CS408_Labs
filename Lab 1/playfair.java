import java.awt.Point;
import java.util.*;

class PlayfairCipher {
    private static char[][] charMatrix;
    private static Point[] alphabetCoordinates;
    private static String punctuation = ",./'|;:<>()@#$%^&*!?~-+_=";
    private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static char[] outText;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Switch statement that will allow the user to either encodePlainText or decodeCipherText
        switch (args[0]){
          case "-e":
            String keyEnc, txtEnc;
            do{
              System.out.println("Enter the key to use for encryption: ");
              keyEnc = sc.nextLine().trim();
            } while (keyEnc.length() < 1 || keyEnc.length() > 10);
            do {
              System.out.println("Enter the text to be encrypted: ");
              txtEnc = sc.nextLine().trim();
            } while (txtEnc.length() < 1);
            txtEnc = txtEnc.replaceAll("\\s", ""); // Trims the whitespace from the text
            outText = new char[txtEnc.length()+1]; // Sets the output text to be the size of the original text
            /* Checks to see if the text contains any punctuation
               and if it does, moves it to the output text for both the
               encodePlainTextd text and decodeCipherTextd text. */
            for(char c: txtEnc.toCharArray()){
              if(punctuation.indexOf(c) != -1){
                outText[txtEnc.indexOf(c)] = c;
              }
            }
            createTable(keyEnc); // Creates the table to be used for encryption
            String enc = encodePlainText(createPreMatrixString(txtEnc)); // encodePlainTexts the message
            System.out.printf("%nencoded message: %n%s%n", enc);
            break;
          case "-d":
            String keyDec, txtDec;
            do{
              System.out.println("Enter the key to used for encryption: ");
              keyDec = sc.nextLine().trim();
            } while (keyDec.length() < 1 || keyDec.length() > 10);
            do {
              System.out.println("Enter the text to be decrypted: ");
              txtDec = sc.nextLine().trim();
            } while (txtDec.length() < 1);
            outText = new char[txtDec.length()]; // Sets the output text to be the size of the original text
            // Same function as when decoding but we don't need to trim the string
            for(char c: txtDec.toCharArray()){
              if(punctuation.indexOf(c) != -1){
                outText[txtDec.indexOf(c)] = c;
              }
            }
            createTable(keyDec);
            txtDec = txtDec.replaceAll("\\p{P}", ""); // Removes all non-alphanumeric characters
            System.out.printf("%ndecodeCipherTextd message: %n%s%n", decodeCipherText(txtDec));
            break;
          default:
            throw new IllegalArgumentException("Error:  Not a valid argument");
        }
    }
    private static String createPreMatrixString(String s) {
        s = s.toUpperCase().replaceAll("[^A-Z]", "");
        return s.replace("J", "I");
    }
    private static void createTable(String key) {
        charMatrix = new char[5][5]; // 5 x 5 Matrix creation for the table
        alphabetCoordinates = new Point[26]; // Represnt the 26 different letters of the English Alphabet       
        key += alphabet; // Prepares the text to be used for the encryption
        String preMatrixString = createPreMatrixString(key);
        int matrixPos = 0; 
        for (int i = 0; i < preMatrixString.length(); i++) {
            if (alphabetCoordinates[alphabet.indexOf(preMatrixString.charAt(i))] == null) { // Checks to see if the letter has been used already or not
                int row = matrixPos / 5, col = matrixPos % 5;
                charMatrix[row][col] = preMatrixString.charAt(i); // Sets the position in the 2D array to be used for the matrix
                /*  Allows for the coordinates for the specific point in the key matrix to be saved at the particular 
                    index of the alphabet corresponding to the character.  */
                alphabetCoordinates[alphabet.indexOf(preMatrixString.charAt(i))] = new Point(col, row); 
                matrixPos++;
            }
        }
    }
    /* Responsible for encoding the string and making sure that the encodePlainTextd text
    will be properly formatted when when passed to the codec to finish encoding
    the plaintext message */
    private static String encodePlainText(String plainText) {
        StringBuilder encodedMessage = new StringBuilder(plainText); // Consists of the pre-formatted text made earlier
        for (int i = 0; i < encodedMessage.length(); i += 2) {
            // If the length of the cipher text is odd it will append an 'X' to the end
            if (i == encodedMessage.length() - 1){
                if(encodedMessage.length() % 2 == 1){
                    encodedMessage.append('X'); 
                } 
                else{
                    encodedMessage.append("");
                }
            }
            // Inserts an 'X' wherever there is a set of duplicate letter like 'SS'
            else if (encodedMessage.charAt(i) == encodedMessage.charAt(i + 1))
                encodedMessage.insert(i + 1, 'X');
        }
        // return playfairCodec(encodedMessage, 1);
        for (int i = 0; i < encodedMessage.length(); i += 2) {
            // First character in the pair by checking it against the alphabet string and encoded message location
            int firstCharY = alphabetCoordinates[alphabet.indexOf(encodedMessage.charAt(i))].y;
            int firstCharX = alphabetCoordinates[alphabet.indexOf(encodedMessage.charAt(i))].x;
            // Second character in the pair by checking it against the alphabet string and encoded message location
            int secondCharY = alphabetCoordinates[alphabet.indexOf(encodedMessage.charAt(i + 1))].y;
            int secondCharX = alphabetCoordinates[alphabet.indexOf(encodedMessage.charAt(i + 1))].x;
            // shiftSpotAmounts the columns to the right if the rows are equivalent
            if (firstCharY == secondCharY) {
                firstCharX += 1;
                firstCharX = firstCharX % 5;
                secondCharX += 1;
                secondCharX = secondCharX % 5;
            // shiftSpotAmountAmounts the rows down if the columns are equivalent
            } else if (firstCharX == secondCharX) {
                firstCharY += 1;
                firstCharY = firstCharY % 5;
                secondCharY += 1;
                secondCharY = secondCharY % 5;
            // If neither condition is met then a swap is done
            } else {
                int tmp = firstCharX;
                firstCharX = secondCharX;
                secondCharX = tmp;
            }
            // Gets the character from the table and sets it to the appropriate position
            encodedMessage.setCharAt(i, charMatrix[firstCharY][firstCharX]); // First
            encodedMessage.setCharAt(i + 1, charMatrix[secondCharY][secondCharX]); // Second
        }
        // Inserts the punctuation back into the string at the index it belongs in.
	    int i = 0;
        for(char c: outText){
            if(c != 0){
                encodedMessage.insert(i, c);
            }
	        i++;
        }
        return encodedMessage.toString();
    }
    // Decodes the string using the codec method defined later
    private static String decodeCipherText(String cipherText) {
        StringBuilder decodedMessage = new StringBuilder(cipherText);
        for (int i = 0; i < decodedMessage.length(); i += 2) {
            // First character in the pair by checking it against the alphabet string and encoded message location
            int firstCharY = alphabetCoordinates[alphabet.indexOf(decodedMessage.charAt(i))].y;
            int firstCharX = alphabetCoordinates[alphabet.indexOf(decodedMessage.charAt(i))].x;
            // Second character in the pair by checking it against the alphabet string and encoded message location
            int secondCharY = alphabetCoordinates[alphabet.indexOf(decodedMessage.charAt(i + 1))].y;
            int secondCharX = alphabetCoordinates[alphabet.indexOf(decodedMessage.charAt(i + 1))].x;
            // Shifts the columns to the right if the rows are equivalent
            if (firstCharY == secondCharY) {
                firstCharX += 4;
                firstCharX = firstCharX % 5;
                secondCharX += 4;
                secondCharX = secondCharX % 5;
            // Shifts the rows down if the columns are equivalent
            } else if (firstCharX == secondCharX) {
                firstCharY += 4;
                firstCharY = firstCharY % 5;
                secondCharY += 4;
                secondCharY = secondCharY % 5;
            // If neither condition is met then a swap is done
            } else {
                int tmp = firstCharX;
                firstCharX = secondCharX;
                secondCharX = tmp;
            }
            // Gets the character from the table and sets it to the appropriate position
            char firstLetter = charMatrix[firstCharY][firstCharX];
            char secodLetter = charMatrix[secondCharY][secondCharX];
            decodedMessage.setCharAt(i, firstLetter); // First
            decodedMessage.setCharAt(i + 1, secodLetter); // Second
        }
        // Inserts the punctuation back into the string at the index it belongs in.
	    int i = 0;
        for(char c: outText){
            if(c != 0){
                decodedMessage.insert(i, c);
            }
	        i++;
        }
        return decodedMessage.toString();
    }
}
