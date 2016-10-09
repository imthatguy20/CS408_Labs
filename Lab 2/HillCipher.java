import java.util.*;
 
public class HillCipher {
    public static int keymatrix[][];
    public static int linematrix[];
    public static int resultmatrix[];
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final Scanner in = new Scanner(System.in);
    public static String line, key;
    public static double sq;
 
    // Main method of the program
    public static void main(String args[]) {
        try{
            switch(args[0]){
                case "-e": // Recognizes flag for encryption
                System.out.println("Text to be encrypted: ");
                line = in.nextLine();
                System.out.println("Key for encryption: ");
                key = in.nextLine();
                sq = Math.sqrt(key.length());
                if (sq != (long) sq)
                    System.out.println("Error: Invalid key length.  Does not form a square matrix!");
                else {
                    int s = (int) sq;
                    if (invertableCheck(key, s)) {
                        System.out.println("Result:");
                        divide(line, s);
                        cofact(keymatrix, s);
                    }
                }
                break;
            case "-d": // Recognizes flag for decryption
                System.out.println("Text to be decrypted: ");
                line = in.nextLine();
                System.out.println("Key for decryption: ");
                key = in.nextLine();
                sq = Math.sqrt(key.length());
                if (sq != (long) sq)
                    System.out.println("Error: Invalid key length.  Does not form a square matrix!");
                else {
                    int s = (int) sq;
                    if (invertableCheck(key, s)) {
                        System.out.println("Result:");
                        divide(line, s);
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

    public static void divide(String temp, int s)
    {
        while (temp.length() > s)
        {
            String sub = temp.substring(0, s);
            temp = temp.substring(s, temp.length());
            perform(sub);
        }
        if (temp.length() == s)
            perform(temp);
        else if (temp.length() < s)
        {
            for (int i = temp.length(); i < s; i++)
                temp = temp + 'x';
            perform(temp);
        }
    }
 
    public static void perform(String line)
    {
        lineConvertToMatrix(line);
        multiplyLineMatrixKeyMatrix(line.length());
        resultToString(line.length());
    }
 
    public static void convertKeyToMatrix(String key, int len)
    {
        keymatrix = new int[len][len];
        int c = 0;
        for (int i = 0; i < len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                keymatrix[j][i] = ((int) key.charAt(c)) - 97;
                c++;
            }
        }
        for (int[] row : keymatrix){
            System.out.println(Arrays.toString(row));
        }
    }
 
    public static void lineConvertToMatrix(String line)
    {
        linematrix = new int[line.length()];
        for (int i = 0; i < line.length(); i++)
        {
            linematrix[i] = ((int) line.charAt(i)) - 97;
        }
        System.out.print(Arrays.toString(linematrix));
    }
 
    public static void multiplyLineMatrixKeyMatrix(int len)
    {
        resultmatrix = new int[len];
        for (int i = 0; i < len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                resultmatrix[i] += keymatrix[i][j] * linematrix[j];
            }
            resultmatrix[i] %= 26;
        }
    }
 
    public static void resultToString(int len)
    {
        String result = "";
        for (int i = 0; i < len; i++)
        {
            result += (char) (resultmatrix[i] + 97);
        }
        System.out.print(result);
    }
 
    public static boolean invertableCheck(String key, int len)
    {
        convertKeyToMatrix(key, len);
        int d = calculateDeterminant(keymatrix, len);
        d = d % 26;
        if (d == 0)
        {
            System.out.println("Invalid key!!! Key is not invertible because determinant=0...");
            return false;
        }
        else
        {
            return true;
        }
    }
 
    public static int calculateDeterminant(int A[][], int N)
    {
        int res;
        if (N == 1)
            res = A[0][0];
        else if (N == 2)
        {
            res = A[0][0] * A[1][1] - A[1][0] * A[0][1];
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
                        m[i - 1][j2] = A[i][j];
                        j2++;
                    }
                }
                res += Math.pow(-1.0, 1.0 + j1 + 1.0) * A[0][j1]
                        * calculateDeterminant(m, N - 1);
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
        int i, j;
        int b[][], inv[][];
        b = new int[r][r];
        inv = new int[r][r];
        int d = calculateDeterminant(keymatrix, r);
        int mi = convertToMatrixInverse(d % 26);
        mi %= 26;
        if (mi < 0)
            mi += 26;
        for (i = 0; i < r; i++)
        {
            for (j = 0; j < r; j++)
            {
                b[i][j] = fac[j][i];
            }
        }
        for (i = 0; i < r; i++)
        {
            for (j = 0; j < r; j++)
            {
                inv[i][j] = b[i][j] % 26;
                if (inv[i][j] < 0)
                    inv[i][j] += 26;
                inv[i][j] *= mi;
                inv[i][j] %= 26;
            }
        }
        System.out.println("\nInverse key:");
        matrixConvertToInverseKey(inv, r);
    }
 
    public static int convertToMatrixInverse(int d)
    {
        int q, r1, r2, r, t1, t2, t;
        r1 = 26;
        r2 = d;
        t1 = 0;
        t2 = 1;
        while (r1 != 1 && r2 != 0)
        {
            q = r1 / r2;
            r = r1 % r2;
            t = t1 - (t2 * q);
            r1 = r2;
            r2 = r;
            t1 = t2;
            t2 = t;
        }
        return (t1 + t2);
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
        System.out.print(invkey+"\n");
    }
}