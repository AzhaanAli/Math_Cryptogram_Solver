import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

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
        evalPermutations("", uniqueCharacters.size());

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
    public static void evalPermutations(String curr, int length){

        if(curr.length() == length)
            evalContender(curr);
        else
            for(int i = 0; i <= 9; i++)
                if(!curr.contains(String.valueOf(i)))
                    evalPermutations(curr + i, length);

    }
    public static void evalContender(String mapKey){

        char[] asArr = mapKey.toCharArray();
        HashMap<Character, Integer> key = new HashMap<>();

        // Assign proper variables to the key HashMap.
        for(int i = 0; i < mapKey.length(); i++)
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