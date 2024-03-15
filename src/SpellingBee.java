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
        makeWords("", letters);
    }

    public void makeWords(String word, String letters) {
        // each time we add a letter to the left side and recursively call this
        if(letters.isEmpty()) {
            words.add(word);
        }
        for(int i = 0; i < letters.length(); i++) {
            //word = word + letters.charAt(i);
            words.add(word);
            String newWord = word + letters.charAt(i);
            // Get every letter but the one that was just chosen
            makeWords(newWord, letters.substring(0, i) + letters.substring(i + 1));
            //makeWords(newWord, letters.substring(0, i) + letters.substring(i + 1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        mergeSort(0, words.size() - 1);
    }

    public void mergeSort(int low, int high) {
        if(high - low == 0) {
            return;
//            ArrayList<String> newArray = new ArrayList<String>();
//            newArray.add(words.get(low));
//            return newArray;
        }
        int med = (high + low) / 2;
        mergeSort(low, med);
        mergeSort(med + 1, high);
        merge(low, med, high);
    }

    public void merge(int low, int med, int high) {
        ArrayList<String> merged = new ArrayList<String>();
        int i = low;
        int j = med + 1;
        while(i <= med && j <= high) {
            if (words.get(i).compareTo(words.get(j)) < 0) {
                merged.add(words.get(i));
                i++;
            } else {
                merged.add(words.get(j));
                j++;
            }
        }
        while (i <= med) {
            merged.add(words.get(i));
            i++;
        }
        while(j <= high) {
            merged.add(words.get(j));
            j++;
        }
        for(int l = 0; l < merged.size(); l++) {
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

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        for(int i = 0; i < words.size(); i++) {
            if(!foundAgain(words.get(i), 0, DICTIONARY_SIZE)) {
                words.remove(i);
                i--;
            }
        }
    }
//    public boolean found(String s) {
//        for(int i = 0; i < DICTIONARY_SIZE; i++) {
//            if(DICTIONARY[i].equals(s)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public boolean foundAgain(String s, int start, int end) {
        int med = (end + start) / 2;
        if(DICTIONARY[med].equals(s)) {
            return true;
        }
        else if(start >= end) {
            return false;
        }
        else if(DICTIONARY[med].compareTo(s) > 0) {
            end = med - 1;
        }
        else if(DICTIONARY[med].compareTo(s) < 0) {
            start = med + 1;
        }
        return foundAgain(s, start, end);
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
