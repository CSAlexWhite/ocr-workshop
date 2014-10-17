package alexwhitecs.ocr;

public abstract class Segmentation {

	public static int[][] reduction(int[][] input, int width, int height){
		
		int[][] output = new int[width/2][height/2];		
		
		for(int i=0; i<width/2; i+=2){
			for(int j=0; j<height/2; j+=2){
				
				output[i/2][j/2] = 	
				(input[i][j] + input[i+1][j] + input[i][j+1] + input[i+1][j+1])/2;
				
			}	
		}	
		
		return output;
	}
}
