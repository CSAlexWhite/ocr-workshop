package alexwhitecs.ocr;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Scanning {
	
	/** Takes a BufferedImage and its dimensions and returns an array of Colors */
	public static Color[][] imageToColor(BufferedImage input, int width, int height){
		
		Color[][] output = new Color[width][height];
		for(int i=0; i<width; i++) output[i] = new Color[height]; 	
		
		for(int i=0; i< width; i++){
			for(int j=0; j<height; j++){
				
				output[i][j] = new Color(input.getRGB(i, j));	
			}
		}
		
		return output;
	}
	
	/** Takes an array of Colors and its dimensions and returns a BufferedImage */
	public static BufferedImage colorToImage(Color[][] inData, int width, int height){
		
		Color[][] image = inData;

		BufferedImage bufferedImage = new BufferedImage(image.length, image[0].length,
		        BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < image.length; x++) {
		    for (int y = 0; y < image[x].length; y++) {
		        bufferedImage.setRGB(x, y, image[x][y].getRGB());
		    }
		}
		
		return bufferedImage;
	}
	
	/** Tales an array of Colors and returns a separated 3D Array of its 
	 * constituent RGBA components */
	public static int[][][] expandColor(Color[][] input, int width, int height){
						
		int[][][] output = new int[width][height][4];
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
								
				output[i][j][0] = input[i][j].getRed();
				output[i][j][1] = input[i][j].getGreen();
				output[i][j][2] = input[i][j].getBlue();
				output[i][j][3] = input[i][j].getAlpha();
			}
		}
		
		return output;	
	}
	
	/** Takes a separated 3D RGBA array and returns it to a 2D Color array */
	public static Color[][] reverseExpansion(int[][][] data, int width, int height){
		
		Color[][] toReturn = new Color[width][height];
		for(int i=0; i<width; i++) toReturn[i] = new Color[height];
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				
				toReturn[i][j] = 
						
						new Color(	data[i][j][0], 
									data[i][j][1], 
									data[i][j][2], 
									data[i][j][3]);
			}
		}
		
		return toReturn;
	}
	
	/** Takes a separated 3D RGBA array, performs grayscaling and returns a 
	 * grayscale array */
	public static int[][] dataToGray(int[][][] data, int width, int height){
			
		int[][] toReturn = new int[width][height];
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				
				toReturn[i][j] = ( ( data[i][j][0] + data[i][j][1] + data[i][j][2] ) / 3 );
			}
		}
		
		return toReturn;
	}
	
	/** Takes a grayscale array and thresholds it to black and white */
	public static int[][] grayToMono(int cutoff, int[][] input, int width, int height){
		
		int[][] output = new int[width][height];
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				
				if(input[i][j] < cutoff) output[i][j] = 0;			
				else output[i][j] = 255;										
			}
		}
		
		return output;
	}
	
	/** Takes some 2D array and converts it to a Color array */
	public static Color[][] dataToColor(int[][] input, int width, int height){
		
		int value = 0;
		
		Color[][] output = new Color[width][height];
		for(int i=0; i<width; i++) output[i] = new Color[height];
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){

				value = input[i][j];
				output[i][j] = new Color(value, value, value, 255);
			}
		}
		
		return output;	
	}
}