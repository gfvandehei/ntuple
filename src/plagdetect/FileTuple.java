package plagdetect;

import java.util.*;
import plagdetect.PlagurismDetector;

public class FileTuple {
	private ArrayList<String> tuple;
	private int hash = 0;
	
	/**
	 * Constructs tuple objects
	 * @param words An array of representing the strings within the tuple
	 * Notes: I chose an ArrayList as my data representation due to the ordering, In my representation
	 * the location of Strings/Synonyms in a tuple must be the same for the tuples to be equal 
	 */
	public FileTuple(ArrayList<String> words) {
		this.tuple = (ArrayList<String>)words.clone();
	}
	
	/**
	 * Computes the hashcode of a FileTuple object,
	 * Note: Computing the hashcode allows us easily compare two tuples by checking if it exists in a hashed object
	 * Note: The hashcode computation takes into account word location, as well as synonyms, 
	 * Note: Examples: (Run,to, lake).hashcode() == (Jog, to, pond).hashcode() but (Lake,to,run).hashcode() != (Run, to Lake) 
	 *@return An integer hashcode relating to the FileTuple object
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		for(String i: this.tuple) {
			if(PlagurismDetector.Synonyms.containsKey(i)) {
				//if the current string has synonym's, then the synonym set of all synonyms will be the same
				//since the original synonym set is an arrayList the hash of each equal synonym set will be the same
				hash=30*hash+PlagurismDetector.Synonyms.get(i).hashCode();
			}
			else {
				//if there are no synonym's just add the hashcode of the string
				hash=30*hash+i.hashCode();
			}
		}
		this.hash = hash;
		return hash;
	}
	
	/**
	 * Determines Equality between current and passed object
	 * @param obj: Object, object to be compared to this
	 * Note: Same rules as hashcode apply, synonym words are regarded as the same
	 */
	@Override 
	public boolean equals(Object obj){
		if(obj==null) {
			return false;
		}
		else if(!(obj instanceof FileTuple)) {
			return false;
		}
		
		if(((FileTuple)obj).tuple.equals(this.tuple)) { //if it is a direct match speed it up
			return true;
		}
		for(int i=0;i<this.tuple.size();i++) {
			//is there a synonym for the current string
			if(PlagurismDetector.Synonyms.containsKey(this.tuple.get(i))) {
				//is the other tuples same location string a synonym
				if(PlagurismDetector.Synonyms.get(this.tuple.get(i)).contains(((FileTuple)obj).tuple.get(i))) {
					continue;
				}
				else {
					//if it is not a synonym or the same that means at least one location word has a different meaning
					return false;
				}
			}
			//if there is no synonym the words must be exactly the same or the tuple is not the same
			if(this.tuple.get(i).hashCode() != ((FileTuple)obj).tuple.get(i).hashCode()) { //had to use hashcode here because seemingly equal arrays were not
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Provides an easy way to print the FileTuple object
	 */
	@Override
	public String toString() {
		return tuple.toString();
	}
	
	//Getter functions
	/**
	 * Allows view of strings within FileTuple without exposing mutable objects
	 * @return a copy of the ArrayList this.tuple
	 */
	public ArrayList<String> getWords() {
		return (ArrayList<String>) this.tuple.clone(); //prevents user from changing original tuple
	}
	
	/**
	 * allows the returning of the current hashcode without recomputing, will be zero if hash has not been computed
	 * @return the hashcode of current object
	 */
	public int getHash() {
		return this.hash;
	}
}
