package alexwhitecs.ocr;

import java.util.Vector;

public abstract class Segmentation {

//	public static int[][] reduce(int[][] in, int width, int height){
//		
//		Integer w = width, h = height;
//		System.out.println("width = " + w);
//		System.out.println("height = " + h);
//		int[][] input = in, output = null;
//		System.out.print("h = " + w);
//		for(int k=0; k<3; k++){
//			
//			//output = new int[w][h];
//			output = new int[w/2][h/2];	
//			//output = new int[w =(int) Math.ceil((w)/2)][h = (int) Math.ceil((h)/2)];
//			//output = new int[w/=2][h/=2];
//			
//			
//			System.out.print("\nh = " + h);
//			
//			for(int i=0; i<w; i+=2){
//				
//				System.out.print("h = " + w);
//				System.out.println(i + " ");
//				for(int j=0; j<h; j+=2){
//					
//					
//					System.out.print("h = " + w);
//					System.out.print(j + " ");
//					output[i/2][j/2] = 	
//					(input[i][j] + input[i+1][j] + input[i][j+1] + input[i+1][j+1])/4;				
//				}	
//			}
//			
//			w = w/2 + 1;
//			h = h/2 + 1;	
//			
//			input = output;				
//		}
//				
//		return output;
//	}
	
	public static int[][] reduce(int[][] input, int width, int height, int iterations){
		
		int[][]  output = null;
		//System.out.print("h = " + height);
			
			//output = new int[w][h];
			
			//output = new int[w =(int) Math.ceil((w)/2)][h = (int) Math.ceil((h)/2)];
			//output = new int[w/=2][h/=2];
			
			for(int k=0; k<iterations; k++){
			//System.out.print("\nh ! " + height);
				//if(k==0) 
					output = new int[width/2][height/2];	
				//else output = new int[width][height];
				
				for(int i=0; i<width-1; i+=2){
					
					//System.out.print("h = " + height);  // h = 5000
					//System.out.println(i + " ");
					for(int j=0; j<height-1; j+=2){
						
						
						//System.out.print("h = " + height);
						//System.out.print(j + " ");
						output[i/2][j/2] = 	
						(input[i][j] + input[i+1][j] + input[i][j+1] + input[i+1][j+1])/4;				
					}	
				}
				
				width /=2;
				height /=2;	
				
				input = output;
				
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
