/* 
  * Brief Description: 
  * This is the class that will write the new player's information into the file for the leaderboard.
  */ 

import java.io.*;

public class TextWriter {
	
	public static void saveToFile(String file_name, String input) throws IOException {
		File file = new File(file_name);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		out.println(input);
		out.close();
	}
	
	
}