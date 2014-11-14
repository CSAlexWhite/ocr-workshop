package alexwhitecs.ocr;

import java.util.Stack;
import java.util.Vector;

public abstract class Segmentation {
	
	public static OCRImage horizontals(OCRImage image){
		
		Stack<Integer> lines = getLines(image);
		
		OCRImage output1 = new OCRImage(image, image.cutoff);
		
		for(int i=0; i<image.height; i++){
			
			if(lines.contains(i)) for(int j=0; j<image.width; j++) output1.monochrome[j][i] = 0;
		}
		
		OCRImage output2 = new OCRImage(output1.monochrome, output1.cutoff);
		
		return output2;
	}
	
	public static Stack<Integer> getLines(OCRImage input){
			
		Stack<Integer> emptyLines = new Stack<Integer>();
		int hasBlack;
		
		for(int i=0; i<input.height; i++){	  // Find the empty lines which extend
			hasBlack = 0;
			
			for(int j=0; j<input.width; j++){ // all the way across the page
				
				//System.out.print(input.monochrome[j][i]);
				
				if(input.monochrome[j][i] < input.cutoff){ // if the line has black
					
					hasBlack++;
				}
			}
			
			System.out.println(hasBlack);
			
			if(hasBlack==0){
				
				emptyLines.push(i);  // otherwise push that line index
				//System.out.println(i);
			}
		}
				
		return getEdges(emptyLines);	
	}
	
	/** 
	 * Not that useful a function, but would look cool if I could finish it
	 * @param input
	 * @return
	 */
	public static Stack<Integer> getEdges(Stack<Integer> input){
		
		int threshold = 1;
		
		System.out.println(input.size());
		
		Stack<Integer> edges = new Stack<Integer>();
		
		// first, find the ranges of the groups, need to get the first/last of each group
		
		Integer temp1, temp2;
		temp1 = input.pop();
		edges.push(temp1);
		
		while(!input.empty()){
			
			temp2 = input.pop();
				
			if( Math.abs(temp2 - temp1) >= threshold){ 
				
				edges.push(temp2);
			}
			
			if(input.empty()) break;
			temp1 = input.pop();
			
			if( Math.abs(temp2 - temp1) >= threshold){ 
				
				edges.push(temp1);
			}
		}
		
		return edges;
	}
	
	public static OCRImage getBlocks(OCRImage input, int iterations, int innerLoops){
		
		int[][] testData = null;
		OCRImage output = new OCRImage(input,input.cutoff);
		
		for(int i=0; i<iterations; i++){
			
			testData = reduce(output.monochrome, output.width, output.height, innerLoops);
			output = new OCRImage(testData, input.cutoff);
					
			testData = expand(output.monochrome, output.width, output.height, innerLoops);
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
