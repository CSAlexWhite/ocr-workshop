package alexwhitecs.ocr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OCRImage {
	
	public BufferedImage source;
	
	public int width;
	public int height;
	public int cutoff;;
	
	public Color[][] colorArray;
	public int[][][] rawData;
	
	public int[][] grayscale;
	public int[][] monochrome;
	
	public OCRImage(String filename, int cutoff){
		
		try{ source = ImageIO.read(new File(filename));} 
		
		/* TODO: Maybe display a popup here? */
		catch (IOException ioe){ System.out.println("Couldn't read file.");}
		
		width = source.getWidth();
		height = source.getHeight();
		
		colorArray = Scanning.imageToColor(source, width, height);	
		rawData = Scanning.expandColor(colorArray, width, height);
		grayscale = Scanning.dataToGray(rawData, width, height);
		threshold(cutoff);
			
	}
	
	public OCRImage(BufferedImage preimage, int cutoff){
		
		width = source.getWidth();
		height = source.getHeight();
		
		colorArray = Scanning.imageToColor(source, width, height);	
		rawData = Scanning.expandColor(colorArray, width, height);
		grayscale = Scanning.dataToGray(rawData, width, height);
		threshold(cutoff);
		
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
			
		colorArray = Scanning.imageToColor(source, width, height);	
		rawData = Scanning.expandColor(colorArray, width, height);
		grayscale = Scanning.dataToGray(rawData, width, height);
		threshold(cutoff);
	}
	
	public OCRImage(OCRImage preimage, int cutoff){
		
		width = preimage.width;
		height = preimage.height;	
		this.cutoff = cutoff;
				
		//color = Scanning.imageToColor(preimage.source, width, height);
		
//		color = new Color[1000][1000];
//		for(int i=0; i<1000; i++) color[i] = new Color[1000];
//		
//		for(int i=0; i<250; i++){
//			for(int j=0; i<250; j++){
//				color[i][j] = preimage.color[i][j];
//			}
//		}
		
		colorArray = preimage.colorArray;
				
		rawData = Scanning.expandColor(colorArray, width, height);
		grayscale = Scanning.dataToGray(rawData, width, height);
		threshold(cutoff);
			
		source = Scanning.colorToImage(
				Scanning.dataToColor(monochrome, width, height), 
												width, height); 
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
	

}
