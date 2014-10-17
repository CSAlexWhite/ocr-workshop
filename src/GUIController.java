

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import alexwhitecs.ocr.Scanning;

public class GUIController {
	
	@FXML ScrollPane pictureframe1;
	@FXML ScrollPane pictureframe2;
	@FXML TextArea thresholdOutInt;
	@FXML ProgressBar thresholdOutVis;
	
	Integer count;
	String imagename;
	
	int[][][] data;
	int[][] grays;
	Color[][] colorArray;
	BufferedImage rawImage;
	
	public GUIController(){
		
		thresholdOutInt = new TextArea();
		count = 50;
		imagename = null;
		rawImage = null;
		
		try {data = Scanning.init("letters.jpg");} 
		catch (IOException e) {e.printStackTrace();}
		
		grays = Scanning.colorToGrayscale(data);
	}
	
	@FXML public void displayImage1(){
			
		colorArray = Scanning.graysToBlackAndWhite(count += 5, grays);
		rawImage = Scanning.toBuffered(colorArray);
		
		thresholdOutInt.setText(count.toString());
		thresholdOutVis.setProgress(((double) count) / 255.0);
		
		getImage(rawImage);
	
		/* SET IT AS AN IMAGE VIEW */
		ImageView picture = new ImageView(wr);
		
		pictureframe1.setContent(picture);
	}
	
	@FXML public void displayImage2(){
		
		if(count%3 == 0) imagename = "colors.jpg";
		if(count%3 == 1) imagename = "letters.jpg";
		if(count%3 == 2) imagename = "sample.jpg";
		count++;
		
		/* LOAD THE IMAGE */
		Image image = new Image(imagename);
		
		/* SET IT AS AN IMAGE VIEW */
		ImageView picture = new ImageView();
		picture.setImage(image);
		
		pictureframe2.setContent(picture);
	}
	
	public WritableImage getImage(BufferedImage input){
		
		WritableImage wr = null;
	    if (input != null) {
	        wr = new WritableImage(input.getWidth(), input.getHeight());
	        PixelWriter pw = wr.getPixelWriter();
	        for (int x = 0; x < input.getWidth(); x++) {
	            for (int y = 0; y < input.getHeight(); y++) {
	                pw.setArgb(x, y, input.getRGB(x, y));
	            }
	        }
	    }
		
	    return wr;
	}	
}
