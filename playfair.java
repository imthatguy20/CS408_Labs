import java.util.*;

class PlayfairCipherEncryption
{
    private String keyWord = new String();
    private String key = new String();
    private char matrix_arr[][] = new char[5][5];

    // This code sets the key for the playfair cipher
    public void setkey(String key)
    {
        String keyAdjust = new String();
        boolean flag = false;
        keyAdjust = keyAdjust + key.charAt(0);
        for (int i = 1; i < key.length(); i++)
        {
            for (int j = 0; j < keyAdjust.length(); j++)
            {
                if (key.charAt(i) == keyAdjust.charAt(j))
                {
                    flag = true;
                }
            }
            if (flag == false)
                keyAdjust = keyAdjust + key.charAt(i);
            flag = false;
        }
        keyWord = keyAdjust;
    }

    public void keyGen()
    {
        boolean flag = true;
        char current;
        key = keyWord;
        for (int i = 0; i < 26; i++)
        {
            // Gets the ASCII value for the individual characters
            current = (char) (i + 97);
            if (current == 'j')
                continue;
            for (int j = 0; j < keyWord.length(); j++)
            {
                if (current == keyWord.charAt(j))
                {
                    flag = false;
                    break;
                }
            }
            if (flag)
                key = key + current;
            flag = true;
        }
        //System.out.println(key);
        matrix();
    }

    // This makes a 5 x 5 matrix from the key text
    private void matrix()
    {
        int counter = 0;
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                matrix_arr[i][j] = key.charAt(counter);
                System.out.print(matrix_arr[i][j] + " ");
                counter++;
            }
            System.out.println();
        }
    }

    private String format(String originalText)
    {
        int i = 0;
        int len = 0;
        String text = new String();
        len = originalText.length();
        for (int temp = 0; temp < len; temp++)
        {
            // If the plaintext has a 'j' it will change that to an 'i' when formatted
            if (originalText.charAt(temp) == 'j')
            {
                text = text + 'i';
            }
            else
                text = text + originalText.charAt(temp);
        }
        len = text.length();
        // Traditionally used when there are dup letters
        // for (i = 0; i < len; i = i + 2)
        // {
        //     // Checks for
        //     if (text.charAt(i + 1) == text.charAt(i))
        //     {
        //         text = text.substring(0, i + 1) + 'x' + text.substring(i + 1);
        //     }
        // }
        return text;
    }

    private String[] dividPairs(String curString)
    {
        String original = format(curString);
        int size = original.length();
        // Adds an additional character to the end of the string if not even number of chars
        if (size % 2 != 0)
        {
            size++;
            original = original + 'x';
        }
        String x[] = new String[size / 2];
        int counter = 0;
        // Cuts the string into two separate substrings
        for (int i = 0; i < size / 2; i++)
        {
            x[i] = original.substring(counter, counter + 2);
            counter = counter + 2;
        }
        return x;
    }

    public int[] getDiminsions(char letter)
    {
        int[] key = new int[2];
        if (letter == 'j')
            letter = 'i';
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                if (matrix_arr[i][j] == letter)
                {
                    key[0] = i;
                    key[1] = j;
                    break;
                }
            }
        }
        return key;
    }

    /*
    Encrypts the string using the playfair cipher and appropriate
    shifting if needed
    */
    public String encryptMessage(String Source)
    {
        // Divides the plaintext into an array of strings of length two
        String srcArr[] = dividPairs(Source);
        String code = new String();
        char one;
        char two;
        //System.out.println(Arrays.toString(srcArr));
        // Creates a set of two chars. One for the original and one for the resulting.
        int charOne[] = new int[2];
        int charTwo[] = new int[2];
        for (int i = 0; i < srcArr.length; i++)
        {
            //System.out.println(Arrays.toString(srcArr));
            one = srcArr[i].charAt(0);
            two = srcArr[i].charAt(1);

            // Chars represent the two letters of the pair
            charOne = getDiminsions(one);
            charTwo = getDiminsions(two);

            // Checks to see if the rows are the same
            if (charOne[0] == charTwo[0])
            {
                if (charOne[1] < 4)
                    charOne[1]++; // Moves down a row if they are on the same row
                else
                    charOne[1] = 0; // Back to the first row if the end is reached
                if (charTwo[1] < 4)
                    charTwo[1]++; // Moves down a row if they are on the same row
                else
                    charTwo[1] = 0; // Back to the first row if the end is reached
            }
            // Checks to see if the columns are the same
            else if (charOne[1] == charTwo[1])
            {
                if (charOne[0] < 4)
                    charOne[0]++; // Shift right if not the last column.
                else
                    charOne[0] = 0; // Back to the first column if the end is reached
                if (charTwo[0] < 4)
                    charTwo[0]++; // Shift right if not the last column.
                else
                    charTwo[0] = 0; // Back to the first column if the end is reached
            }
            else
            {
                int temp = charOne[1];
                charOne[1] = charTwo[1];
                charTwo[1] = temp;
            }
            // Adds to the cipher text
            code = code + matrix_arr[charOne[0]][charOne[1]]
                    + matrix_arr[charTwo[0]][charTwo[1]];
        }
        // Returns the cipher text
        return code;
    }

    /*
    This method can be almost the same as the original encryption
    method but the reverse since all decryption is would be the
    opposite of encryption.
    */
    public String decryptMessage(String Source){
      String srcArr[] = dividPairs(Source);
      String code = new String();
      char one;
      char two;
      int charOne[] = new int[2];
      int charTwo[] = new int[2];
      for (int i = 0; i < srcArr.length; i++)
      {
          one = srcArr[i].charAt(0);
          two = srcArr[i].charAt(1);
          charOne = getDiminsions(one);
          charTwo = getDiminsions(two);
          if (charOne[0] == charTwo[0])
          {
              if (charOne[1] > 0)
                  charOne[1]--;
              else
                  charOne[1] = 4;
              if (charTwo[1] > 0)
                  charTwo[1]--;
              else
                  charTwo[1] = 4;
          }
          else if (charOne[1] == charTwo[1])
          {
              if (charOne[0] > 0)
                  charOne[0]--;
              else
                  charOne[0] = 4;
              if (charTwo[0] > 0)
                  charTwo[0]--;
              else
                  charTwo[0] = 4;
          }
          else
          {
              int temp = charOne[1];
              charOne[1] = charTwo[1];
              charTwo[1] = temp;
          }
          code = code + matrix_arr[charOne[0]][charOne[1]]
                  + matrix_arr[charTwo[0]][charTwo[1]];
        //  System.out.println(code);
      }
      return code;
    }

    public static void main(String[] args)
    {
        PlayfairCipherEncryption x = new PlayfairCipherEncryption();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a keyWord:");
        String keyWord = sc.next();
        x.setkey(keyWord);
        x.keyGen();
        System.out.println("Enter word to encrypt: (Make sure length of message is even)");
        String keyIn = sc.next();
        if (keyIn.length() % 2 == 0)
        {
            System.out.println("Encryption: " + x.encryptMessage(keyIn));
            System.out.println("Decryption: "
                    + x.decryptMessage(x.encryptMessage(keyIn)));
        }
        else
        {
            System.out.println("Message length should be even");
        }
        sc.close();
    }
}
