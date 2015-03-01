package alexwhitecs.ocr;

import java.util.Vector;

public class OCRCharacter {
	
	Vector<Integer> xCoords, yCoords;
	int[] featureVector, featureSize;
	int width, height;
	
	public OCRCharacter(OCRImage input){
		
		featureVector = new int[25];
		
		width = input.width;
		height = input.height;
		
		featureSize = new int[2];
		featureSize[0] = width/5;
		featureSize[1] = height/5;
		
		
		xCoords = new Vector<Integer>();
		yCoords = new Vector<Integer>();
		
		xCoords.add(0);
		yCoords.add(0);
		
		int tempX = 0;
		for(int i=0; i<5; i++) xCoords.add(tempX += featureSize[0]);
		xCoords.add(width);
		
		int tempY = 0;
		for(int i=0; i<5; i++) yCoords.add(tempY += featureSize[1]);
		yCoords.add(height);
		
		for(int i=0; i<5; i++){
			
			for(int j=0; j<5; j++){
							
				for(int x=xCoords.get(i); x<xCoords.get(i+1); x++){
					
					for(int y=yCoords.get(j); y<yCoords.get(j+1); y++){
						
						featureVector[i+j] += input.monochrome[i][j];
					}
				}
			}
		}
		
		for(int i=0; i<25; i++){
			
			System.out.print(featureVector[i] + " ");
		}
	}	
}
