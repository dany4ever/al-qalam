package parser.quran.com;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.imageio.ImageIO;



public class TextToImage {

	private static String 			inPath = "input/";
	private static String 			outPath = "output/arabic/";
	
	private static int[]	SurahNumberOfAyats = new int[] { 7,286,200,176,120,165,206,75,129,109,123,111,43,52,99,128,111,110,98,135,
		112,78,118,64,77,227,93,88,69,60,34,30,73,54,45,83,182,88,75,85,54,53,89,59,37,35,38,29,18,45,60,49,62,55,78,96,29,22,24,
		13,14,11,11,18,12,12,30,52,52,44,28,28,20,56,40,31,50,40,46,42,29,19,36,25,22,17,19,26,30,20,15,21,11,8,8,19,5,8,8,11,11,
		8,3,9,5,4,7,3,6,3,5,4,5,6 };
	
	public static void main(String[] args) {
		int i,j;
		String inputLine;
		BufferedImage  arabiTextImage;
	  	
		int imageWidth = 390;
		Font f = new Font("me_quran", Font.LAYOUT_RIGHT_TO_LEFT & Font.PLAIN, 30);
	    
		File inFile = new File (inPath + "quran-uthmani.txt");
		
		FileInputStream 	fstream; 
		BufferedReader 		reader = null;
		BufferedWriter 		writer  = null;
		String				bismillah = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ";
		try {
			
			fstream = new FileInputStream(inFile);
			reader = new BufferedReader(new InputStreamReader (new DataInputStream(fstream)));
		
		}
		catch (Exception e){
			System.out.println("Error: " + e.getMessage());
		}
		
		for (i=1;i<=114;i++) {
			boolean result = (new File(outPath + i)).mkdirs();
			
			try {
				//File outFile = new File(outPath + i + ".txt");
				//writer = new BufferedWriter(new FileWriter(outFile));
				
				int until = SurahNumberOfAyats[i-1];
												
				for (j=0;j<until;j++) {
					inputLine = reader.readLine();
					
					if ( (i != 1 && i != 9) && j == 0 )
						inputLine = inputLine.substring(bismillah.length());

					arabiTextImage = renderTextToImage (f, Color.BLACK, inputLine, imageWidth);
					
					File outputfile = new File(outPath + i + "/" + makeFileName(i,j) + ".gdw");
					ImageIO.write(arabiTextImage, "png", outputfile);
					
					System.out.println("FILE " + i + ":" + (j+1) + "is DONE");
					
					//writer.write(inputLine);
					//writer.newLine();
					//System.out.println(inputLine);
				}
			
				//writer.close();
			}
			catch (Exception e){
				System.out.println("Error: " + e.getMessage());
			}
		}
	}
	
	public static BufferedImage renderTextToImage(Font font, Color textColor, String text, int width){
		final int sidePadding = 5;
    	
        AttributedString attributedString = new AttributedString(text);
        attributedString.addAttribute(TextAttribute.FONT, font);
        attributedString.addAttribute(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);
        AttributedCharacterIterator paragraph = attributedString.getIterator();
       
        FontRenderContext frc =  new FontRenderContext(null, true, false);
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);      
        
        float   drawPosY=0;
        
        //First time around, just determine the height
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(width); // -  rightPadding - leftPadding);
            
            // Move it down
            drawPosY += layout.getAscent() + layout.getDescent() + layout.getLeading();
        }
        
        //drawPosY += bottomPadding;
        
        BufferedImage image = new BufferedImage(width + sidePadding*2,(int) drawPosY, Transparency.BITMASK);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        //graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);       
        //graphics.setFont(font);
        graphics.setColor(textColor);
		
        drawPosY=0;
        lineMeasurer.setPosition(paragraphStart);
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(width - 2*sidePadding);// - rightPadding - leftPadding);
            
            // Move y-coordinate by the ascent of the layout.
            drawPosY += layout.getAscent();
            
           /* Compute pen x position.  If the paragraph is
              right-to-left, we want to align the TextLayouts
              to the right edge of the panel.
            */
            float drawPosX;
            if (layout.isLeftToRight()) {
                drawPosX = 0;
            } else {
                drawPosX = width - layout.getAdvance() - sidePadding;
            }
            
            // Draw the TextLayout at (drawPosX, drawPosY) with paddings;
            layout.draw(graphics, drawPosX, drawPosY);
            
            // Move y-coordinate in preparation for next layout.
            drawPosY += layout.getDescent() + layout.getLeading();
        }
        
        graphics.dispose();
        return image;
    }
	
	private static String makeFileName(int surahNumber, int index) {
		String  SNM, ANM;
		
		if (surahNumber < 10 )
			SNM = "00" + surahNumber;
		else if (surahNumber < 100 )
			SNM = "0"+surahNumber;
		else 
			SNM = ""+surahNumber;
		
		if (index < 10 )
			ANM = "00"+index;
		else if (index < 100)
			ANM = "0"+index;
		else 
			ANM = ""+index;
		
		return SNM + ANM;
	}
}
