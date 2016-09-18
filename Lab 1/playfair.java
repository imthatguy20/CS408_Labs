import java.awt.Point;
import java.util.*;

class PlayfairCipher {
    private static char[][] charMatrix;
    private static Point[] alphabetPoints;
    private static String punctuation = ",./'|;:<>()@#$%^&*!?~-+_=";
    private static char[] outText;
    private static boolean qRemoved = false;

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
            createTable(keyEnc); // Creates the table to be used for encryption
            String enc = encodePlainText(formatTextForMatrix(txtEnc)); // encodePlainTexts the message
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

    private static String formatTextForMatrix(String s) {
        s = s.toUpperCase().replaceAll("[^A-Z]", "");
        return s.replace("J", "I").replace("Q", "");
    }

    private static void createTable(String key) {
        charMatrix = new char[5][5]; // 5 x 5 Matrix creation for the table
        alphabetPoints = new Point[26]; // Represnt the 26 different letters of the English Alphabet

        // Prepares the text to be used for the encryption
        String preMatrixString = formatTextForMatrix(key + "ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        int matrixPos = 0; 
        
        for (int i = 0; i < preMatrixString.length(); i++) {
            char c = preMatrixString.charAt(i);
            int alphabetIndex = ((int) c) - 65; // Gets the index of the letter in the alphabet
            if (alphabetPoints[alphabetIndex] == null) { // Checks to see if the letter has been used already or not
                charMatrix[matrixPos / 5][matrixPos % 5] = c; // Sets the position in the 2D array to be used for the matrix
                /*
                    Allows for the coordinates for the specific point in the key matrix to be saved at the particular 
                    index of the alphabet corresponding to the character.  
                */
                alphabetPoints[alphabetIndex] = new Point(matrixPos % 5, matrixPos / 5); 
                matrixPos++;
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

            // Used to get the numeric index of the character in the alphabet
            int aAlphabetIndex = ((int) a) - 65;
            int bAlphabetIndex = ((int) b) - 65;

            // First character in the pair
            int rowOne = alphabetPoints[aAlphabetIndex].y;
            int colOne = alphabetPoints[aAlphabetIndex].x;

            // Second character in the pair
            int rowTwo = alphabetPoints[bAlphabetIndex].y;
            int colTwo = alphabetPoints[bAlphabetIndex].x;

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
