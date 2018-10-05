package plagdetect;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class FileHandler {
	/**
	 * Creates an array containing lines in a file
	 * @return ArrayList<String> containing all lines in a file 
	 * @throws IOException
	 */
	public ArrayList<String> readFileLines(String filename, boolean lower) throws IOException{
		
		FileReader fr = new FileReader(filename);
		BufferedReader filereader = new BufferedReader(fr);
		
		ArrayList<String> fileLines = new ArrayList<String>();
		
		String line;
		while((line=filereader.readLine()) != null) {
			fileLines.add(line.toLowerCase());
		}
		//System.out.println(synMap);
		filereader.close();
		
		return fileLines;
	}
	
	/**
	 * Creates an array containing words in a file
	 * @return ArrayList<String> containing all words in a file 
	 * @throws IOException
	 */
	public ArrayList<String> readFileWords(String filename, boolean lower) throws IOException{
		
		Scanner sc = new Scanner(new File(filename));
		ArrayList<String> fileLines = new ArrayList<String>();
		
		String line;
		while(sc.hasNext()) {
			fileLines.add(sc.next().toLowerCase());
		}
		//System.out.println(synMap);
		sc.close();
		
		return fileLines;
	}
	
}
