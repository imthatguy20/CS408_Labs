import java.util.Scanner;

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
        System.out.println(key);
        matrix();
    }

    // This makes the matrix and
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
            if (originalText.charAt(temp) == 'j')
            {
                text = text + 'i';
            }
            else
                text = text + originalText.charAt(temp);
        }
        len = text.length();
        for (i = 0; i < len; i = i + 2)
        {
            if (text.charAt(i + 1) == text.charAt(i))
            {
                text = text.substring(0, i + 1) + 'x' + text.substring(i + 1);
            }
        }
        return text;
    }

    private String[] dividTwoPairs(String curString)
    {
        String original = format(curString);
        int size = original.length();
        if (size % 2 != 0)
        {
            size++;
            original = original + 'x';
        }
        String x[] = new String[size / 2];
        int counter = 0;
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

    public String encryptMessage(String Source)
    {
        String srcArr[] = dividTwoPairs(Source);
        String code = new String();
        char one;
        char two;
        int portionOne[] = new int[2];
        int portionTwo[] = new int[2];
        for (int i = 0; i < srcArr.length; i++)
        {
            one = srcArr[i].charAt(0);
            two = srcArr[i].charAt(1);
            portionOne = getDiminsions(one);
            portionTwo = getDiminsions(two);
            if (portionOne[0] == portionTwo[0])
            {
                if (portionOne[1] < 4)
                    portionOne[1]++;
                else
                    portionOne[1] = 0;
                if (portionTwo[1] < 4)
                    portionTwo[1]++;
                else
                    portionTwo[1] = 0;
            }
            else if (portionOne[1] == portionTwo[1])
            {
                if (portionOne[0] < 4)
                    portionOne[0]++;
                else
                    portionOne[0] = 0;
                if (portionTwo[0] < 4)
                    portionTwo[0]++;
                else
                    portionTwo[0] = 0;
            }
            else
            {
                int temp = portionOne[1];
                portionOne[1] = portionTwo[1];
                portionTwo[1] = temp;
            }
            code = code + matrix_arr[portionOne[0]][portionOne[1]]
                    + matrix_arr[portionTwo[0]][portionTwo[1]];
        }
        return code;
    }

    public String decryptMessage(){
      
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
        }
        else
        {
            System.out.println("Message length should be even");
        }
        sc.close();
    }
}
