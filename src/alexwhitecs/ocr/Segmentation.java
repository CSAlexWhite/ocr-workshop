package alexwhitecs.ocr;

import java.util.Stack;
import java.util.Vector;

public abstract class Segmentation {
		
	public static Vector<OCRImage> segment(OCRImage image){
				
		Vector<OCRImage> characters = extractComponents(detectLines(image));		
		return characters;
	}
	
	/************************ BOXING ALGORITHMS *******************************/
	
	/**
	 * This function extracts the lines of text individually out of an image
	 * and exports them as new OCRImages.
	 * @param image
	 * @return
	 */
	public static Vector<OCRImage> detectLines(OCRImage image){		
		
		int x1, x2, y1 = 0, y2 = image.width, emptyThreshold = 5, cutoff = image.cutoff;		
		Vector<OCRImage> rows = new Vector<OCRImage>();		
		Vector<Integer> xCoords = getVLines(image);			

		x1 = xCoords.remove(0);
		x2 = xCoords.elementAt(0);
		
		if(x1 != x2) 
			rows.add(new OCRImage(selectFrom(image, x1, x2, y1, y2), cutoff));
		
		while(xCoords.size()>0){
	
			x1 = x2;
			x2 = xCoords.remove(0);
			
			if(x2 == x1) continue;
			
			rows.add(new OCRImage(selectFrom(image, x1, x2, y1, y2), cutoff));
		}
		
		for(int i=0; i<rows.size(); i++){
			
			if(isEmpty(rows.elementAt(i), emptyThreshold)) 
				rows.removeElementAt(i);			
		}
		
		return rows;
	}
	
	/**
	 * After the lines have been extracted, this extracts chunks of the line with 
	 * black ink in them and returns a vector of new OCRImages.
	 * @param lines
	 * @return
	 */
	public static Vector<OCRImage> extractComponents(Vector<OCRImage> lines){
		
		int x1 = 0, x2, y1, y2, emptyThreshold = 5, cutoff = lines.get(0).cutoff;
		Vector<OCRImage> components1 = new Vector<OCRImage>();
		Vector<OCRImage> components2 = new Vector<OCRImage>();		
		Vector<Integer> yCoords = new Vector<Integer>();											
		OCRImage component = null;

		for(int i=0; i<lines.size(); i++){
			
			component = new OCRImage(lines.get(i), cutoff);
			yCoords = getHLines(component);
			
			x2 = component.height;			
			y1 = yCoords.remove(0);
			y2 = yCoords.elementAt(0);
			
			if(y1 != y2) 
				components1.add(new OCRImage(selectFrom(component, x1, x2, y1, y2), cutoff));
			
			while(yCoords.size()>0){								
				
				y1 = y2;
				y2 = yCoords.remove(0);
				
				if(y1 == y2) continue;
				
				components1.add(new OCRImage(selectFrom(component, x1, x2, y1, y2), cutoff));
			}
		}
		
		for(int i=0; i<components1.size(); i++){
			
			if(isEmpty(components1.elementAt(i), emptyThreshold)) 				
				components1.removeElementAt(i);
			
		}
		
		for(int i=0; i<components1.size(); i++){
			
			components2.add(removeSpace(components1.elementAt(i)));
		}

		return components1;
	}
	
	/**
	 * A helper function: given an images and the coordinates of opposite corners
	 * of the bounding rectangle, exports an OCRImage of what's inside the box.
	 * @param preimage
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static OCRImage selectFrom(OCRImage preimage, int x1, int x2, int y1, int y2)
		throws ArrayIndexOutOfBoundsException{
				
		int height = Math.abs(x2-x1), width = Math.abs(y2-y1);
		int[][] imageArray = new int[width][height];
		
		int k = 0; 		
		for(int i=y1; i<y2; i++){
			
			int l = 0;
			for(int j=x1; j<x2; j++){
				
				imageArray[k][l] = preimage.monochrome[i][j];						
				l++;		
			}
			
			k++;
		}
		
		try{
		return new OCRImage(imageArray, preimage.cutoff);
		}
		
		catch(IllegalArgumentException iae){}
		
		return preimage;
	}
	
	/**
	 * A test for blankness.
	 * @param input
	 * @param emptyThreshold
	 * @return
	 */
	public static boolean isEmpty(OCRImage input, int emptyThreshold){
		
		int threshold = emptyThreshold;
		input = new OCRImage(input, input.cutoff);
		
		for(int i=0; i<input.width; i++){
			
			for(int j=0; j<input.height; j++){
				
				if(input.monochrome[i][j] == 0){ 
					
					threshold--;
				}				
			}
		}
		
		if(threshold <= 0) return false;
		return true;
	}
	
	/**
	 * Removes unnecessary white area.
	 * @param component
	 * @return
	 */
	public static OCRImage removeSpace(OCRImage component){
		
		Vector<Integer> xCoords = getVLines(component);
				
		for(int i=0; i<xCoords.size(); i++){
	
			//System.out.println("Looking at " + xCoords.get(i));
			if((xCoords.get(i) == 0) || xCoords.get(i) == component.width-1){
				
				//System.out.println("Removed " + xCoords.get(i));
				xCoords.remove(i);
			}
				
		}
		
//		System.out.println("x1: " + xCoords.get(0));
//		System.out.println("x2: " + xCoords.get(xCoords.size()-1)); 
//		System.out.println("y1: " + 0);
//		System.out.println("y2: " + (component.width-1));
		
		try{
		return new OCRImage(selectFrom(	component, 
										0, component.width-1,
										xCoords.get(0), xCoords.get(xCoords.size()-1)), 
										component.cutoff);
		}
		catch(ArrayIndexOutOfBoundsException aioobe){}
		
		return component;
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
		emptyLines.add(0);
		emptyLines.add(input.width-1);
		int hasBlack;
		
		for(int i=0; i<input.width; i++){	  // Find the empty lines which extend
			hasBlack = 0;
			
			for(int j=0; j<input.height; j++)								
				if(input.monochrome[i][j] < input.cutoff) hasBlack++;				
				
			if(hasBlack==0) emptyLines.add(i);  // otherwise push that line index			
		}
		
		Vector<Integer> edges = new Vector<Integer>();
		
		edges.add(emptyLines.get(0));
		edges.add(emptyLines.get(emptyLines.size()-1));
		
		for(int i=0; i<emptyLines.size()-1; i++){
			
			if(emptyLines.elementAt(i+1)-emptyLines.elementAt(i) == 1) continue;
			else edges.add(emptyLines.elementAt(i));
		}
		
		for(int i=emptyLines.size()-1; i>0; i--){
			
			if(emptyLines.elementAt(i)-emptyLines.elementAt(i-1) == 1) continue;
			else edges.add(emptyLines.elementAt(i));
		}
		
		edges.sort(new IntegerComparator());
		
		/* REMOVE EDGES THAT ARE TOO CLOSE TOGETHER? */		
		int threshold = 0;
		
		int temp1 = 0, temp2 = 0;
		for(int i=0; i<edges.size()-1; i++){
			
			temp1 = edges.get(i);
			temp2 = edges.get(i+1);
			
			//System.out.println("temp1: " + temp1 + ", temp2: " + temp2);
			
			if(Math.abs(temp1 - temp2) < threshold){
				
				edges.remove(i);
				
				try{edges.remove(i+1);}
				catch(ArrayIndexOutOfBoundsException aioobe){}
								
				//System.out.println("Removed " + temp1 + " and " + temp2);
			}
		}
		
		return edges;
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
		
		emptyLines.add(0);
		emptyLines.add(input.height-1);
		
		for(int i=0; i<input.height; i++){	  // Find the empty lines which extend
			hasBlack = 0;
			
			for(int j=0; j<input.width; j++)  // CHECK Memory cacheing issues
				
				if(input.monochrome[j][i] < input.cutoff) hasBlack++;

			if(hasBlack==0) emptyLines.add(i);
		}
		
		Vector<Integer> edges = new Vector<Integer>();
		
		for(int i=0; i<emptyLines.size()-1; i++){
			
			if(emptyLines.elementAt(i+1)-emptyLines.elementAt(i) < 15) continue;
			else edges.add(emptyLines.elementAt(i));
		}
		
		for(int i=emptyLines.size()-1; i>0; i--){
			
			if(emptyLines.elementAt(i)-emptyLines.elementAt(i-1) < 15) continue;
			else edges.add(emptyLines.elementAt(i));
		}
		
		edges.sort(new IntegerComparator());
		return edges;
	}
	
	/** 
	 * Extracts the closest edge to the character.
	 * @param input
	 * @return
	 */
	public static Stack<Integer> getEdges(Stack<Integer> input){
		
		if(input.isEmpty()) return new Stack<Integer>();
		
		Stack<Integer> edges = new Stack<Integer>();
		Integer temp1, temp2;
		
		edges.push(temp1 = input.pop());		
		while(!input.empty()){
			
			temp2 = input.pop();				
			if(Math.abs(temp2 - temp1) >= 1) edges.push(temp2);			
			
			if(input.empty()) break;
			
			temp1 = input.pop();			
			if(Math.abs(temp2 - temp1) >= 1) edges.push(temp1);			
		}
		
		return edges;
	}
	
	/**************************** DISPLAY / DEBUG UTILITIES *******************/	
	
	/**
	 * A method to help display the vertical lines created during segmentation, 
	 * mainly for debugging.
	 * @param image
	 * @return
	 */
	public static OCRImage chopVertically(OCRImage image){
		
		Vector<Integer> lines = getHLines(image);
		
		OCRImage output1 = new OCRImage(image, image.cutoff);
		
		for(int i=0; i<image.width; i++){
			
			if(lines.contains(i)) for(int j=0; j<image.height; j++) 
				output1.monochrome[i][j] = 0;
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
	public static OCRImage chopHorizontally(OCRImage image){
		
		Vector<Integer> lines = getVLines(image);
		
		OCRImage output1 = new OCRImage(image, image.cutoff);
		
		for(int i=0; i<image.height; i++){
			
			if(lines.contains(i)) for(int j=0; j<image.width; j++) 
				output1.monochrome[j][i] = 0;
		}
		
		OCRImage output2 = new OCRImage(output1.monochrome, output1.cutoff);
		
		return output2;
	}
	
	/**************************** EXPERIMENTS *********************************/
	
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
}
