package alexwhitecs.fx;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class Scanning {

	static String filename;
	static LoadImage preimage;
	static BufferedImage subject;
	static BufferedImage outImage;
	
	static InputStreamReader sreader = new InputStreamReader(System.in);
	static BufferedReader reader = new BufferedReader(sreader);
	
	static int[][][] data;
	static Color[][] colorArray;
	static int width;
	static int height;
	
//	public static BufferedImage Execute(String filename, int threshold) throws IOException{
//		
//		BufferedImage out = init(threshold);	
//		
//		return out;
//		
//		//printData(data);		
//	}
	
//	public static int[][][] init(String filename) throws IOException{
//		
//		subject = ImageIO.read(new File(filename));//preimage.img;
//		width = subject.getWidth(); //ERROR HERE!!!!!
//		height = subject.getHeight();
//		data = buildPixelArray(subject);
//		//colorArray = dataToBlackAndWhite(threshold, data);
//		//colorArray = dataToGrayscale(data);
//		//colorArray = dataToColor(data);
//		//outImage = colorToImage(colorArray);
//		
//		return data;
//	}
	
	public static int[][][] init(String filename) throws IOException{
		
		subject = ImageIO.read(new File(filename));//preimage.img;
		width = subject.getWidth(); //ERROR HERE!!!!!
		height = subject.getHeight();
		data = buildPixelArray(subject);
		//colorArray = dataToBlackAndWhite(threshold, data);
		//colorArray = dataToGrayscale(data);
		//colorArray = dataToColor(data);
		//outImage = colorToImage(colorArray);
		
		return data;
	}
	
	public static BufferedImage toBuffered(Color[][] inArray){
		
		BufferedImage toReturn = colorToImage(inArray);
		return toReturn;
	}
	
	public static void threshold(int value){
		
		colorArray = dataToBlackAndWhite(value, data);
		//colorArray = dataToGrayscale(data);
		//colorArray = dataToColor(data);
		outImage = colorToImage(colorArray);
	}
	
	public static int[][][] buildPixelArray(BufferedImage target){
		
		int[][] outData1 = new int[width][height];				
		int[][][] outData2 = new int[width][height][4];
		
		Color color;
		
		for(int i=0; i< width; i++){
			for(int j=0; j<height; j++){
				
				outData1[i][j] = target.getRGB(i, j);	
			}
		}
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				
//				(float)outData2[i][j] = new Color(outData1[i][j]).getRGBComponents((float)outData1[i][j]);
				
				outData2[i][j][0] = new Color(outData1[i][j]).getRed();
				outData2[i][j][1] = new Color(outData1[i][j]).getGreen();
				outData2[i][j][2] = new Color(outData1[i][j]).getBlue();
				outData2[i][j][3] = new Color(outData1[i][j]).getAlpha();
			}
		}
		
		return outData2;	
	}
	
	public static Color[][] dataToGrayscale(int[][][] data){
		
		Color[][] toReturn = new Color[width][height];
		for(int i=0; i<width; i++) toReturn[i] = new Color[height];
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				
				toReturn[i][j] = 
											
						new Color(	( ( data[i][j][0] + data[i][j][1] + data[i][j][2] ) / 3 ),
									( ( data[i][j][0] + data[i][j][1] + data[i][j][2] ) / 3 ),
									( ( data[i][j][0] + data[i][j][1] + data[i][j][2] ) / 3 ), 
									data[i][j][3]);
			}
		}
		
		return toReturn;
	}
	
	public static int[][] colorToGrayscale(int[][][] data){
			
		int[][] toReturn = new int[width][height];
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				
				toReturn[i][j] = ( ( data[i][j][0] + data[i][j][1] + data[i][j][2] ) / 3 );
			}
		}
		
		return toReturn;
	}
	
	public static Color[][] graysToBlackAndWhite(int threshold, int[][] data){
		
		Color[][] toReturn = new Color[width][height];
		for(int i=0; i<width; i++) toReturn[i] = new Color[height];
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				
				
				
				if(data[i][j] < threshold) toReturn[i][j] = 
						new Color(0, 0, 0, 255);
				
				else toReturn[i][j] = 										
						new Color(255, 255, 255, 255);
			}
		}
		
		return toReturn;	
	}
	
	public static Color[][] dataToBlackAndWhite(int threshold, int[][][] data){
		
		int gray = 0;
		
		Color[][] toReturn = new Color[width][height];
		for(int i=0; i<width; i++) toReturn[i] = new Color[height];
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				
				gray = ( ( data[i][j][0] + data[i][j][1] + data[i][j][2] ) / 3 );
				
				if(gray < threshold) gray = 0;
				else gray = 255;
				
				toReturn[i][j] = 
											
						new Color(gray, gray, gray, data[i][j][3]);
			}
		}
		
		return toReturn;
	}
	
	public static Color[][] dataToColor(int[][][] data){
		
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
	
	public static BufferedImage colorToImage(Color[][] inData){
		
		Color[][] image = inData;

		// Initialize BufferedImage, assuming Color[][] is already properly populated.
		BufferedImage bufferedImage = new BufferedImage(image.length, image[0].length,
		        BufferedImage.TYPE_INT_RGB);

		// Set each pixel of the BufferedImage to the color from the Color[][].
		for (int x = 0; x < image.length; x++) {
		    for (int y = 0; y < image[x].length; y++) {
		        bufferedImage.setRGB(x, y, image[x][y].getRGB());
		    }
		}
		
		return bufferedImage;
	}
	
	public static void printData(int[][][] inData){
		
		for(int i=0; i<4; i++){
			
			if(i==0) System.out.println("\n\tRED\n");
			if(i==1) System.out.println("\n\tGREEN\n");
			if(i==2) System.out.println("\n\tBLUE\n");
			if(i==3) System.out.println("\n\tALPHA\n");
			
			for(int j=0; j<height; j++){
				for(int k=0; k<width; k++){
					
					System.out.print((inData[k][j][i]) + "\t");
				}			
				System.out.println();
			}
		}
	}
	
	public static void writeFile(String filename, String filetype, BufferedImage image){
		
		try {
		    File outputfile = new File(filename);
		    ImageIO.write(image, filetype, outputfile);
		} catch (IOException e) {}
	}
}