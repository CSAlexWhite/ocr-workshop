package alexwhitecs.ocr;

import java.util.Vector;

public abstract class Segmentation {
	
	
	public static OCRImage getBlocks(OCRImage input, int width, int height, int iterations, int innerLoops){
		
		int[][] testData = null;
		OCRImage output = input;
		
		for(int i=0; i<iterations; i++){
			
			testData = Segmentation.reduce(output.monochrome, output.width, output.height, innerLoops);
			output = new OCRImage(testData, input.cutoff);
					
			testData = Segmentation.expand(output.monochrome, output.width, output.height, innerLoops);
			output = new OCRImage(testData, input.cutoff);
		}	
		
		return output;	
	}
	
	public static int[][] reduce(int[][] input, int width, int height, int iterations){
		
		int[][]  output = null;
			
			for(int k=0; k<iterations; k++){
				output = new int[width/2][height/2];	
				
				for(int i=0; i<width-1; i+=2){
					for(int j=0; j<height-1; j+=2){

						output[i/2][j/2] = 	
						(input[i][j] + input[i+1][j] + input[i][j+1] + input[i+1][j+1])/4;				
					}	
				}
				
				width /=2; height /=2; input = output;			
			}
			
		return output;
	}
	
	public static int[][] expand(int[][] input, int width, int height, int iterations){
		
		int[][] output = null;
		
			for(int k=0; k<iterations; k++){
				output = new int[width*2][height*2];	
				
				for(int i=0; i<width; i++){
					for(int j=0; j<height; j++){
						
						output[i*2][j*2] 	= input[i][j];
						output[i*2+1][j*2] 	= input[i][j];
						output[i*2][j*2+1]	= input[i][j];
						output[i*2+1][j*2+1]= input[i][j];														
					}	
				}
				
				width *=2; height *=2; input = output;			
			}
	
		return output;
	}
	
	/* OLD PART */
	
	int[][] imageArray;	
	int X, Y, XMin, YMin, XMax, YMax;
	
	Vector <Integer> Xs;
	Vector <Integer> Ys;
	
	public Segmentation(int[][] image){
		
		imageArray = image;
		
		XMin = 0;
		YMin = 0;
		
		Xs.addElement(XMax = imageArray.length-1);
		Ys.addElement(YMax = imageArray[0].length-1);
	}
	
	public void down(){
		
		X = XMax;
		Y = 0;
		
		while(Y < YMax){
			
			if(imageArray[X][Y++] == 255){
				
				Xs.addElement(XMax = X);
				right();
			}
			
			X--;
			
			if(X == XMin) return;
		}				
	}
	
	public void right(){
		
		Y = YMax;
		X = 0;
		
		while (X < XMax){
			
			if(imageArray[X++][Y] == 255){
				
				Ys.addElement(YMax = Y);
				down();
			}
			
			Y--;
			
			if(Y == YMin) return;
		}	
	}	
}
