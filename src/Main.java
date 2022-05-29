import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/*
    TODO:
        - EFFICIENCY FIXES (minimizing known false possible solutions):
            - A way to add interval min max constraints for every placeholder.
            - No frontal zero rule.
            - Compute max and min values for certain indices.
            - Let the number of inputs be x:
                - If inputs and outputs are of equal length, then allowed interval of the first character of the
                  output is [x(x + 1) / 2, 9].
                - If the outputs length is greater than the inputs length by a digit, then the maximum value
                  of the outputs first character can be calculated by a descending sum starting from 9, whose
                  length is equal to x. For example if x is 4, the resultant sum would be 9 + 8 + 7 + 6.
                  This example sum equals to 30, thus the maximum value of the outputs first character is 3.
        - User friendly improvements:
            - Abstract more unnecessary methods and code.
            - Replace methods within main method with easier to understand, use, and easier to look at code.
            - A more user intuitive method of passing the puzzles data into the program.
        - Messages for when a puzzle has no solution:
            - Actually do something when theres no solution (at the moment nothing is done).
            - If possible, print a reason for why no solution is possible.
        - Allow inputs and outputs to be expressed with numbers as well as letters.
            - Prevent numbers from showing up as potential values of placeholders in permutation method.
*/

public class Main {


    // ---------------------------------- //
    // Global variables.

    public static String[] inputs;
    public static String output;
    public static ArrayList<Character> uniqueCharacters;


    // ---------------------------------- //
    // Main method.

    public static void main(String[] args) {

        initializeGlobals(
                new String[]{
                        "EUROPA",
                        "JUPITER",
                },
                "NEPTUNE"
        );
        generatePossibleSolutions("", uniqueCharacters.size());

    }


    // ---------------------------------- //
    // Methods.

    // Neatly assign values to global variables.
    public static void initializeGlobals(String[] inputs_param, String output_param){

        inputs = inputs_param;
        output = output_param;
        uniqueCharacters = getUniqueCharacterArray(inputs, output);

    }

    // Search though every possible solution, and evaluate each.
    // The evaluation function will check whether the solution is valid, and if it is, will print it.
    public static void generatePossibleSolutions(String curr, int length){

        // If a possible solution is fully built, stop the recursion and pass it in to the eval function.
        // Otherwise, keep developing new possible solutions.
        if(curr.length() == length)
            evalPossibleSolution(curr);
        else
            // Only loop from 0 to 9, as each placeholder can only represent a one-digit number.
            for(int i = 0; i <= 9; i++)
                // If the possible solution already contains a value, then there is no need to add it again.
                // This is because it is redundant for two placeholders to equal each other, thus we assume they don't.
                if(!curr.contains(String.valueOf(i)))
                    generatePossibleSolutions(curr + i, length);

    }
    public static void evalPossibleSolution(String mapKey){

        char[] asArr = mapKey.toCharArray();
        HashMap<Character, Integer> key = new HashMap<>();

        // Assign proper variables to the key HashMap.
        for(int i = 0; i < mapKey.length(); i++)
            // Subtract 48 from asArr[i] to retrieve the char's numerical value.
            key.put(uniqueCharacters.get(i), asArr[i] - 48);

        // Compute the totals for the inputs with the following key.
        long total = 0;
        for(String input : inputs)
            total += stringToNum(input, key);

        // If the total equals the outputs total, print the solution.
        if(total == stringToNum(output, key))
            System.out.println(key);

    }

    // Using a key that dictates which characters equal what, this method converts a string to a number.
    public static long stringToNum(String string, HashMap<Character, Integer> key){

        StringBuilder asString = new StringBuilder();
        for(char c : string.toCharArray())
            asString.append(key.get(c));
        return Long.parseLong(asString.toString());

    }

    // Returns a sorted array containing all unique characters found within input and outputs.
    public static ArrayList<Character> getUniqueCharacterArray(String[] inputs, String output){

        ArrayList<Character> arr = new ArrayList<>(getUniqueCharacterSet(inputs, output));
        Collections.sort(arr);
        return arr;

    }


    // ---------------------------------- //
    // Helper Methods.

    // Returns all unique characters found within input and outputs.
    public static HashSet<Character> getUniqueCharacterSet(String[] inputs, String output){

        HashSet<Character> set = getUniqueCharacterSet(output);
        set.addAll(getUniqueCharacterSet(inputs));
        return set;

    }
    // Returns all unique characters from an array of strings.
    public static HashSet<Character> getUniqueCharacterSet(String[] strings){

        HashSet<Character> set = new HashSet<>();
        for(String string : strings) set.addAll(getUniqueCharacterSet(string));
        return set;

    }
    // Returns all unique characters from a single string.
    public static HashSet<Character> getUniqueCharacterSet(String string){

        // I used a HashMap, as they can only store a value once.
        HashSet<Character> set = new HashSet<>();
        for(char c : string.toCharArray()) set.add(c);
        return set;

    }


}