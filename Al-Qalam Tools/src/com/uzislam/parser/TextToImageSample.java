package com.uzislam.parser;

import javax.imageio.ImageIO;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
* Created by IntelliJ IDEA.
* User: Nurmuhammad
* Date: 22.11.2010
* Time: 14:09:16
* To change this template use File | Settings | File Templates.
*/

public class TextToImageSample {
	// The LineBreakMeasurer used to line-break the paragraph.
    private static LineBreakMeasurer lineMeasurer;
        
    public static void main(String[] args) throws IOException {
		String s [] = {
		"بَرَآءَةٌۭ مِّنَ ٱللَّهِ وَرَسُولِهِۦٓ إِلَى ٱلَّذِينَ عَٰهَدتُّم مِّنَ ٱلْمُشْرِكِينَ"		
		};
		
		BufferedImage image;
		
		//_PDMS_IslamicFont,me_quran
		Font font = new Font("me_quran", Font.PLAIN, 34);
		
		int i = 0;
		//for (int i=0; i<s.length; i++) {
			
		    image = renderTextToImage (font, Color.BLACK, s[0], 400);
			
			File outputfile = new File("c:\\" + i + ".png");
			
			System.out.println("FILE "+ i+ "is DONE");
			
			ImageIO.write(image, "png", outputfile);
		//}
	}
    
    public static BufferedImage renderTextToImage(Font font, Color textColor, String text, int width){
		final int rightPadding = 15, bottomPadding = 3;
    	
        AttributedString attributedString = new AttributedString(text);
        attributedString.addAttribute(TextAttribute.FONT, font);
        AttributedCharacterIterator paragraph = attributedString.getIterator();
        
        
        FontRenderContext frc =  new FontRenderContext(null, true, false);
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);      
        
        float   drawPosY=0;
        
        //First time around, just determine the height
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(width);
            
            // Move it down
            drawPosY += layout.getAscent() + layout.getDescent() + layout.getLeading();
        }
        
        drawPosY += bottomPadding;
        
        BufferedImage image = new BufferedImage(width,(int) drawPosY, Transparency.BITMASK);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);       
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
		
        drawPosY=0;
        lineMeasurer.setPosition(paragraphStart);
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(width - rightPadding);
            
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
                drawPosX = width - layout.getAdvance();
            }
            
            // Draw the TextLayout at (drawPosX, drawPosY) with paddings;
            layout.draw(graphics, drawPosX -  rightPadding, drawPosY);
            
            // Move y-coordinate in preparation for next layout.
            drawPosY += layout.getDescent() + layout.getLeading();
        }
        
        //graphics.dispose();
        return image;
    }
   
}

