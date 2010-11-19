import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MakeSeparateFiles {

	private static String 			outPath = "output/russian/";
	private static String 			inPath = "input/";
	
	private static int[]	SurahNumberOfAyats = new int[] { 7,286,200,176,120,165,206,75,129,109,123,111,43,52,99,128,111,110,98,135,
			112,78,118,64,77,227,93,88,69,60,34,30,73,54,45,83,182,88,75,85,54,53,89,59,37,35,38,29,18,45,60,49,62,55,78,96,29,22,24,
			13,14,11,11,18,12,12,30,52,52,44,28,28,20,56,40,31,50,40,46,42,29,19,36,25,22,17,19,26,30,20,15,21,11,8,8,19,5,8,8,11,11,
			8,3,9,5,4,7,3,6,3,5,4,5,6 };
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String 	inputLine;
		int 	i = 1, j = 1;
		File inFile = new File (inPath + "ru.osmanov.txt");
		
		FileInputStream 	fstream; 
		BufferedReader 		reader = null;
		BufferedWriter 		writer  = null;

		try {
			
			fstream = new FileInputStream(inFile);
			reader = new BufferedReader(new InputStreamReader (new DataInputStream(fstream)));
		
		}
		catch (Exception e){
			System.out.println("Error: " + e.getMessage());
		}
		
		for (i=1;i<=114;i++) {
			try {
				File outFile = new File(outPath + i + ".txt");
				writer = new BufferedWriter(new FileWriter(outFile));
			
				for (j=1;j<=SurahNumberOfAyats[i-1];j++) {
					inputLine = reader.readLine();
					writer.write(inputLine);
					writer.newLine();
					System.out.println(inputLine);
				}
			
				writer.close();
			}
			catch (Exception e){
				System.out.println("Error: " + e.getMessage());
			}
		}
	}
}
