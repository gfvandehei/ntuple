package plagdetect;

import java.io.*;
import java.lang.reflect.Array;

import plagdetect.PlagurismDetector;

public class Main {
	
	static int defaultn = 3; //sets the default value for tuple length
	
	public static void main(String[] args) {
		PlagurismDetector plag;
		plag = createPlag(args);
		System.out.print(plag.CompareFiles(true));
		System.out.println("%");
	}
	
	/**
	 * This function makes sure all passed arguments are valid
	 * @param args, an array of arguments args[0,1,2] should be filenames, args[3] is an optional tuplelength
	 * @return A PlagurismDetector object created with respect to args[]
	 * notes: I do not check for filename validity, is handled when the files are opened
	 */
	public static PlagurismDetector createPlag(String[] args) {
		//handle too little arguments passed
		String usageString = "usage: [Filename]Synonym file [Filename]File1 [Filename]File2 (Optional)[Number]TupleNumber";
		if(Array.getLength(args) < 3) {
			System.out.println("Too little arguments "+usageString);
			System.exit(0);
		}
		//If there are too many arguments we will only look at first 4, no action needed but prompt user
		else if(Array.getLength(args) > 4) {
			System.out.println("More arguments provided than needed "+usageString);
			try{
				int n = Integer.parseInt(args[3],10);
				return new PlagurismDetector(args[0],args[1],args[2],n);
			}
			catch(NumberFormatException e) {
				System.out.println("Argument TupleNum could not be converted to an integer "+usageString);
				System.exit(0);
			}
		}
		else { //only other option is that length = 3
			return new PlagurismDetector(args[0],args[1],args[2],defaultn); 
		}
		return null;
	
	}
	
}


