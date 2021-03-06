
import java.util.*;
import java.lang.*;
import java.math.*;

public class threephase{
    private static Scanner in = new Scanner(System.in);
    private static int keyX = 0, keyY = 0;
    private static int[] keyNVals;
    private static int[] plainTextASCII;
    private static String plainText, keyYBitString, phaseOneRes, cipherText; // String to be encrypted
    private static String encStringRes = ""; // Resulting string from the encryption

    public static void main(String[] args){
        switch(args[0]){ // Switch on the user choice to encrypt, decrypt, or do both.
            case "1":
                // Write code for encryption only
                codec("1");
                phaseOneEnc(plainText);
                phaseTwoEnc(plainTextASCII);
                phaseThreeEnc(plainTextASCII);
                break;
            case "2":
                // Code will run the decryption portion only
                codec("2");
                phaseOneDec(cipherText);
                phaseTwoDec(phaseOneRes);
                phaseThreeDec(plainTextASCII);
                break;
            case "3":
                // Write code to do both opperations
                codec("3");
                System.out.println("***** ENCRYPTION PHASES *****\n");
                phaseOneEnc(plainText);
                phaseTwoEnc(plainTextASCII);
                phaseThreeEnc(plainTextASCII);
                System.out.println("***** DECRYPTION PHASES *****\n");
                phaseOneDec(plainText);
                phaseTwoDec(phaseOneRes);
                phaseThreeDec(plainTextASCII);
                break;
            default: // Case executes if the ussage is not right.
                System.out.println("USAGE: java threephase [1] [2] [3]");
                break;
        }
    }

    // Separates the rest of the main method code away from it to make it more readable
    public static void codec(String arg){
        switch(arg){
            case "1":
                System.out.println("Enter the text to be encrypted:");
                plainText = in.nextLine();
                System.out.println("Enter a large value for 'X':");
                keyX = in.nextInt();
                while(keyX > 1792){
                   System.out.println("Enter a large value for 'X':");
                   keyX = in.nextInt(); 
                }
                System.out.println("\nP = "+Integer.toBinaryString(keyX).length()+"\n");
                // System.out.println(Integer.toBinaryString(keyX)); // Used for getting the binary representation of a number
                System.out.println("Enter a number for 'n'");
                keyNVals = new int[in.nextInt()];
                System.out.println();
                for(int i = 0; i < keyNVals.length; i++){
                    // Loop makes sure that only valid values are added to the array
                    System.out.println("Enter value #"+i+" for the second key part:");
                    keyNVals[i] = in.nextInt();
                    while(!checkNVals(keyNVals[i])){
                        System.out.println("Enter value #"+i+" for the second key part:");
                        keyNVals[i] = in.nextInt();
                    }
                }
                System.out.println("\nEnter a large value for 'Y':");
                keyY = in.nextInt();
                keyYBitString = Integer.toBinaryString(keyY);
                System.out.println("\nB = "+keyYBitString.length()+"\n");
                break;
            case "2":
                System.out.println("Enter the text to be decrypted:");
                cipherText = in.nextLine();
                System.out.println("Enter the value for the 'X' portion of the key:");
                keyX = in.nextInt();
                while(keyX > 1792){
                   System.out.println("Enter a large value for 'X':");
                   keyX = in.nextInt(); 
                }
                System.out.println("\nP = "+Integer.toBinaryString(keyX).length()+"\n");
                System.out.println("Enter the the value of 'n' used for encryption:");
                keyNVals = new int[in.nextInt()];
                System.out.println();
                for(int i = 0; i < keyNVals.length; i++){
                    // Loop makes sure that only valid values are added to the array
                    System.out.println("Enter value #"+i+" for the second key part:");
                    keyNVals[i] = in.nextInt();
                    while(!checkNVals(keyNVals[i])){
                        System.out.println("Enter value #"+i+" for the second key part:");
                        keyNVals[i] = in.nextInt();
                    }
                }
                System.out.println("Enter the value used for 'Y':");
                keyY = in.nextInt();
                keyYBitString = Integer.toBinaryString(keyY);
                System.out.println("\nB = "+keyYBitString.length()+"\n");
                break;
            case "3":
                System.out.println("Enter the text to be encrypted:");
                plainText = in.nextLine();
                System.out.println("Enter a large value for 'X':");
                keyX = in.nextInt();
                while(keyX > 1792){
                   System.out.println("Enter a large value for 'X':");
                   keyX = in.nextInt(); 
                }
                System.out.println("\nP = "+Integer.toBinaryString(keyX).length()+"\n");
                // System.out.println(Integer.toBinaryString(keyX)); // Used for getting the binary representation of a number
                System.out.println("Enter a number for 'n'");
                keyNVals = new int[in.nextInt()];
                System.out.println();
                for(int i = 0; i < keyNVals.length; i++){
                    // Loop makes sure that only valid values are added to the array
                    System.out.println("Enter value #"+i+" for the second key part:");
                    keyNVals[i] = in.nextInt();
                    while(!checkNVals(keyNVals[i])){
                        System.out.println("Enter value #"+i+" for the second key part:");
                        keyNVals[i] = in.nextInt();
                    }
                }
                System.out.println("\nEnter a large value for 'Y':");
                keyY = in.nextInt();
                keyYBitString = Integer.toBinaryString(keyY);
                System.out.println("\nB = "+keyYBitString.length()+"\n");
                break;
        }                
    }

    /*
    ----------------------------------------------------------------------------
                                ENCRYPTION METHODS
    ----------------------------------------------------------------------------
    */
    // This checks to make sure that the 'n' values can be used or not
    private static boolean checkNVals(int nValIn){
        int lowerBound = ((int) Math.pow(2, Integer.toBinaryString(keyX).length()-1)) - keyX; // Lower bound as calculated
        int upperBound = ((int) Math.pow(2, Integer.toBinaryString(keyX).length())) - keyX - 255; // Upper bound as calculated
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
        } 
        System.out.println("Phase #1: "+Arrays.toString(plainTextASCII));
    }

    // Adds the second part of the key to the values from the previous round
    private static void phaseTwoEnc(int[] asciiWithX){
        int j = 0;
        for(int i = 0; i < asciiWithX.length; i++){
            asciiWithX[i] += keyNVals[j]; // Allows loop back when all 'n' vals have been used
            j++;
            if(j == keyNVals.length)
                j = 0;
        }
        System.out.println("Phase #2: "+Arrays.toString(asciiWithX));
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
            encStringRes += Integer.toBinaryString(block);
        }
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
        while((encBitString.toString().length() % 8) != 0){
            encBitString.append("0");
        }
        System.out.println("Phase #3: "+encBitString.toString()+"\n");
        bitStringToCT(encBitString.toString());
    }
    
    // Method converts the bitstring from the last result to the CT.
    private static void bitStringToCT(String bitString){
        StringBuilder ct = new StringBuilder();
        String section = "";
        ArrayList<String> dividedBitString = new ArrayList<String>();
        int j = 0;
        for(int i = 0; i < bitString.length(); i++){
                section += bitString.charAt(i);
                j++;
                if(j == 8){
                    j = 0;
                    dividedBitString.add(section);
                    section = "";
                }
        } 
        for(String resultSect : dividedBitString){
            char c; 
            c = (char) Integer.parseInt(resultSect, 2);
            ct.append(c);
        }
        plainText = ct.toString();
        System.out.println("Cipher Text: "+plainText+"\n");
    }
    
     /*
    ----------------------------------------------------------------------------
                                DECRYPTION METHODS
    ----------------------------------------------------------------------------
    */
    
    // Runs the first part of the decryption process 
    public static void phaseOneDec(String cipherText){
        StringBuilder cipherTextBitString = new StringBuilder();
        StringBuilder decBitString = new StringBuilder();
        String section = "";
        String lastSect = "";
        ArrayList<String> dividedBitString = new ArrayList<String>();
        int i = 0;
        for(char c : cipherText.toCharArray()){
            while((Integer.toBinaryString((int)c).length() + i) != 8){ // Adds 0 to the end of the bit string to make up for trimmed leading zeros
                cipherTextBitString.append("0");
                i++;
            }
            i = 0;
            cipherTextBitString.append(Integer.toBinaryString((int)c)); // Add section to the string
        }
       while((cipherTextBitString.length() % Integer.toBinaryString(keyX).length()) != 0){ // Trim all of the excess 0's
           cipherTextBitString.deleteCharAt(cipherTextBitString.length()-1); // Reduces length and hence removes the last char
       }
       int j = 0;
       for(int a = 0; a < cipherTextBitString.length(); a++){ 
            section += cipherTextBitString.charAt(a);
            j++;
            if(j == keyYBitString.length()){
                j = 0;
                dividedBitString.add(section);
                section = "";
            }
            if (a == (cipherTextBitString.length()-1)) // Add the very last part even though it might not be 
                dividedBitString.add(section);
        }
         j = 0;
        for(String sect : dividedBitString){
            String temp = "";
            for(int b = 0; b < sect.length(); b++){ // XOR component 
                if(j == 0)
                    temp += (sect.charAt(b) == keyYBitString.charAt(b)) ? "0" : "1";
                else 
                    temp += (sect.charAt(b) == lastSect.charAt(b)) ? "0" : "1";
            }
            j++;
            lastSect = sect;
            decBitString.append(temp);
            temp = "";
        }
        phaseOneRes = decBitString.toString();
        System.out.println("Phase #1: "+decBitString);
    }
    
    //  Runs the second part of the of the decryption 
    public static void phaseTwoDec(String decBitString){
        ArrayList<Integer> resultingIntVals = new ArrayList<Integer>();
        String temp = "";
        int j = 0;
        for(int i = 0; i < decBitString.length(); i++){ // Creates the binary string from the CT
            temp += decBitString.charAt(i);
            j++;
            if(j >= Integer.toBinaryString(keyX).length()){
                j = 0;
                resultingIntVals.add(Integer.parseInt(temp, 2));
                temp = "";
            }
        }
        plainTextASCII = new int[resultingIntVals.size()];
        for(int i = 0; i < resultingIntVals.size(); i++){
            plainTextASCII[i] = resultingIntVals.get(i);
        }
        j = 0;
        // Removes the keyNVals from the middle from the values
        for(int t = 0; t < plainTextASCII.length; t++){
            plainTextASCII[t] -= keyNVals[j]; // Allows loop back when all 'n' vals have been used
            j++;
            if(j == keyNVals.length)
                j = 0;
        }
        System.out.println("Phase #2: "+Arrays.toString(plainTextASCII));
    }
    
    // Runs the third phase for the decryption
    public static void phaseThreeDec(int[] phaseTwoRes){
        StringBuilder pt = new StringBuilder();
        for(int i = 0; i < phaseTwoRes.length; i++){
            plainTextASCII[i] -= keyX; // Convert to ASCII and add first part of the key.
            pt.append((char)plainTextASCII[i]);
        }
        System.out.println("Phase #3: "+Arrays.toString(phaseTwoRes));
        System.out.println("\nPlain Text: "+pt.toString()+"\n");
    }
}
   
    