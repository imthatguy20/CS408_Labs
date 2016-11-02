
import java.util.*;
import java.lang.*;

public class threephase{
    private static Scanner in = new Scanner(System.in);
    private static int keyX = 0, keyY = 0;
    private static int[] keyNVals;
    private static int[] plainTextASCII;
    private static String plainText;

    public static void main(String[] args){
        switch(args[0]){ // Switch on the user choice to encrypt, decrypt, or do both.
            case "1":
                // Write code for encryption only
                System.out.println("Enter the text to be encrypted:");
                plainText = in.nextLine();
                System.out.println("Enter a large value for 'X':");
                keyX = in.nextInt();
                addKeyXToChars(plainText);
                // System.out.println(Integer.toBinaryString(keyX)); // Used for getting the binary representation of a number
                System.out.println("Enter a number for 'n'");
                keyNVals = new int[in.nextInt()];
                for(int i = 0; i < keyNVals.length; i++){
                    // Loop makes sure that only valid values are added to the array
                    System.out.println("Enter value #"+i+" for the second key part:");
                    keyNVals[i] = in.nextInt();
                    while(!checkNVals(keyNVals[i])){
                        System.out.println("Enter value #"+i+" for the second key part:");
                        keyNVals[i] = in.nextInt();
                    }
                }
                //System.out.println(Arrays.toString(keyNVals)); // DEBUG
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
    private static boolean checkNVals(int nValIn){
        // Make sure that the value is within the bounds 
        int lowerBound = ((int) Math.pow(2, Integer.toBinaryString(keyX).length()-1)) - keyX; // Lower bound as calculated
        //System.out.println(lowerBound); // Bound works
        int upperBound = ((int) Math.pow(2, Integer.toBinaryString(keyX).length())) - keyX - 255; // Upper bound as calculated
        //System.out.println(upperBound); // Bound works
        if(nValIn >= lowerBound && nValIn < upperBound){
            return true;
        } else {
            return false;
        }
    }

    private static void addKeyXToChars(String plainTextIn){
        plainTextASCII = new int[plainTextIn.length()];
        for(int i = 0; i < plainTextIn.length(); i++){
            plainTextASCII[i] = (int) plainTextIn.charAt(i) + keyX; // Convert to ASCII and add first part of the key.
            //System.out.println(Arrays.toString(plainTextASCII)); // DEBUG
        } 
    }
}