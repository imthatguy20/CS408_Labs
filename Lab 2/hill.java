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
        int inv[][] = new int[size][size];
        int d[][] = new int[size][size];
        int m, n;
        for (int q = 0; q < size; q++) {
            for (int p = 0; p < size; p++) {
                m = 0;
                n = 0;
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        d[i][j] = 0;
                        if (i != q && j != p) {
                            d[m][n] = num[i][j];
                            if (n < (size - 2))
                                n++;
                            else {
                                n = 0;
                                m++;
                            }
                        }
                    }
                }
                fac[q][p] = (int) Math.pow(-1, q + p) * calculateDeterminant(d, size - 1);
            }
        }
        int mi = convertToMatrixInverse(calculateDeterminant(keymatrix, size) % 26) % 26;
        mi += (mi < 0) ? 26 : 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                d[i][j] = fac[j][i];
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                inv[i][j] = (d[i][j] * mi) % 26;
                inv[i][j] += (inv[i][j] < 0) ? 26 : 0;
                inv[i][j] %= 26;
            }
        }
        System.out.println("\nInverse key:");
        matrixConvertToInverseKey(inv, size);
    }
 
    public static int convertToMatrixInverse(int d) {
        BigInteger one = BigInteger.valueOf(d);
        BigInteger two = BigInteger.valueOf(26);
        BigInteger multiplicativeInverse = one.modInverse(two);
        return multiplicativeInverse.intValue();
    }
 
    public static void matrixConvertToInverseKey(int inv[][], int n) {
        String invkey = "";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                invkey += (char) (inv[i][j] + 97);
            }
        }
        System.out.print(invkey+"\n");
    }
}