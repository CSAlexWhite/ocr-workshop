package alexwhitecs.ocr;

import java.awt.Color;
import java.awt.image.BufferedImage;

public abstract class Scanning {
	

	public static Color[][] imageToColor(BufferedImage input, int width, int height){
		
		Color[][] output = new Color[width][height];
		for(int i=0; i<width; i++) output[i] = new Color[height]; 	
		
		for(int i=0; i< width; i++){
			for(int j=0; j<height; j++){
				
				output[i][j] = new Color(input.getRGB(i, j));	
			}
		}
		
		return output;
	}
	
	public static BufferedImage colorToImage(Color[][] inData, int width, int height){
		
		Color[][] image = inData;

		BufferedImage bufferedImage = new BufferedImage(image.length, image[0].length,
		        BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < image.length; x++) {
		    for (int y = 0; y < image[x].length; y++) {
		        bufferedImage.setRGB(x, y, image[x][y].getRGB());
		    }
		}
		
		return bufferedImage;
	}
	
	public static int[][][] expandColor(Color[][] input, int width, int height){
						
		int[][][] output = new int[width][height][4];
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
								
				output[i][j][0] = input[i][j].getRed();
				output[i][j][1] = input[i][j].getGreen();
				output[i][j][2] = input[i][j].getBlue();
				output[i][j][3] = input[i][j].getAlpha();
			}
		}
		
		return output;	
	}
	
	public static Color[][] reverseExpansion(int[][][] data, int width, int height){
		
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
	
	public static int[][] dataToGray(int[][][] data, int width, int height){
			
		int[][] toReturn = new int[width][height];
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				
				toReturn[i][j] = ( ( data[i][j][0] + data[i][j][1] + data[i][j][2] ) / 3 );
			}
		}
		
		return toReturn;
	}
	
	public static int[][] grayToMono(int cutoff, int[][] input, int width, int height){
		
		int[][] output = new int[width][height];
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				
				if(input[i][j] < cutoff) output[i][j] = 0;			
				else output[i][j] = 255;										
			}
		}
		
		return output;
	}
	
	
	public static Color[][] dataToColor(int[][] input, int width, int height){
		
		int value = 0;
		
		Color[][] output = new Color[width][height];
		for(int i=0; i<width; i++) output[i] = new Color[height];
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){

				value = input[i][j];
				output[i][j] = new Color(value, value, value, 255);
			}
		}
		
		return output;	
	}
	
	public static int[][] denoise(int[][] input, int width, int height){
		
		int[][] nbinary = new int[width][height];
		int[][] dbinary = nbinary;
		//int[][] output = new int[width][height];
		
		int[][] output = new int[width][height];
		
//		for(int i=0; i<width/2; i++){
//			for(int j=0; j<height/2; j++){
//				
//				//if(input[i][j] < cutoff) 
//				if(j%2==1) output[i][j] = 255;	
//				else output[i][j] = 0;
//				//else output[i][j] = 255;										
//			}
//		}
		
		//return output;
		// change to binary
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				
				if(input[i][j] == 255) nbinary[i][j] = 0;
				nbinary[i][j] = 1;
			}
		}
		
		// do something
		for(int i=1; i<width-1; i++){
			for(int j=1; j<height-1; j++){
				
				output[i][j] = localPotential(input/*nbinary*/, dbinary, width, height, i, j, 0, 1, 2.1);
			}
		}
	
		// return to pixel valued
//		for(int i=0; i<width; i++){
//			for(int j=0; j<height; j++){
//				
//				if(dbinary[i][j] == 1) output[i][j] = 0;
//				output[i][j] = 255;
//			}
//		}
//		
		return output;
	}
	
	private static int localPotential(int[][] noisy_img, int[][] clean_img, int width, int height, int row, int col, double h, double beta, double eta){
		
//		double n1 = 0.0, p1 = 0.0;
//		
//		if(row==1 && col==1){ //UL
//		    n1 = (h*-1) - beta*(-1*clean_img[row+1][col] - 1*clean_img[row][col+1]) - eta*(-1*noisy_img[row][col]);
//		    p1 = (h*1) - beta*(1*clean_img[row+1][col] + 1*clean_img[row][col+1]) - eta*(1*noisy_img[row][col]);
//		}	    
//			    
//		if(row==1 && col==width-1){ //UR
//		    n1 = (h*-1) - beta*(-1*clean_img[row+1][col] - 1*clean_img[row][col-1]) - eta*(-1*noisy_img[row][col]);
//		    p1 = (h*1) - beta*(1*clean_img[row+1][col] + 1*clean_img[row][col-1]) - eta*(1*noisy_img[row][col]);
//		}
//		    
//		if(row==height-1 && col==1){ //LL
//		    n1=(h*-1)-beta*(-1*clean_img[row-1][col]+-1*clean_img[row][col+1])-eta*(-1*noisy_img[row][col]);
//		    p1=(h*1)-beta*(1*clean_img[row-1][col]+1*clean_img[row][col+1])-eta*(1*noisy_img[row][col]);
//		}
//		    
//		if(row==height-1 && col==width-1){ //LR
//		    n1=(h*-1)-beta*(-1*clean_img[row-1][col]+-1*clean_img[row][col-1])-eta*(-1*noisy_img[row][col]);
//		    p1=(h*1)-beta*(1*clean_img[row-1][col]+1*clean_img[row][col-1])-eta*(1*noisy_img[row][col]);
//		}
//		    
//		if(row!=1 && row!=height-1 && col==1){ //L
//		    n1=(h*-1)-beta*(-1*clean_img[row][col+1]+-1*clean_img[row-1][col]+-1*clean_img[row+1][col])-eta*(-1*noisy_img[row][col]);
//		    p1=(h*1)-beta*(1*clean_img[row][col+1]+1*clean_img[row-1][col]+1*clean_img[row+1][col])-eta*(1*noisy_img[row][col]);
//		}
//		    
//		if(row!=1 && row!=height-1 && col==width-1){ //R
//		    n1=(h*-1)-beta*(-1*clean_img[row][col-1]+-1*clean_img[row+1][col]+-1*clean_img[row-1][col])-eta*(-1*noisy_img[row][col]);
//		    p1=(h*1)-beta*(1*clean_img[row][col-1]+1*clean_img[row+1][col]+1*clean_img[row-1][col])-eta*(1*noisy_img[row][col]);
//		}
//		    
//		if(row==1 && col!=1 && col!=width-1){ //U
//		    n1=(h*-1)-beta*(-1*clean_img[row][col+1]+-1*clean_img[row][col-1]+-1*clean_img[row+1][col])-eta*(-1*noisy_img[row][col]);
//		    p1=(h*1)-beta*(1*clean_img[row][col+1]+1*clean_img[row][col-1]+1*clean_img[row+1][col])-eta*(1*noisy_img[row][col]);
//		}
//		    
//		if(row==height-1 && col!=1 && col!=width-1){ //D
//		    n1=(h*-1)-beta*(-1*clean_img[row-1][col]+-1*clean_img[row][col-1]+-1*clean_img[row][col+1])-eta*(-1*noisy_img[row][col]);
//		    p1=(h*1)-beta*(1*clean_img[row-1][col]+1*clean_img[row][col-1]+1*clean_img[row][col+1])-eta*(1*noisy_img[row][col]);
//		}
//		    
//		else{ //Body
//		    n1=(h*-1)-beta*(-1*clean_img[row][col-1]+-1*clean_img[row-1][col]+-1*clean_img[row+1][col]+-1*clean_img[row][col+1])-eta*(-1*noisy_img[row][col]);
//		    p1=(h*1)-beta*(1*clean_img[row][col-1]+1*clean_img[row-1][col]+1*clean_img[row+1][col]+1*clean_img[row][col+1])-eta*(1*noisy_img[row][col]);
//		}
//		  
//		int choice = 0;
//		double loc_pot_minus_1 = Math.exp(n1);//(int) Randoms.nextExp(Math.abs(n1)+.1);
//		double loc_pot_plus_1 = Math.exp(p1);//(int) Randoms.nextExp(Math.abs(p1)+.1);
//	
//		System.out.println(n1 + "\t" + p1);
//		System.out.println(loc_pot_plus_1 + "\t" + loc_pot_minus_1);
//		if(loc_pot_plus_1 > loc_pot_minus_1)
//		    choice=1;
//		else
//		    choice=0;
		
		//blur
		double value = (noisy_img[row][col] + noisy_img[row][col-1] + noisy_img[row-1][col] + noisy_img[row+1][col] + noisy_img[row][col+1]) / 5;
		System.out.println(value);
		return (int) Math.round(value);
				//Math.floor(value);
		
//		return choice;
	}
}