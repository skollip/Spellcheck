package Spellcheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * CSC316 Sec601 Project 4
 * 4/22/15
 * This program processes words in a text document and
 * to the best of its ability will determine whether the
 * word is correctly spelled. It does this by uploading a
 * dictionary text file into a hash table and searching
 * the words in the text for entries in the hash table.
 * 
 * @author Siddhartha
 *
 */
public class Spellcheck {

	/** Initializes the has table */
	public Hash hash;
	
	/** Tracks the size of the dictionary */
	public int size;
	
	/** Tracks the size of the text being checked */
	public int textSize;
	
	/** Amount of misspelled words in the text */
	public int misspelled;
	
	/** Runs the spellcheck algorithm */
	public static void main(String[] args) {
		Spellcheck spell = new Spellcheck();
		spell.startUI();
	}

	/** Constructor for the main program */
	public Spellcheck() {
		
	}
	/**
	 * Runs through the methods to write the final
	 * output to file
	 */
	public void startUI() {
		Scanner console = new Scanner(System.in);
		Scanner input = getInputScanner(console);
		PrintStream output = getOutputPrintStream(console);
		processFile(input);
		Scanner text = getInputScanner(console);
		checkSpelling(text, output);
	}

	/**
	 * Gets the input file and returns it.
	 * 
	 * @param console a scanner to obtain an input file.
	 * @return input the input file to be manipulated.
	 */
	public Scanner getInputScanner(Scanner console) {
		Scanner input = null;

		while (input == null) {
			System.out.print("Enter input file: ");
			String name = console.nextLine();

			try {
				input = new Scanner(new File(name));
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
		return input;
	}
	
	/**
	 * Initializes an output file and returns it.
	 * 
	 * @param console
	 *            a scanner to obtain an output file.
	 * @return output the output file to write to.
	 */
	public PrintStream getOutputPrintStream(Scanner console) {
		PrintStream output = null;

		while (output == null) {
			System.out.print("Enter output file: ");
			String name = console.nextLine();
			File f = new File(name);
			boolean overwrite = true;

			while (f.exists()) {
				System.out.print("File exists. Overwrite? (Y/N): ");
				String answer = console.nextLine();
				char answerLetter = ' ';

				if (answer.length() == 1) {
					answerLetter = answer.toUpperCase().charAt(0);

					if (answerLetter == 'Y') {
						System.out.println("Overwriting file.");
						break;
					} else if (answerLetter == 'N') {
						overwrite = false;
						System.out.println("Not overwriting file.");
						break;
					} else {
						System.out.println("Invalid answer. Please try again.");
					}

				} else {
					System.out.println("Invalid answer. Please try again.");
				}
			}

			if (overwrite) {
				try {
					output = new PrintStream(new File(name));
				} catch (FileNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return output;
	}

	/**
	 * Processes the dictionary file and creates a hash table
	 * to store the values in.
	 * @param input dictionary file
	 */
	public void processFile(Scanner input) {
		String line = "";
		hash = new Hash();
		while (input.hasNextLine()) {
			line = input.nextLine();
			hash.addString(line);
			size++;
		}	
	}
	
	/**
	 * Removes punctuation from the end of the token
	 * @param s token to process
	 * @return the corrected token
	 */
	public String removeEnd(String s) {
		int size = 0;
		for(int i = 1; i <= s.length(); i++) {
			int c = s.charAt(s.length() - i);
			if(c >= 65 && c <= 90) {
				break;
			}
			if(c >= 97 && c <= 122) {
				break;
			}
			if(c >= 48 && c <= 57) {
				break;
			}
			size++;
		}
		return s.substring(0, s.length() - size);
	}
	
	/**
	 * Removes punctuation from the start of the token
	 * @param s token to process
	 * @return the corrected token
	 */
	public String removeStart(String s) {
		int size = 0;
		for(int i = 0; i < s.length(); i++) {
			int c = s.charAt(i);
			if(c >= 65 && c <= 90) {
				break;
			}
			if(c >= 97 && c <= 122) {
				break;
			}
			if(c >= 48 && c <= 57) {
				break;
			}
			size++;
		}
		return s.substring(size, s.length());
	}
	
	/**
	 * Checks the spelling of the words in the input text file
	 * @param text the text file to be spell checked
	 * @param output the list of misspelled words and statistics in a file
	 */
	public void checkSpelling(Scanner text, PrintStream output) {
		while (text.hasNext()) {
			textSize++;
			String word = text.next().trim();
			word = removeStart(word);
			word = removeEnd(word);
			
			if(word.length() > 0) {
				if(!hash.wordSearch(word)) {
					misspelled++;
					output.println(word);
				}
			}
		}
		output.println("The number of words in the dictionary: " + size);
		output.println("The number of words in the text: " + textSize);
		output.println("The number of words misspelled: " + misspelled);
		output.println("The total number of probes: " + hash.getProbe());
		output.println("The average number of probes per word in text: " + (double) hash.getProbe() / (double) textSize);
		output.println("The average number of probes per lookup operation: " + (double) hash.getProbe() / (double) hash.getLookup());
	}
}
