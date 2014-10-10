package alexwhitecs.fx;

import java.util.Vector;

public class Segmentation {
	
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
