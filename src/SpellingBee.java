import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [Sabrina Vohra]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        // Calls recursive method
        makeWords("", letters);
    }

    // Method makes all possible words
    public void makeWords(String word, String letters) {
        // Makes sure all letters haven't been used
        if(letters.isEmpty()) {
            // Adds final word if all letters have been used
            words.add(word);
        }
        for(int i = 0; i < letters.length(); i++) {
            // Adds each possible word
            words.add(word);
            // Creates a new word
            String newWord = word + letters.charAt(i);
            // Gets every letter but the one that was just chosen
            makeWords(newWord, letters.substring(0, i) + letters.substring(i + 1));
        }
    }

    // Sorts possible words
    public void sort() {
        // YOUR CODE HERE
        mergeSort(0, words.size() - 1);
    }

    // Does the divide step of the merge sort algorithm
    public void mergeSort(int low, int high) {
        // Returns if all words have been sorted
        if(high - low == 0) {
            return;
        }
        // Creates medium between high and low values
        int med = (high + low) / 2;
        // Calls merge sort on the first half of the values
        mergeSort(low, med);
        // Calls merge sort on the second half of the values
        mergeSort(med + 1, high);
        // Calls merging method
        merge(low, med, high);
    }

    // Does the merging stop of the merge sort algorithm
    public void merge(int low, int med, int high) {
        // Creates new ArrayList to store values as they are added and sorted
        ArrayList<String> merged = new ArrayList<String>();
        // Initializes values to know where to sort
        int i = low;
        int j = med + 1;
        // Makes sure that values don't traverse too high
        while(i <= med && j <= high) {
            // Adds word from half of segment if it is lesser than the other segment
            if (words.get(i).compareTo(words.get(j)) < 0) {
                // Adds word to end of ArrayList
                merged.add(words.get(i));
                // Increases i since the value has been used
                i++;
            // Adds word from remaining segment if it is lesser than the other segment
            } else {
                // Adds word to end of ArrayList
                merged.add(words.get(j));
                // Increases j since the value has been used
                j++;
            }
        }
        // If one half has more remaining Strings, can continue adding
        while (i <= med) {
            merged.add(words.get(i));
            i++;
        }
        while(j <= high) {
            merged.add(words.get(j));
            j++;
        }
        // Adds each sorted word back to words
        for(int l = 0; l < merged.size(); l++) {
            // Sets words to correct spot so they don't overlap
            words.set(low + l, merged.get(l));
        }
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    public void checkWords() {
        // YOUR CODE HERE
        // Removes words from words if they are not in the dictionary
        // Looks through each word in the list
        for(int i = 0; i < words.size(); i++) {
            // Calls found method on each word in the list
            if(!found(words.get(i), 0, DICTIONARY_SIZE)) {
                // Removes word if it isn't in the dictionary
                words.remove(i);
                // Removes from i so each word is looked at even after one is deleted
                i--;
            }
        }
    }

    // Finds out if a given word is in the dictionary
    public boolean found(String s, int start, int end) {
        // Declares variables for the middle of the area being searched for the word
        int med = (end + start) / 2;
        // Returns true if the dictionary at the medium value is the same as the String currently being searched for
        if(DICTIONARY[med].equals(s)) {
            return true;
        }
        // Returns false if all the values have been checked and there's no match
        else if(start >= end) {
            return false;
        }
        // Changes end variable if the String being searched for is less than the medium value lexicographically
        else if(DICTIONARY[med].compareTo(s) > 0) {
            end = med - 1;
        }
        // Changes start variable if the String being searched for is greater than the medium value lexicographically
        else if(DICTIONARY[med].compareTo(s) < 0) {
            start = med + 1;
        }
        // Recursively calls the method with the new values until a match is found or not
        return found(s, start, end);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
