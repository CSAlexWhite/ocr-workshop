package alexwhitecs.fx;

import java.awt.image.BufferedImage;

import alexwhitecs.ocr.OCRImage;
import alexwhitecs.ocr.Segmentation;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class GUIController {
	
	@FXML ScrollPane pictureframe1;
	@FXML ScrollPane pictureframe2;
	@FXML TextArea thresholdOutInt;
	@FXML ProgressBar thresholdOutVis;
	
	Integer count;
	String imagename;
	
	int[][] testdata;
	
	OCRImage image1;
	OCRImage image2;
	OCRImage image3;
	OCRImage image4;
	OCRImage image5;
	
	public GUIController(){
		
		thresholdOutInt = new TextArea();
		count = 50;
		imagename = null;
		
		image1 = new OCRImage("letters.jpg", count);
	}
	
	@FXML public void displayImage1(){
		
		image1.threshold(count += 10);
		image2 = image1;
		
		thresholdOutInt.setText(count.toString());
		thresholdOutVis.setProgress(((double) count) / 255.0);
		
		WritableImage wr = getImage(image1.getMonochromeImage());
		
		/* SET IT AS AN IMAGE VIEW */
		ImageView picture = new ImageView(wr);	
		pictureframe1.setContent(picture);
	}
	
	@FXML public void displayImage2(){
		
//		System.out.println(image2.width + "\t" + image2.height);
		testdata = Segmentation.reduce(image2.monochrome, image2.width, image2.height,4);
		image3 = new OCRImage(testdata, count);
//		System.out.println(image3.width + "\t" + image3.height);
//		if(image3.monochrome == null) System.out.println("Null bitch");
		
//		testdata = Segmentation.reduce(image3.monochrome, image3.width, image3.height);
//		image4 = new OCRImage(testdata, count);
//		
//		testdata = Segmentation.reduce(image4.monochrome, image4.width, image4.height);
//		image5 = new OCRImage(testdata, count);
		
		WritableImage wr = getImage(image3.source);
		
		/* SET IT AS AN IMAGE VIEW */ 
		ImageView picture = new ImageView(wr);	
		pictureframe2.setContent(picture);
	}
	
	/*************************** PRIVATE METHODS ***********************/
	
	private WritableImage getImage(BufferedImage input){
		
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
