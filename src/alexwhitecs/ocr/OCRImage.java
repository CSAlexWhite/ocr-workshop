package alexwhitecs.ocr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OCRImage {
	
	public BufferedImage source;
	public Color[][] colorArray;	
	
	public int[][][] rawData;	
	public int[][] grayscale, monochrome;
	public int width, height, cutoff;
	
	/******************************* CONSTRUCTORS *****************************/
	
	public OCRImage(String filename, int cutoff){
		
		try{ source = ImageIO.read(new File(filename));} 
		catch (IOException ioe){ System.out.println("Couldn't read file.");}		
		setArrays();			
	}
	
	public OCRImage(BufferedImage preimage, int cutoff){
		
		setArrays();		
		source = Scanning.colorToImage(
				 Scanning.dataToColor(monochrome, width, height), 
												width, height);
	}
	
	public OCRImage(int[][] inputData, int cutoff){
		
		width = inputData.length;
		height = inputData[0].length;		
		source = Scanning.colorToImage(
					Scanning.dataToColor(inputData, width, height), 
													width, height);			
		setArrays();
	}
	
	public OCRImage(OCRImage preimage, int cutoff){
		
		width = preimage.width;
		height = preimage.height;	
		this.cutoff = cutoff;
		
		colorArray = new Color[width][height];	// MAKES SURE TO REFERENCES ARE
		for(int i=0; i<width; i++){				// REMOVEd
			for(int j=0; j<height; j++) 
				colorArray[i][j] = new Color(preimage.colorArray[i][j].getRGB());
		}
							
		rawData = Scanning.expandColor(colorArray, width, height);
		grayscale = Scanning.dataToGray(rawData, width, height);
		threshold(cutoff);
			
		source = Scanning.colorToImage(
				Scanning.dataToColor(monochrome, width, height), 
												width, height); 
	}
	
	/****************************** I/O Operations ****************************/
	
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
	
	/********************************** GETTERS *******************************/
	

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
	
	/********************************** SETTERS *******************************/
	
	private void setArrays(){
		
		width = source.getWidth();
		height = source.getHeight();
		
		colorArray = Scanning.imageToColor(source, width, height);	
		rawData = Scanning.expandColor(colorArray, width, height);
		grayscale = Scanning.dataToGray(rawData, width, height);
		threshold(cutoff);
	}
	
	
	public void threshold(int cutoff){
		
		monochrome = Scanning.grayToMono(cutoff, grayscale, width, height);
	}

}
