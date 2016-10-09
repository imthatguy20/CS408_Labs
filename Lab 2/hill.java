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
                        cofact(keymatrix, s);
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
                        cofact(keymatrix, s);
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
        d = d % 26;
        if(d == 0){
            System.out.println("Can not use key! Determinant is zero!");
            return false;
        }
        else if(d % 2 == 0 || d % 13 == 0) {
            System.out.print("Can not use key! The determinant does not have a multiplicative inverse with mod 26.");
            return false;
        }
        else {
            return true;
        }
    }
 
    public static int calculateDeterminant(int matrix[][], int N)
    {
        int res;
        if (N == 1)
            res = matrix[0][0];
        else if (N == 2)
        {
            res = matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1];
        }
        else
        {
            res = 0;
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
                res += Math.pow(-1.0, 1.0 + j1 + 1.0) * matrix[0][j1]* calculateDeterminant(m, N - 1);
            }
        }
        return res;
    }
 
    public static void cofact(int num[][], int f)
    {
        int b[][], fac[][];
        b = new int[f][f];
        fac = new int[f][f];
        int p, q, m, n, i, j;
        for (q = 0; q < f; q++)
        {
            for (p = 0; p < f; p++)
            {
                m = 0;
                n = 0;
                for (i = 0; i < f; i++)
                {
                    for (j = 0; j < f; j++)
                    {
                        b[i][j] = 0;
                        if (i != q && j != p)
                        {
                            b[m][n] = num[i][j];
                            if (n < (f - 2))
                                n++;
                            else
                            {
                                n = 0;
                                m++;
                            }
                        }
                    }
                }
                fac[q][p] = (int) Math.pow(-1, q + p) * calculateDeterminant(b, f - 1);
            }
        }
        trans(fac, f);
    }
 
    public static void trans(int fac[][], int r)
    {
        int b[][], inv[][];
        b = new int[r][r];
        inv = new int[r][r];
        int d = calculateDeterminant(keymatrix, r);
        int mi = convertToMatrixInverse(d % 26) % 26;
        if (mi < 0)
            mi += 26;
        for (int i = 0; i < r; i++)
        {
            for (int j = 0; j < r; j++)
            {
                b[i][j] = fac[j][i];
            }
        }
        for (int i = 0; i < r; i++)
        {
            for (int j = 0; j < r; j++)
            {
                inv[i][j] = b[i][j] % 26;
                if (inv[i][j] < 0){
                    inv[i][j] += 26;
                }
                inv[i][j] *= mi;
                inv[i][j] %= 26;
            }
        }
        System.out.println("\nInverse key:");
        matrixConvertToInverseKey(inv, r);
    }
 
    public static int convertToMatrixInverse(int d)
    {
        BigInteger one = BigInteger.valueOf(d);
        BigInteger two = BigInteger.valueOf(26);
        BigInteger multiplicativeInverse = one.modInverse(two);
        return multiplicativeInverse.intValue();
    }
 
    public static void matrixConvertToInverseKey(int inv[][], int n) {
        String invkey = "";
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                invkey += (char) (inv[i][j] + 97);
            }
        }
        for (int[] r : inv){
            System.out.println(Arrays.toString(r));
        }
        System.out.print(invkey+"\n");
    }
}