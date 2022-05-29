import java.util.*;

/*
    TODO:
        - EFFICIENCY FIXES (minimizing known false possible solutions):
            - Maybe create unique blacklist arrays that are passed down and modified though each generation branch.
            - No even/ odd values (uncovered by equation formation and solving).
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

    public static String[] blacklistValues;


    // ---------------------------------- //
    // Main method.

    public static void main(String[] args) {

        // Old approach --> 10293 milliseconds taken.
        // Frontal Zeros --> 5100 milliseconds taken.

//        int rounds = 5;
//        long elapsed = -System.currentTimeMillis() * rounds;
//
//        for(int i = 0; i < rounds; i++)
//        {
//            initializeGlobals(
//                    new String[]{
//                            "EUROPA",
//                            "JUPITER",
//                    },
//                    "NEPTUNE"
//            );
//            generatePossibleSolutions();
//
//            elapsed += System.currentTimeMillis();
//        }
//        elapsed /= rounds;
//
//        System.out.println(elapsed + " milliseconds taken.");

            initializeGlobals(
                    new String[]{
                            "BANJO",
                            "VIOLA",
                    },
                    "VIOLIN"
            );
            generatePossibleSolutions();


    }


    // ---------------------------------- //
    // Methods.

    // Neatly assign values to global variables.
    public static void initializeGlobals(String[] inputs_param, String output_param){

        // Initialize inputs and outputs, and assign values to uniqueCharacters.
        inputs = inputs_param;
        output = output_param;
        uniqueCharacters = getUniqueCharacterArray(inputs, output);

        // Initialize and assign values to the blacklists.
        initializeBlacklist();
        setBlacklistValues(inputs_param, output_param);

    }
    public static void initializeBlacklist(){

        blacklistValues = new String[uniqueCharacters.size()];
        Arrays.fill(blacklistValues, "");

    }

    // Applies all blacklist rules to the list.
    public static void setBlacklistValues(String[] inputs_param, String output_param){

        blackListFrontalZeros(inputs_param, output_param);
        blacklistKnownNumbers(inputs_param, output_param);

    }

    // Makes it so the leading placeholders of numbers are not considered to be 0.
    public static void blackListFrontalZeros(String[] inputs_param, String output_param){

        // Add zero to all blacklists of frontal input characters.
        for(String input : inputs_param)
            blackListFrontalZero(input);
        // Add zero to the blacklist of the frontal output character.
        blackListFrontalZero(output_param);

    }
    public static void blackListFrontalZero(String str){

        // Add zero to the first characters blacklist.
        int index = uniqueCharacters.indexOf(str.charAt(0));
        blacklistValues[index] += "0";

    }

    // Makes it so all characters contain blacklists for numbers that are already known.
    public static void blacklistKnownNumbers(String[] inputs_param, String output_param){

        // Blacklist known numbers within the inputs.
        for(String input : inputs_param)
            blacklistKnownNumber(input);
        // Blacklist known numbers within the output.
        blacklistKnownNumber(output_param);

    }
    public static void blacklistKnownNumber(String str){

        for(char c : str.toCharArray())
            if(charIsNumber(c))
                for(int i = 0; i < blacklistValues.length; i++)
                    blacklistValues[i] += c;

    }

    // Search though every possible solution, and evaluate each.
    // The evaluation function will check whether the solution is valid, and if it is, will print it.
    public static void generatePossibleSolutions(String curr, int length){

        int solutionLength = curr.length();

        // If a possible solution is fully built, stop the recursion and pass it in to the eval function.
        // Otherwise, keep developing new possible solutions.
        if(solutionLength == length)
            evalPossibleSolution(curr);
        else
        {
            // If the current value is a number rather than a placeholder,
            // simply use it instead of generating solutions from the loop.
            char c = uniqueCharacters.get(curr.length());
            if (charIsNumber(c))
                generatePossibleSolutions(curr + c, length);

            else

                // Otherwise, loop from 0 to 9, and branch new possible solutions.
                for (int i = 0; i <= 9; i++)
                {
                    String strVal = String.valueOf(i);
                    // If the possible solution already contains a value, then there is no need to add it again.
                    // This is because it is redundant for two placeholders to equal each other, thus we assume they don't.
                    if (!curr.contains(strVal) && !blacklistValues[solutionLength].contains(strVal))
                        generatePossibleSolutions(curr + i, length);
                }
        }

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
    // Recursive base-call function.
    public static void generatePossibleSolutions(){

        generatePossibleSolutions("", uniqueCharacters.size());

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

    // Returns whether a character represents a value between 0 and 9.
    public static boolean charIsNumber(char c){

        return 48 <= c && c <= 57;

    }


}