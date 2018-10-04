package plagdetect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import plagdetect.FileTuple;

public class PlagurismDetector {
	
	public static Map<String,ArrayList<String>> Synonyms; //A Map to store the synonyms in, this allows us to use in equals operator
	private Integer n;
	private HashMap<FileTuple,Integer> file1;
	private HashMap<FileTuple,Integer> file2;
	
	/**
	 * Constructor for the PlagurismDetector object
	 * @param synfile:String a file containing synonyms as rows
	 * @param file1:String a file to have the plagurism level computed of
	 * @param file2:String a file to have the plagurism level of file 1 computed against
	 * @param n:Integer the size of a tuple
	 */
	public PlagurismDetector(String synfile, String file1, String file2, Integer n) {
		//if any argument is null we should not create
		if(synfile == null || file1 == null || file2 == null || n == null) {
			System.out.println("Not all arguments are initialized");
			System.exit(0);
		}
		this.n = n;
		this.Synonyms = this.CreateSynMap(synfile);
		this.file1 = this.getFileTuples(file1);
		this.file2 = this.getFileTuples(file2);
	}
	
	/**
	 * A Wrapper to compare the tuples of files loaded in
	 */
	public int CompareFiles(boolean duplicateFlag) {
		return this.CompareTuples(this.file1, this.file2, duplicateFlag);
	}
	
	/**
	 * Find the percentage of tuples from file1 which are the same as from file2
	 * @return the percentage of file1, that was found in file2
	 * Note: Say file 1 contains (asd, gfs)x2 and file 2 contains (asd,gfs)x1 only one of two of the tuples will count as plagurized
	 * Note: Comparing to an empty file will give 0 percent plagurized, it does not make sense for 2 empties to give 100% plagurization
	 */
	public int CompareTuples(HashMap<FileTuple,Integer> file1, HashMap<FileTuple,Integer> file2,boolean duplicateFlag) {
		float sameCounter = 0;
		float tupleSum = 0;
		for(FileTuple i: file1.keySet()) {
			tupleSum += file1.get(i);
			if(file2.containsKey(i)) {
				//gives two options on plagurism checking
				if(duplicateFlag) {
					sameCounter+=file1.get(i); //if a single instance is found all tuples in 1 will be counted as plagurized
				}
				else {
					sameCounter+= Math.min(file1.get(i), file2.get(i)); //only the amount of instances of tuples in 2 will be counted as plagurized in 1
				}
				
			}
		}
		//do some math to compare overall level of 
		return (int)(sameCounter/tupleSum*100);
	}
	
	
	/**
	 * Used to create a map of synonyms, all words in a row in "synfile" are considered a synonym
	 * @param synFile, the name of the file to read synonyms from
	 * @return a HashMap<String,HashSet<String>> containing words as keys and a set of synonyms as values
	 */
	private Map<String,ArrayList<String>> CreateSynMap(String synFile){
		Map<String,ArrayList<String>> synMap = new HashMap<String,ArrayList<String>>();
		
		try {
			FileReader fr = new FileReader(synFile);
			BufferedReader filereader = new BufferedReader(fr);
			
			String line;
			while((line=filereader.readLine()) != null) {
				String[] words = line.split(" ");//split line into seperate words
				for(int i=0;i<Array.getLength(words);i++) {
					words[i] = words[i].toLowerCase(); //eliminate potential errors from case
				}
				ArrayList<String> wordsSet = new ArrayList(Arrays.asList(words));
				
				//create a new map entry for each word and add synonym list to it
				//using lists allows to hash according to values instead of location
				Iterator<String> i = wordsSet.iterator();
				while(i.hasNext()) {
					synMap.put(i.next(), wordsSet);//add word to map, and add Array of synonyms as value 
				}
			}
			//System.out.println(synMap);
			filereader.close();
		}
		catch(IOException e) { //Buffered reader has failed, we must exit
			System.out.println("Could not read "+synFile+" please check file name");
			System.exit(0);
		}
		return synMap;
	}
	
	/**
	 * Creates an Array of Arrays of Strings, where the inner arrays represent
	 * @param fileName, the name of the file to generate tuples from
	 * @return an ArrayList of tuples of length n
	 */
	private HashMap<FileTuple,Integer> getFileTuples(String fileName){
		HashMap<FileTuple,Integer> tuples = new HashMap<FileTuple,Integer>();
		try {
			Scanner sc = new Scanner(new File(fileName));
			
			//populate an array with all words from file
			ArrayList<String> filearr = new ArrayList<String>();
			while(sc.hasNext()) {
				filearr.add(sc.next().toLowerCase()); //prevent errors from case differences
			}
			//create tuples from file array, and add to return ArrayList
			for(int i=0;i<filearr.size()-this.n+1;i++) {
				ArrayList<String> tuple = new ArrayList<String>();
				//make a array of length n
				for(int u=0;u<this.n;u++) {
					tuple.add(filearr.get(i+u));
				}
				//create tuple out of array
				FileTuple curTuple = new FileTuple(tuple);
				//if the tuple already exists increment a counter for it to keep track of time it appears
				if(tuples.containsKey(curTuple)) {
					int oldvalue = tuples.get(curTuple);
					tuples.replace(curTuple, oldvalue+1);
				}
				else {//take care of case where tuple does not exist, so we must initialize key and counter to 1
					tuples.put(curTuple,1);
				}
			}
			//System.out.println(tuples);
			sc.close();
		}
		catch(IOException e) { //will be reached if file could not be read
			System.out.println("Could not read file "+fileName);
			System.out.println(e);
			System.exit(0);
		}
		
		return tuples;
	}
}
