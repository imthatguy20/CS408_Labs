import java.util.*;
import java.math.BigInteger;
 
public class hill {
    public static int keyMatrix[][];
    public static int resultMatrix[];
    public static char[][] divedLine;
    public static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final Scanner in = new Scanner(System.in);
    public static String line, key;
    public static double squareRoot;
 
    // Main method of the program
    public static void main(String args[]) {
        try{
            switch(args[0]){
                case "-e": // Recognizes flag for encryption
                System.out.println("\nText to be encrypted: ");
                line = in.nextLine().toUpperCase();
                line = line.replaceAll("[^A-Z]", "");
                System.out.println("\nKey for encryption: ");
                key = in.nextLine().toUpperCase();
                key = key.replaceAll("[^A-Z]", "");
                squareRoot = Math.sqrt(key.length());
                codec(line, key, squareRoot);
                break;
            case "-d": // Recognizes flag for decryption
                System.out.println("\nText to be decrypted: ");
                line = in.nextLine().toUpperCase();
                line = line.replaceAll("[^A-Z]", "");
                System.out.println("\nKey for decryption: ");
                key = in.nextLine().toUpperCase();
                key = key.replaceAll("[^A-Z]", "");
                squareRoot = Math.sqrt(key.length());
                codec(line, key, squareRoot);
                break;
            default:
                System.out.println("\nUSAGE: java hill.java [-e] [-d]\n -e Encryption\n -d Decryption");
                break;
            }
        }
        catch (ArrayIndexOutOfBoundsException e){ // Catches exception where no flag is given
            System.out.println("\nUSAGE: java hill.java [-e] [-d]\n -e Encryption\n -d Decryption");
        }
    }
    // Runs all of the necesary methods for encryption and decryption
    public static void codec(String line, String key, double squareRoot){
        if (squareRoot != (long) squareRoot)
            System.out.println("\nError: Invalid key length.  Does not form a square matrix!");
        else {
            int s = (int) squareRoot;
            while(line.length() % s != 0) { // If the plaintext can not be dived evenly, add X until it can
                line += 'X';
            }
            divedLine = new char[line.length() / s][s]; // Creates the appropriately sized matrix depending on the key
            int characterIndex = 0;
            for(int i = 0; i < (line.length() / s); i++){ // Adds letters to the matrix
                for(int j = 0; j < s; j++){
                    divedLine[i][j] = line.charAt(characterIndex);
                    characterIndex++;
                }
            }
            if (invertableCheck(key, s)) { // Makes sure that the key is invertable.  If not, end the program
                System.out.println("\nResulting Text:");
                for(char[] row : divedLine){ 
                    multiplyLineMatrixKeyMatrix(s, row); // Multiply key and plaintext
                }
                calculateInverseMatrix(keyMatrix, s); // Get the inverse key for decryption
            }
        }
    }
    // Multiplies the key matrix and the specific row matrix for a chunk of text
    public static void multiplyLineMatrixKeyMatrix(int length, char[] row)
    {
        resultMatrix = new int[length];
        int keyMatrixIndex, lineMatrixIndex;
        String res = "";
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                keyMatrixIndex = keyMatrix[j][i]; 
                lineMatrixIndex = alphabet.indexOf(row[j]);
                resultMatrix[i] += keyMatrixIndex * lineMatrixIndex; // Values are calculated and added together
                resultMatrix[i] %= 26; // Mod 26 needs to be used to make sure that the values correspond to an alphabetic index
            }
            char letterAtIndex = (char) (resultMatrix[i] + 65);
            res += letterAtIndex;
        }
        System.out.print(res);
    }
    /*
    This method will check to make sure that the key can be used 
    for the key matrix.  If the key is not invertable then it can not 
    be used.  This is required since the key must be inverted for 
    decryption.
    */
    public static boolean invertableCheck(String key, int length)
    {
        keyMatrix = new int[length][length];
        int c = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int letterIndex = alphabet.indexOf(key.charAt(c));
                keyMatrix[j][i] = letterIndex;
                c++;
            }
        }
        int determinant = calculateDeterminant(keyMatrix, length); 
        int trueOrFalse;
        boolean boolVal = false;
        determinant = determinant % 26;
        trueOrFalse = determinant == 0 ? 0 : 
            (determinant % 2 == 0 || determinant % 13 == 0) ? 0 : 1; // If the key has a common factors with 26 it can not be used.
        switch(trueOrFalse){
            case 0: // If the determinant is zero the key can not be used
                System.out.println("Error: Key can not be used!");
                break;
            case 1:
                boolVal = true;
                break;
        }
        return boolVal;
    }
    /* 
    Calculates the determinant of the inputted matrix. 
    This will be useful for different applications throughout the code when determining 
    if there will be an inverse and then for finding the inverse of key matrix.
    */
    public static int calculateDeterminant(int matrix[][], int size)
    {
        int res = 0, topRowVal, subDeterminant;
        double multiplier = 0;
        switch(size){
            case 1: 
                res = matrix[0][0];
                break;
            case 2: // Case for a 2x2 matrix or the minor matrix of a 3x3 matrix
                int multiplyAD = matrix[0][0] * matrix[1][1];
                int multiplyBC = matrix[1][0] * matrix[0][1];
                res = multiplyAD - multiplyBC;
                break;
            default: // Default ops for a 3x3 matrix which includes finding and running ops on minor matrices
                for (int matrixCol = 0; matrixCol < size; matrixCol++) {
                    int minorMatrix[][] = new int[size-1][size-1]; // Size will be one smaller for minors
                    for (int i = 1; i < size; i++) { // Finds each individual minor matrix
                        int minorCol = 0;
                        for (int j = 0; j < size; j++){
                            if (j == matrixCol)
                                continue;
                            int inputMatIndex = matrix[i][j];
                            minorMatrix[i-1][minorCol] = inputMatIndex;
                            minorCol++;
                        }
                    }
                    multiplier = Math.pow(-1, matrixCol + 2); // Based on location in the equation will either be -1 or 1 for correct signing
                    topRowVal = matrix[0][matrixCol]; 
                    subDeterminant = calculateDeterminant(minorMatrix, size - 1);
                    res += multiplier * topRowVal * subDeterminant; // Ops to find the determinant of the 3x3 matrix
                }
                break;
        }
        return res;
    }

     // Calculates the inverse of the key matrix for decryption
    public static void calculateInverseMatrix(int inputMatrix[][], int size)
    {
        int res[][] = new int[size][size]; // Matrix to store first portion end result values after all ops
        int inverseMatrix[][] = new int[size][size]; // Stores inverse matrix
        int preResultMat[][] = new int[size][size]; // Stores the matrix before all inverse ops are done
        int resultRow, resultCol, multiplier, subDeterminant;
        String invkey = ""; // String to store the full inverted key
        /*
        First set of loops gets the matrix of minors row and column where the lines meet.
        This will determine what values in the matrix make up a specific minor matrix. From 
        there the determinate of the minor matrix is calculated and fills the index with that
        value in the new matrix.
        */
        for (int matOfMinorsRow = 0; matOfMinorsRow < size; matOfMinorsRow++) { // This first set of loops sets the "dividing lines"
            for (int matOfMinorsCol = 0; matOfMinorsCol < size; matOfMinorsCol++) {
                resultRow = 0;
                resultCol = 0;
                // Second set of loops here gets the minor matrix 
                for (int minorMatRow = 0; minorMatRow < size; minorMatRow++) {
                    for (int minorMatCol = 0; minorMatCol < size; minorMatCol++) {
                        preResultMat[minorMatRow][minorMatCol] = 0;
                        if (minorMatRow != matOfMinorsRow && minorMatCol != matOfMinorsCol) { // Makes sure next set of minor matrix values do not fall on "dividing lines"
                            int inputMatIndex = inputMatrix[minorMatRow][minorMatCol];
                            preResultMat[resultRow][resultCol] = inputMatIndex;
                            if (resultCol < (size - 2)){ 
                                resultCol++; 
                            } else {
                                resultCol = 0;
                                resultRow++;
                            }
                        }
                    }
                }
                multiplier = (int) Math.pow(-1, matOfMinorsCol + matOfMinorsRow); // Determines if the index in the array should be a possitive or negative value
                subDeterminant = calculateDeterminant(preResultMat, size - 1); // Calculates the determinant of the subarray matrix of minors
                res[matOfMinorsCol][matOfMinorsRow] = multiplier * subDeterminant; // Assigns results
            }
        }
        int multInvDet = calculateMultInverse(calculateDeterminant(keyMatrix, size) % 26);
        multInvDet += (multInvDet < 0) ? 26 : 0;
        for (int row = 0; row < size; row++) { // Calculates the actual inverse of the matrix 
            for (int col = 0; col < size; col++) {
                inverseMatrix[row][col] = (res[row][col] * multInvDet) % 26;
                inverseMatrix[row][col] += (inverseMatrix[row][col] < 0) ? 26 : 0;
                inverseMatrix[row][col] %= 26;
            }
        }
        for (int i = 0; i < size; i++) { // Gets the char that corresponds with the specific index
            for (int j = 0; j < size; j++) {
                char letterIndex = alphabet.charAt(inverseMatrix[j][i]);
                invkey += letterIndex;
            }
        }
        System.out.println("\n\nInverse key:");
        System.out.print(invkey+"\n");
    }
    /* 
    Method calculates the multiplicative inverse for the matrix.
    This is crucial when finding the modular inverse of a matrix
    to be used for decryption.
     */
    public static int calculateMultInverse(int determinant) {
        BigInteger one = BigInteger.valueOf(determinant);
        BigInteger two = BigInteger.valueOf(26);
        BigInteger multiplicativeInverse = one.modInverse(two); // Calculates the multiplicative inverse
        return multiplicativeInverse.intValue();
    }
}
