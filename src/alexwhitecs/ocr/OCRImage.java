package alexwhitecs.ocr;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class OCRImage {
	
	public BufferedImage source;
	public Color[][] colorArray;	
	
	public int[][][] rawData;	
	public int[][] grayscale, monochrome, binary;
	public int width, height, cutoff;
	
	/******************************* CONSTRUCTORS *****************************/
	
	public OCRImage(String filename, int cutoff){
		
		try{ source = ImageIO.read(new File(filename));} 
		catch (IOException ioe){ System.out.println("Couldn't read file.");}		
		
        width = source.getWidth();
        height = source.getHeight();
        
        colorArray = Scanning.imageToColor(source, width, height);    
        rawData = Scanning.expandColor(colorArray, width, height);
        grayscale = Scanning.dataToGray(rawData, width, height);
        threshold(cutoff);
        createBinary();
	}
	
	public OCRImage(File file, int cutoff){
		
		try{ source = ImageIO.read(file);} 
		catch (IOException ioe){ System.out.println("Couldn't read file.");}		
		
        width = source.getWidth();
        height = source.getHeight();
        
        colorArray = Scanning.imageToColor(source, width, height);    
        rawData = Scanning.expandColor(colorArray, width, height);
        grayscale = Scanning.dataToGray(rawData, width, height);
        threshold(cutoff);
        createBinary();
	}
	
	public OCRImage(BufferedImage preimage, int cutoff){
		
		setArrays();		
		source = Scanning.colorToImage(
				 Scanning.dataToColor(monochrome, width, height), 
												width, height);
        createBinary();
	}
	
	public OCRImage(int[][] inputData, int cutoff){
		
		width = inputData.length;
		height = inputData[0].length;		
		source = Scanning.colorToImage(
					Scanning.dataToColor(inputData, width, height), 
													width, height);			
		setArrays();
        createBinary();
	}
	
	public OCRImage(OCRImage preimage, int cutoff){
		
		width = preimage.width;
		height = preimage.height;	
		this.cutoff = cutoff;
		
//		colorArray = preimage.colorArray;
		
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
		
		this.cutoff = cutoff;
		monochrome = Scanning.grayToMono(cutoff, grayscale, width, height);
	}
	
	public void changeThreshold(int cutoff){
		
		this.cutoff = cutoff;
		monochrome = Scanning.grayToMono(cutoff, grayscale, width, height);
		reload();
	}

	/********************************* UTILITIES ******************************/
	
	public void reload(){
		
		source = Scanning.colorToImage(
				 Scanning.dataToColor(monochrome, width, height), 
												  width, height);			
	}
	
	public void recalcMono(){
		
		setArrays();
	}
	
	public void printImage(){
		
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(source)));
		frame.pack();
		frame.setVisible(true);		
	}
	
	public void createBinary(){
		
		binary = new int[width][height];
		
		for(int i=0; i<width; i++){			
			for(int j=0; j<height; j++){
				
				if(monochrome[i][j] > 0) binary[i][j] = 0;
				if(monochrome[i][j] <=0) binary[i][j] = 1;
				//System.out.print(binary[i][j]);
			}		
			//System.out.println();
		}
	}
}
