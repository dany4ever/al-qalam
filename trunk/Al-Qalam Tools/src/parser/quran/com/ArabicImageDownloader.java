package parser.quran.com;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;

public class ArabicImageDownloader {
	
	private static String 			inputPath= "http://c0022506.cdn1.cloudfiles.rackspacecloud.com/";
	private static String 			outputPath = "output/arabic/";
	
	private static int[]	SurahNumberOfAyats = new int[] { 7,286,200,176,120,165,206,75,129,109,123,111,43,52,99,128,111,110,98,135,
		112,78,118,64,77,227,93,88,69,60,34,30,73,54,45,83,182,88,75,85,54,53,89,59,37,35,38,29,18,45,60,49,62,55,78,96,29,22,24,
		13,14,11,11,18,12,12,30,52,52,44,28,28,20,56,40,31,50,40,46,42,29,19,36,25,22,17,19,26,30,20,15,21,11,8,8,19,5,8,8,11,11,
		8,3,9,5,4,7,3,6,3,5,4,5,6 };
	
	static BufferedImage downloadImage(String path) { 
		BufferedImage source = null;
		
		try {
			URL url = new URL(path);
			source = ImageIO.read(url);
		} catch (IOException ex) {
			System.out.println("can not read : " + path);
			System.out.println(ex.getMessage());
		} 
		
		//int color = source.getRGB(0, 0);

        //Image image = makeColorTransparent(source, new Color(color));
        	
		//return imageToBufferedImage(image);
		
		return source;
	}
	
	private static BufferedImage imageToBufferedImage(Image image) {

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return bufferedImage;

    }
	
	
	public static void main (String[] args) {
		int i,j;
		
		for (i=1;i<=114;i++) {
			outputPath = outputPath;
			boolean result = (new File(outputPath + i)).mkdirs();
			
			for (j=1;j<=SurahNumberOfAyats[i-1];j++) {
				String remoteFile = inputPath + i + "_" + j + ".png";
				File localFile = new File(outputPath + i + "/" + j + ".png");

				try {
					ImageIO.write(downloadImage(remoteFile), "png", localFile);
					System.out.println("FILE " + i + ":" + j + "is DONE");
				}
				catch (IOException e) {
					System.out.println("Error : " + e.getMessage());
				}
			}

		}
	}
	
	 public static Image makeColorTransparent(BufferedImage im, final Color color) {
	        ImageFilter filter = new RGBImageFilter() {

	                // the color we are looking for... Alpha bits are set to opaque
	                public int markerRGB = color.getRGB() | 0xFF000000;

	                public final int filterRGB(int x, int y, int rgb) {
	                        if ((rgb | 0xFF000000) == markerRGB) {
	                                // Mark the alpha bits as zero - transparent
	                                return 0x00FFFFFF & rgb;
	                        } else {
	                                // nothing to do
	                                return rgb;
	                        }
	                }
	        };

	        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
	        return Toolkit.getDefaultToolkit().createImage(ip);
	    }
   
}
