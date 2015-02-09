package alexwhitecs.ocr;

import java.util.List;
import java.util.Stack;
import java.util.Vector;

public abstract class Segmentation {
	
	public static OCRImage getGrid(OCRImage image){
		
		OCRImage temp1 = new OCRImage(image, image.cutoff);
		temp1 = horizontals(temp1);
		
		OCRImage temp2 = new OCRImage(image, image.cutoff);
		temp2 = verticals(temp2);
		
		return overlay(temp1, temp2);
	}
	
	public static Vector<OCRImage> chop(OCRImage image){
		
		Vector<OCRImage> rows = new Vector<OCRImage>();
		Vector<OCRImage> letters = new Vector<OCRImage>();
		
		Vector<Integer> xCoords = new Vector<Integer>();
		Vector<Integer> yCoords = new Vector<Integer>();
		Integer x1, x2, y1, y2;
		
								
//		yCoords = getHLines(image);
		xCoords = getVLines(image);
		
		//System.out.println(image.width + " " + image.height);
		
		while(xCoords.size()>1){
			
			y1 = 0;
			y2 = image.width;
	
			x1 = xCoords.remove(0);
			x2 = xCoords.elementAt(0);
			
			rows.add(selectFrom(image, x1, x2, y1, y2));
		}
		
//		for(int i=0; i<rows.size(); i++){
//			
//			yCoords = getHLines(rows.elementAt(i));
//			
//			System.out.println(yCoords.size());
//			
//			while(yCoords.size()>1){
//				
//				x1 = 0;
//				x2 = image.height;
//		
//				y1 = xCoords.remove(0);
//				y2 = xCoords.elementAt(0);
//				
//				letters.add(selectFrom(image, x1, x2, y1, y2));
//			}
//		}
			
		System.out.println(rows.size());
		return rows;
	}
	
	public static OCRImage selectFrom(OCRImage preimage, int x1, int x2, int y1, int y2){
		
		int height = Math.abs(x2-x1);
		int width = Math.abs(y2-y1);
		int[][] imageArray = new int[width][height];

//		System.out.println("width = " + width);
//		System.out.println("height = " + height);
		
		int k = 0; 		
		for(int i=y1; i<y2; i++){
			
			int l = 0;
			for(int j=x1; j<x2; j++){
				
				imageArray[k][l] = preimage.monochrome[i][j];						
				l++;		
			}
			
			k++;
		}
				
		return new OCRImage(imageArray, preimage.cutoff);
	}
	
	/**
	 * A method to help display the vertical lines created during segmentation, 
	 * mainly for debugging.
	 * @param image
	 * @return
	 */
	public static OCRImage verticals(OCRImage image){
		
		Vector<Integer> lines = getHLines(image);
		
		OCRImage output1 = new OCRImage(image, image.cutoff);
		
		for(int i=0; i<image.width; i++){
			
			if(lines.contains(i)) for(int j=0; j<image.height; j++) output1.monochrome[i][j] = 0;
		}
		
		OCRImage output2 = new OCRImage(output1.monochrome, output1.cutoff);
		
		return output2;
	}
	
	/**
	 * A method to help display the horizontal lines created during segmentation,
	 * mainly for debugging.
	 * @param image
	 * @return
	 */
	public static OCRImage horizontals(OCRImage image){
		
		Vector<Integer> lines = getVLines(image);
		
		OCRImage output1 = new OCRImage(image, image.cutoff);
		
		for(int i=0; i<image.height; i++){
			
			if(lines.contains(i)) for(int j=0; j<image.width; j++) output1.monochrome[j][i] = 0;
		}
		
		OCRImage output2 = new OCRImage(output1.monochrome, output1.cutoff);
		
		return output2;
	}
	
	
	/**
	 * Takes an OCRImage input and for each horizontal line:
	 * (1) Counts black pixels
	 * (2) If count is zero, adds that row to the stack
	 * @param input
	 * @return
	 */
	public static Vector<Integer> getHLines(OCRImage input){
			
		Vector<Integer> emptyLines = new Vector<Integer>();
		int hasBlack;
		
		for(int i=0; i<input.width; i++){	  // Find the empty lines which extend
			hasBlack = 0;
			
			for(int j=0; j<input.height; j++){ // all the way across the page
				
				//System.out.print(input.monochrome[j][i]);
				
				if(input.monochrome[i][j] < input.cutoff){ // if the line has black
					
					hasBlack++;
				}
			}
			
			//System.out.println(hasBlack);			
			if(hasBlack==0){
				
				emptyLines.add(i);  // otherwise push that line index
				//System.out.println(i);
			}
		}
		
		Vector<Integer> edges = new Vector<Integer>();
		
		for(int i=0; i<emptyLines.size()-1; i++){
			
			if(emptyLines.elementAt(i+1)-emptyLines.elementAt(i) == 1) continue;
			else edges.add(emptyLines.elementAt(i));
		}
		
		for(int i=emptyLines.size()-1; i>0; i--){
			
			if(emptyLines.elementAt(i)-emptyLines.elementAt(i-1) == 1) continue;
			else edges.add(emptyLines.elementAt(i));
		}
		
//		for(int i=0; i<edges.size(); i++) System.out.print(edges.elementAt(i) + " ");
//		System.out.println();
		
		edges.sort(new IntegerComparator());
		
//		for(int i=0; i<edges.size(); i++) System.out.print(edges.elementAt(i) + " ");
//		System.out.println();
		
		return edges;
		//return getEdges(emptyLines);	
	}
	
	/**
	 * Takes an OCRImage input and for each vertical line:
	 * (1) Counts black pixels
	 * (2) If count is zero, adds that row to the stack
	 * @param input
	 * @return
	 */
	public static Vector<Integer> getVLines(OCRImage input){
		
		Vector<Integer> emptyLines = new Vector<Integer>();
		int hasBlack;
		
		for(int i=0; i<input.height; i++){	  // Find the empty lines which extend
			hasBlack = 0;
			
			for(int j=0; j<input.width; j++){ // all the way across the page
				
				//System.out.print(input.monochrome[j][i]);
				
				if(input.monochrome[j][i] < input.cutoff){ // if the line has black
					
					hasBlack++;
				}
			}
			
			//System.out.println(hasBlack);
			
			if(hasBlack==0){
				
				emptyLines.add(i);  // otherwise push that line index
				//System.out.println(i);
			}
		}
		
		Vector<Integer> edges = new Vector<Integer>();
		
		for(int i=0; i<emptyLines.size()-1; i++){
			
			if(emptyLines.elementAt(i+1)-emptyLines.elementAt(i) == 1) continue;
			else edges.add(emptyLines.elementAt(i));
		}
		
		for(int i=emptyLines.size()-1; i>0; i--){
			
			if(emptyLines.elementAt(i)-emptyLines.elementAt(i-1) == 1) continue;
			else edges.add(emptyLines.elementAt(i));
		}
		
		
//		for(int i=0; i<edges.size(); i++) System.out.print(edges.elementAt(i) + " ");
//		System.out.println();
		
		edges.sort(new IntegerComparator());
		
//		for(int i=0; i<edges.size(); i++) System.out.print(edges.elementAt(i) + " ");
//		System.out.println();
		
		
		return edges;
		//return getEdges(emptyLines);	
	}
	
	/** 
	 * Not that useful a function, but would look cool if I could finish it
	 * @param input
	 * @return
	 */
	public static Stack<Integer> getEdges(Stack<Integer> input){
		
		if(input.isEmpty()) return new Stack<Integer>();
		
		int threshold = 1;
		
		//System.out.println(input.size());
		
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
	
	public static OCRImage overlay(OCRImage input1, OCRImage input2){
				
		for(int i=0; i<input1.width; i++){			
			for(int j=0; j<input1.height; j++){
				
				if(input1.monochrome[i][j] < input2.monochrome[i][j]) input1.monochrome[i][j] = input2.monochrome[i][j];
				
			}
		}
		
		return input2; 
	}
}
