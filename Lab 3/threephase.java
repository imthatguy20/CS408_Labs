
import java.util.*;
import java.lang.*;
import java.math.*;

public class threephase{
    private static Scanner in = new Scanner(System.in);
    private static int keyX = 0, keyY = 0;
    private static int[] keyNVals;
    private static int[] plainTextASCII;
    private static String plainText, keyYBitString; // String to be encrypted
    private static String encStringRes = ""; // Resulting string from the encryption

    public static void main(String[] args){
        switch(args[0]){ // Switch on the user choice to encrypt, decrypt, or do both.
            case "1":
                // Write code for encryption only
                System.out.println("Enter the text to be encrypted:");
                plainText = in.nextLine();
                System.out.println("Enter a large value for 'X':");
                keyX = in.nextInt();
                phaseOneEnc(plainText);
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
                System.out.println("Enter a large value for 'Y':");
                keyY = in.nextInt();
                keyYBitString = Integer.toBinaryString(keyY);
                phaseTwoEnc(plainTextASCII);
                phaseThreeEnc(plainTextASCII);
                //System.out.println(encStringRes);
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

    // Adds the first part of the key to the plain text ASCII values for each char
    private static void phaseOneEnc(String plainTextIn){
        plainTextASCII = new int[plainTextIn.length()];
        for(int i = 0; i < plainTextIn.length(); i++){
            plainTextASCII[i] = (int) plainTextIn.charAt(i) + keyX; // Convert to ASCII and add first part of the key.
            //System.out.println(Arrays.toString(plainTextASCII)); // DEBUG
        } 
    }

    // Adds the second part of the key to the values from the previous round
    private static void phaseTwoEnc(int[] asciiWithX){
        // System.out.println(Arrays.toString(asciiWithX)); // DEBUG
        // System.out.println(Arrays.toString(keyNVals)); // DEBUG
        int j = 0;
        for(int i = 0; i < asciiWithX.length; i++){
            asciiWithX[i] += keyNVals[j]; // Allows loop back when all 'n' vals have been used
            j++;
            if(j == keyNVals.length)
                j = 0;
            // System.out.println(Arrays.toString(asciiWithX)); // DEBUG
        }
    }

    /* This will take the result from phase two and run the XOR 
       opperation on it with the block size of the 'Y' value.
       This also will concatinate all of the results with each
       other to get the end result. */ 
    private static void phaseThreeEnc(int[] phaseTwoResult){
        // Convert to a binary string
        int j = 0;
        boolean state = true;
        String element = "";
        ArrayList<String> divideBinaryArray = new ArrayList<String>();
        String lastRes = "";
        StringBuilder encBitString = new StringBuilder();
        for(int block : phaseTwoResult){
            // System.out.println(Integer.toBinaryString(block)); // DEBUG
            encStringRes += Integer.toBinaryString(block);
            // System.out.println(encStringRes); // DEBUG
        }
        // System.out.println(keyYArray[0]); // DEBUG
        // System.out.println(encStringResArray); // DEBUG
        while(j < encStringRes.length()){
            for(int i = 0; i < Integer.toBinaryString(keyY).length(); i++){
                if(j != encStringRes.length()){
                    element += encStringRes.charAt(j);
                    j++;
                }
            }
            divideBinaryArray.add(element);
            element = "";
        }
        j = 0;
        for(String sect : divideBinaryArray){
            String temp = "";
            for(int i = 0; i < sect.length(); i++){
                if(j == 0)
                    temp += (sect.charAt(i) == keyYBitString.charAt(i)) ? "0" : "1";
                else 
                    temp += (sect.charAt(i) == lastRes.charAt(i)) ? "0" : "1";
            }
            j++;
            lastRes = temp;
            temp = "";
            encBitString.append(lastRes);
        }
        //System.out.println(Arrays.toString(divideBinaryArray.toArray()));
        while((encBitString.toString().length() % 8) != 0){
            encBitString.append("0");
        }
        System.out.println(encBitString.toString());
        bitStringToCT(encBitString.toString());
    }
    
    // Method converts the bitstring from the last result to the CT.
    private static void bitStringToCT(String bitString){
        StringBuilder ct = new StringBuilder();
        String section = "";
        ArrayList<String> dividedBitString = new ArrayList<String>();
        System.out.print(bitString.length());
        int j = 0;
        for(int i = 0; i < bitString.length(); i++){
            if(j != 8){
                section += bitString.charAt(i);
                // System.out.println(section); // DEBUG
                j++;
            }
            else{
                j = 0;
                dividedBitString.add(section);
                section = "";
            }
        } // TODO Some bits are being skipped when moving to divided string
        System.out.println(dividedBitString.toString());
    }
}