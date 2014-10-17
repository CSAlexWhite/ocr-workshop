package alexwhitecs.ocr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OCRImage {
	
	BufferedImage source;
	
	int width;
	int height;
	int cutoff;;
	
	Color[][] color;
	int[][][] rawData;
	
	int[][] grayscale;
	int[][] monochrome;
	
	public OCRImage(String filename){
		
		try{ source = ImageIO.read(new File(filename));} 
		
		/* TODO MAYBE WANT TO DISPLAY A POPUP HERE */
		catch (IOException ioe){ System.out.println("Couldn't read file.");}
		
		width = source.getWidth();
		height = source.getHeight();
		
		color = Scanning.imageToColor(source, width, height);	
		rawData = Scanning.expandColor(color, width, height);
		grayscale = Scanning.dataToGray(rawData, width, height);
			
	}
		
	public void threshold(int cutoff){
		
		monochrome = Scanning.grayToMono(cutoff, grayscale, width, height);
	}
	

	public BufferedImage getColorImage(){
		
		return source;
	}
	
	public BufferedImage getGrayscaleImage(){
	
		return 	Scanning.colorToImage(
				Scanning.dataToColor(grayscale, width, height), 
												width, height);
	}
	
	public BufferedImage getMonochromeImage(){
	
		return 	Scanning.colorToImage(
				Scanning.dataToColor(monochrome, width, height), 
												 width, height); 
	}
	
	public void printData(){
		
		for(int i=0; i<4; i++){
			
			if(i==0) System.out.println("\n\tRED\n");
			if(i==1) System.out.println("\n\tGREEN\n");
			if(i==2) System.out.println("\n\tBLUE\n");
			if(i==3) System.out.println("\n\tALPHA\n");
			
			for(int j=0; j<height; j++){
				for(int k=0; k<width; k++){
					
					System.out.print((rawData[k][j][i]) + "\t");
				}			
				System.out.println();
			}
		}
	}
	
	public static void writeFile(
			String filename, String filetype, BufferedImage image){
		
		try {
		    File outputfile = new File(filename);
		    ImageIO.write(image, filetype, outputfile);
		} catch (IOException e) {}
	}
}
