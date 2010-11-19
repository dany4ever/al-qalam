package parser.quran.com;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.htmlparser.*;
import org.htmlparser.filters.*;
import org.htmlparser.beans.*;
import org.htmlparser.util.*;

public class Parser {

	
	private static String 			defaultURL = "http://beta.globalquran.com/output.php";
	private static String 			defaultPath = "output/russian";
	private static String			url;
	
	public static int[]	SurahNumberOfAyats = new int[] {7,286,200,176,120,165,206,75,129,109,123,111,43,52,99,128,111,110,98,135,
		112,78,118,64,77,227,93,88,69,60,34,30,73,54,45,83,182,88,75,85,54,53,89,59,37,35,38,29,18,45,60,49,62,55,78,96,29,22,24,
		13,14,11,11,18,12,12,30,52,52,44,28,28,20,56,40,31,50,40,46,42,29,19,36,25,22,17,19,26,30,20,15,21,11,8,8,19,5,8,8,11,11,
		8,3,9,5,4,7,3,6,3,5,4,5,6};

	
	public static void main(String args[]) {
		String 	inputLine;
		int 	i = 2, j = 2;
		//for (i=1;i<=114;i++) {
		//	for (j=1;j<=SurahNumberOfAyats[i-1];j++) {
				try {
					url = defaultURL + "?s=" + i + "&sA=" + j + "&35";
					
					TagNameFilter filter0 = new TagNameFilter ();
			        filter0.setName ("div");
			        
			        HasAttributeFilter filter1 = new HasAttributeFilter ();
			        filter1.setAttributeName ("class");
			        filter1.setAttributeValue ("ayah");
			        
			        NodeFilter[] array0 = new NodeFilter[2];
			  
			        array0[0] = filter0;
			        array0[1] = filter1;
			        AndFilter filter2 = new AndFilter ();
			        filter2.setPredicates (array0);
			        NodeFilter[] array1 = new NodeFilter[1];
			        array1[0] = filter2;
			       
			        FilterBean bean = new FilterBean ();
			        bean.setFilters (array1);
			      
			        bean.setURL (url);
			        
			        NodeList list = bean.getNodes();
			        
			        //File outFile = new File(i+".txt");
					
					//BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
					
					//inputLine = list.elementAt(0).toPlainTextString();
					
					//System.out.println(inputLine);
					
			        for (int m=1;m<list.size();m++) {
			        	
			        	inputLine = list.elementAt(m).toPlainTextString();       	
			        	
			        	System.out.println(inputLine);
			        	
				        //writer.write(inputLine);
				        
			        	//writer.write(list.elementAt(j).toPlainTextString());
				        
				        //writer.newLine();
			        }
	
			        //writer.close();
			        
			        System.out.println("File "+i+" is DONE");
			        
				}
				catch (Exception e)
				{
					System.out.println("Error: " + e.getMessage());
				}
				
			//} // end for j
		
		//} // end for i
	
	} // end for main

}
