
import java.util.*;

public class threephase{
    private static Scanner in = new Scanner(System.in);
    private static int keyX = 0, keyY = 0;
    private static int[] keyNVals;
    private static String plainText;

    public static void main(String[] args){
        switch(args[0]){ // Switch on the user choice to encrypt, decrypt, or do both.
            case "1":
                // Write code for encryption only
                System.out.println("Enter the text to be encrypted:");
                plainText = in.nextLine();
                System.out.println("Enter a large value for 'X':");
                keyX = in.nextLine();
                System.out.println("Enter a number for 'n'");
                keyNVals = new int[in.nextInt()];
                System.out.println();
                break;
            case "2":
                // Write code for decryption only 
                break;
            case "3":
                // Write code to do both opperations
                break;
            default: // Case executes if the ussage is not right.
                System.out.println("USAGE: java threephase [1] [2] [3]");
                break;
        }
    }

    // Method runs the encryption portion of the function
    // private int encrypt(){

    // }


    // // Method runs the decryption portion of the program
    // private String decrypt(){

    // }

    // This checks to make sure that the 'n' values can be used or not
    private boolean checkNVals(int nValIn){
        
    }
}