import java.util.*;

class PlayfairCipher {
    private static char[][] charMatrix;
    private static int [][] alphabetPoints;
    private static String punctuation = ",./'|;:<>()@#$%^&*!?~-+_=";
    private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static char[] outText;
    private static int colOne, colTwo, rowOne, rowTwo;

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

    public static String formatTextForMatrix(String s) {
        s = s.toUpperCase().replaceAll("[^A-Z]", "");
        return s.replace("J", "I").replace("Q", "");
    }

    public static void createTable(String key) {
        charMatrix = new char[5][5]; // 5 x 5 Matrix creation for the table
        alphabetPoints = new int [26][2]; // Represnt the 26 different letters of the English Alphabet

        // Prepares the text to be used for the encryption
        String preMatrixString = formatTextForMatrix(key + alphabet);

        int matrixPos = 0;

        for (int i = 0; i < preMatrixString.length(); i++) {
            char c = preMatrixString.charAt(i);
            int alphabetIndex = alphabet.indexOf(c); // Gets the index of the letter in the alphabet

            if (alphabetPoints[alphabetIndex][1] == 0  && alphabetPoints[alphabetIndex][0] == 0) { // Checks to see if the letter has been used already or not
                charMatrix[matrixPos / 5][matrixPos % 5] = c; // Sets the position in the 2D array to be used for the matrix
                /*
                    Allows for the coordinates for the specific point in the key matrix to be saved at the particular
                    index of the alphabet corresponding to the character.
                */
                alphabetPoints[alphabetIndex][0] = matrixPos % 5;
                alphabetPoints[alphabetIndex][1] = matrixPos / 5;
                //System.out.println(alphabetPoints[alphabetIndex][1]);
                matrixPos++;
            }
        }
        // for(char[] arr : charMatrix){
        //   System.out.println(Arrays.toString(arr));
        // }
        // for(int[] arr : alphabetPoints){
        //   System.out.println(Arrays.toString(arr));
        // }
    }


    /* Responsible for encoding the string and making sure that the encodePlainTextd text
    will be properly formatted when when passed to the codec to finish encoding
    the plaintext message */
    public static String encodePlainText(String plainText) {
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
            char a = encodedMessage.charAt(i);
            char b = encodedMessage.charAt(i + 1);

            // Used to get the numeric index of the character in the alphabet
          //  int colOne = 0, colTwo = 0, rowOne = 0, rowTwo = 0;
            for (int j = 0; j < alphabetPoints.length; j++){
              if (alphabet.indexOf(a) == j){
                colOne = alphabetPoints[j][0];
                rowOne = alphabetPoints[j][1];
              }
              if (alphabet.indexOf(b) == j){
                colTwo = alphabetPoints[j][0];
                rowTwo = alphabetPoints[j][1];
              }
            }

            // shiftSpotAmounts the columns to the right if the rows are equivalent
            if (rowOne == rowTwo) {
                colOne = (colOne + 1) % 5;
                colTwo = (colTwo + 1) % 5;

            // Shifts the rows down if the columns are equivalent
            } else if (colOne == colTwo) {
                rowOne = (rowOne + 1) % 5;
                rowTwo = (rowTwo + 1) % 5;
            // If neither condition is met then a swap is done
            } else {
              int temp = colOne;
              colOne = colTwo;
              colTwo = temp;
            }
            // Gets the character from the table and sets it to the appropriate position
            encodedMessage.setCharAt(i, charMatrix[rowOne][colOne]); // First
            encodedMessage.setCharAt(i + 1, charMatrix[rowTwo][colTwo]); // Second
        }
        /*
          Inserts the punctuation back into the string at the index
	  it belongs in.
        */
	    int i = 0;
        for(char c: outText){
            if(c != 0){
                encodedMessage.insert(i, c);
            }
	        i++;
         }
        return encodedMessage.toString();
    }

    //decodeCipherTexts the string using the codec method defined later
    public static String decodeCipherText(String cipherText) {

        StringBuilder decodedMessage = new StringBuilder(cipherText);
        for (int i = 0; i < decodedMessage.length(); i += 2) {
            // If the length of the cipher text is odd it will append an 'X' to the end
            if (i == decodedMessage.length() - 1){
                if(decodedMessage.length() % 2 == 1){
                    decodedMessage.append('X');
                }
                else{
                    decodedMessage.append("");
                }
            }

            // Inserts an 'X' wherever there is a set of duplicate letter like 'SS'
            else if (decodedMessage.charAt(i) == decodedMessage.charAt(i + 1))
                decodedMessage.insert(i + 1, 'X');
        }

        // return playfairCodec(encodedMessage, 1);
        for (int i = 0; i < decodedMessage.length(); i += 2) {
            char a = decodedMessage.charAt(i);
            char b = decodedMessage.charAt(i + 1);

            System.out.println(alphabet.indexOf(b));

            // Used to get the numeric index of the character in the alphabet
            for (int j = 0; j < alphabetPoints.length; j++){
              if (alphabet.indexOf(a) == j){
                colOne = alphabetPoints[j][0];
                rowOne = alphabetPoints[j][1];
              }
              if (alphabet.indexOf(b) == j){
                colTwo = alphabetPoints[j][0];
                rowTwo = alphabetPoints[j][1];
              }
            }

            // shiftSpotAmounts the columns to the right if the rows are equivalent
            if (rowOne == rowTwo) {
                colOne = (colOne + 4) % 5;
                colTwo = (colTwo + 4) % 5;

            // Shifts the rows down if the columns are equivalent
            } else if (colOne == colTwo) {
                rowOne = (rowOne + 4) % 5;
                rowTwo = (rowTwo + 4) % 5;
            // If neither condition is met then a swap is done
            } else {
              int temp = colOne;
              colOne = colTwo;
              colTwo = temp;
            }
            // Gets the character from the table and sets it to the appropriate position
            decodedMessage.setCharAt(i, charMatrix[rowOne][colOne]); // First
            decodedMessage.setCharAt(i + 1, charMatrix[rowTwo][colTwo]); // Second
        }
	      int i = 0;
        for(char c: outText){
            if(c != 0){
                decodedMessage.insert(i, c);
            }
	        i++;
        }
        decodedMessage.replaceAll("X", "");
        return decodedMessage.toString();
    }


}
