package Spellcheck;

/**
 * Hash table object to store the dictionary
 * and reference for spell checking.
 * 
 * @author Siddhartha
 *
 */
public class Hash {

	/** The size of the hash table which is next prime number above n*2+1 */
	public static final int HASH_SIZE = 50291;
	
	/** Initializes the hash array */
	private String[] words = new String[HASH_SIZE];
	
	/** Amount of lookup operations */
	private int lookup;
	
	/** Amount of probes */
	private int probe;
	
	/** Constructor of hash table object */
	public Hash() {
		
	}
	
	/** 
	 * Calculates a hash value based on polynomial hash code.
	 * A cyclic shift of r = power of 2.
	 * used for storing and searching for dictionary
	 * values.
	 * @param s String to calculate hash value from
	 * @return the hash value
	 */
	public int calcHash(String s) {
		int ascii = 0;
		int hashVal = 0;
		double exp;
		for(int i = 0; i < s.length(); i++) {
			ascii = s.charAt(i);
			exp = i;
			hashVal += ascii * Math.pow(2, exp);
		}
		hashVal = hashVal % HASH_SIZE;
		return hashVal;
	}
	
	/**
	 * (Double Hashing Technique)
	 * Calculates the jump value for the string based on polynomial hash code.
	 * A cyclic shift of r = power of 3.
	 * It is used if a collision occurs in the hash table.
	 * @param s String to calculate hash value from
	 * @return the hash value
	 */
	public int calcHash2(String s) {
		int ascii = 0;
		int hashVal = 0;
		double exp;
		for(int i = 0; i < s.length(); i++) {
			ascii = s.charAt(i);
			exp = i;
			hashVal += ascii * Math.pow(3, exp);
		}
		hashVal = hashVal % HASH_SIZE;
		return hashVal;
	}
	
	/**
	 * Adds an entry to the hash table
	 * @param s String to add to the table
	 */
	public void addString(String s) {
		int idx = calcHash(s);
		int idx2 = calcHash2(s);
		while(words[idx] != null) {
			idx = idx + idx2;
			idx = idx % HASH_SIZE;
		}
		words[idx] = s;
	}
	
	/**
	 * Returns a truth value based on if the word is
	 * spelled correctly.
	 * @param s String for spell checking
	 * @return true if word is correct, otherwise false
	 */
	public boolean wordSearch(String s) {
		if(sCheck(s)) {
			return true;
		}
		
		if(s.charAt(0) == Character.toUpperCase(s.charAt(0))) {
			String c = s.substring(0, 1).toLowerCase();
			if(s.length() > 1) {
				s = c + s.substring(1, s.length());
				if(sCheck(s)) {
					return true;
				}
			} else {
				if(sCheck(c)) {
					return true;
				}
			}
		}
		
		String d;
		
		if(s.length() > 2 && s.substring(s.length() - 2).equals("'s")) {
			s = s.substring(0, s.length() - 2);
			if(sCheck(s)) {
				return true;
			}
		}
		if(s.length() > 1 && s.charAt(s.length() - 1) == 's') {
			s = s.substring(0, s.length()-1);
			if(sCheck(s)) {
				return true;
			}
			d = s + "es";
			if(sCheck(d)) {
				return true;
			}
		}
		if(s.length() > 2 && s.substring(s.length()-2).equals("ed")) {
			s = s.substring(0, s.length() - 2);
			if(sCheck(s)) {
				return true;
			}
			d = s + "e";
			if(sCheck(d)) {
				return true;
			}
		}
		if(s.length() > 2 && s.substring(s.length() - 2).equals("er")) {
			s = s.substring(0, s.length() - 2);
			if(sCheck(s)) {
				return true;
			}
			d = s + "r";
			if(sCheck(d)) {
				return true;
			}
		}
		if(s.length() > 3 && s.substring(s.length() - 3).equals("ing")) {
			s = s.substring(0, s.length() - 3);
			if(sCheck(s)) {
				return true;
			}
			d = s + "e";
			if(sCheck(d)) {
				return true;
			}
		}
		if(s.length() > 2 && s.substring(s.length() - 2).equals("ly")) {
			s = s.substring(0, s.length() - 2);
			if(sCheck(s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks the hash table if the string is present within.
	 * @param s String to check
	 * @return true if the String is present, otherwise false
	 */
	public boolean sCheck(String s) {
		int idx = calcHash(s);
		int idx2 = calcHash2(s);
		lookup++;
		while(words[idx] != null) {
			probe++;
			if(words[idx].equals(s)) {
				return true;
			}
			idx = idx + idx2;
			idx = idx % HASH_SIZE;
		}
		return false;
	}
	
	/**
	 * Returns the lookup count
	 * @return lookup count
	 */
	public int getLookup() {
		return lookup;
	}
	
	/**
	 * Returns the probe count
	 * @return probe count
	 */
	public int getProbe() {
		return probe;
	}
}
