import java.util.*;
import java.math.BigInteger;
 
public class hill {
    public static int keymatrix[][];
    public static int linematrix[];
    public static int resultmatrix[];
    public static char[][] divedLine;
    public static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final Scanner in = new Scanner(System.in);
    public static String line, key;
    public static double sq;
 
    // Main method of the program
    public static void main(String args[]) {
        try{
            switch(args[0]){
                case "-e": // Recognizes flag for encryption
                System.out.println("Text to be encrypted: ");
                line = in.nextLine().toUpperCase();
                System.out.println("Key for encryption: ");
                key = in.nextLine().toUpperCase();
                sq = Math.sqrt(key.length());
                if (sq != (long) sq)
                    System.out.println("Error: Invalid key length.  Does not form a square matrix!");
                else {
                    int s = (int) sq;
                    while(line.length() % s != 0){ // Add aditional chars if the length won't work with key
                        line = line + 'X';
                    }
                    divedLine = new char[line.length() / s][s];
                    int characterIndex = 0;
                    for(int i = 0; i < (line.length() / s); i++){
                        for(int j = 0; j < s; j++){
                            divedLine[i][j] = line.charAt(characterIndex);
                            characterIndex++;
                        }
                    }
                    if (invertableCheck(key, s)) {
                        System.out.println("Result:");
                        for(char[] row : divedLine){
                            multiplyLineMatrixKeyMatrix(s, row);
                        }
                        calculateInverseMatrix(keymatrix, s);
                    }
                }
                break;
            case "-d": // Recognizes flag for decryption
                System.out.println("Text to be decrypted: ");
                line = in.nextLine().toUpperCase();
                System.out.println("Key for decryption: ");
                key = in.nextLine().toUpperCase();
                sq = Math.sqrt(key.length());
                if (sq != (long) sq)
                    System.out.println("Error: Invalid key length.  Does not form a square matrix!");
                else {
                    int s = (int) sq;
                    divedLine = new char[line.length() / s][s];
                    int characterIndex = 0;
                    for(int i = 0; i < (line.length() / s); i++){
                        for(int j = 0; j < s; j++){
                            divedLine[i][j] = line.charAt(characterIndex);
                            characterIndex++;
                        }
                    }
                    if (invertableCheck(key, s)) {
                        System.out.println("Result:");
                        for(char[] row : divedLine){
                            multiplyLineMatrixKeyMatrix(s, row);
                        }
                        calculateInverseMatrix(keymatrix, s);
                    }
                }
                break;
            default:
                System.out.println("USAGE: java hill.java [-e] [-d]\n -e Encryption\n -d Decryption");
                break;
            }
        }
        catch (ArrayIndexOutOfBoundsException e){ // Catches exception where no flag is given
            System.out.println("USAGE: java hill.java [-e] [-d]\n -e Encryption\n -d Decryption");
        }
    }
 
    public static void convertKeyToMatrix(String key, int len)
    {
        keymatrix = new int[len][len];
        int c = 0;
        for (int i = 0; i < len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                keymatrix[i][j] = alphabet.indexOf(key.charAt(c));
                c++;
            }
        }
    }
 
    public static void multiplyLineMatrixKeyMatrix(int len, char[] row)
    {
        resultmatrix = new int[len];
        String result = "";
        for (int i = 0; i < len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                resultmatrix[i] += keymatrix[j][i] * alphabet.indexOf(row[j]);
                resultmatrix[i] %= 26;
            }
              result += (char) (resultmatrix[i] + 97);
        }
        System.out.print(result);
    }
 
    public static boolean invertableCheck(String key, int len)
    {
        convertKeyToMatrix(key, len);
        int d = calculateDeterminant(keymatrix, len);
        int trueOrFalse;
        boolean boolVal = false;
        d = d % 26;
        trueOrFalse = d == 0 ? 0 : (d % 2 == 0 || d % 13 == 0) ? 0 : 1;
        switch(trueOrFalse){
            case 0:
                System.out.println("Error: Key can not be used!");
                boolVal = false;
            case 1:
                boolVal = true;
        }
        return boolVal;
    }
 
    public static int calculateDeterminant(int matrix[][], int N)
    {
        int res = 0, topRowVal, subDeterminant;
        double multiplier = 0;
        switch(N){
            case 1:
                res = matrix[0][0];
                break;
            case 2:
                int multiplyAD = matrix[0][0] * matrix[1][1];
                int multiplyBC = matrix[1][0] * matrix[0][1];
                res = multiplyAD - multiplyBC;
                break;
            default:
                for (int j1 = 0; j1 < N; j1++)
                {
                    int m[][] = new int[N - 1][N - 1];
                    for (int i = 1; i < N; i++)
                    {
                        int j2 = 0;
                        for (int j = 0; j < N; j++)
                        {
                            if (j == j1)
                                continue;
                            m[i - 1][j2] = matrix[i][j];
                            j2++;
                        }
                    }
                    multiplier = Math.pow(-1, j1 + 2);
                    topRowVal = matrix[0][j1];
                    subDeterminant = calculateDeterminant(m, N - 1);
                   res += multiplier * topRowVal * subDeterminant;
                }
                break;
        }
        return res;
    }
 
    public static void calculateInverseMatrix(int num[][], int size)
    {
        int b[][] = new int[size][size];
        int fac[][] = new int[size][size];
        int inverseMatrix[][] = new int[size][size];
        int preResultMat[][] = new int[size][size];
        int resultRow, resultCol, multiplier, subDeterminant;
        /*
        First set of loops gets the matrix of minors row and column where the lines meet.
        This will determine what values in the matrix make up a specific minor matrix. From 
        there the determinate of the minor matrix is calculated and fills the index with that
        value in the new matrix.
        */
        for (int matOfMinorsRow = 0; matOfMinorsRow < size; matOfMinorsRow++) {
            for (int matOfMinorsCol = 0; matOfMinorsCol < size; matOfMinorsCol++) {
                resultRow = 0;
                resultCol = 0;
                for (int minorMatRow = 0; minorMatRow < size; minorMatRow++) {
                    for (int minorMatCol = 0; minorMatCol < size; minorMatCol++) {
                        preResultMat[minorMatRow][minorMatCol] = 0;
                        if (minorMatRow != matOfMinorsRow && minorMatCol != matOfMinorsCol) {
                            preResultMat[resultRow][resultCol] = num[minorMatRow][minorMatCol]; 
                            resultRow = (resultCol < (size - 2)) ? resultCol++ : 0; // Increment or set to zero
                            resultCol += (resultCol < (size - 2)) ? 0 : 1; // Add one if the condition is not true 
                        }
                    }
                }
                multiplier = (int) Math.pow(-1, matOfMinorsCol + matOfMinorsRow); // Determines if the index in the array should be a possitive or negative value
                subDeterminant = calculateDeterminant(preResultMat, size - 1); // Calculates the determinant of the subarray matrix of minors
                fac[matOfMinorsCol][matOfMinorsRow] = multiplier * subDeterminant; // Assigns
            }
        }
        int multInvDet = convertToMatrixInverse(calculateDeterminant(keymatrix, size) % 26) % 26;
        multInvDet += (multInvDet < 0) ? 26 : 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                inverseMatrix[row][col] = (fac[row][col] * multInvDet) % 26;
                inverseMatrix[row][col] += (inverseMatrix[row][col] < 0) ? 26 : 0;
                inverseMatrix[row][col] %= 26;
            }
        }
        System.out.println("\nInverse key:");
        matrixConvertToInverseKey(inverseMatrix, size);
    }
 
    public static int convertToMatrixInverse(int d) {
        BigInteger one = BigInteger.valueOf(d);
        BigInteger two = BigInteger.valueOf(26);
        BigInteger multiplicativeInverse = one.modInverse(two);
        return multiplicativeInverse.intValue();
    }
 
    public static void matrixConvertToInverseKey(int inverseMatrix[][], int n) {
        String invkey = "";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                invkey += (char) (inverseMatrix[i][j] + 97);
            }
        }
        System.out.print(invkey+"\n");
    }
}