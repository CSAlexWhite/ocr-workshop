package alexwhitecs.fx;

import java.awt.image.BufferedImage;

import alexwhitecs.ocr.OCRImage;
import alexwhitecs.ocr.Segmentation;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class GUIController {
	
	@FXML ScrollPane pictureframe1;
	@FXML ScrollPane pictureframe2;
	@FXML TextArea thresholdOutInt;
	@FXML ProgressBar thresholdOutVis;
	
	int updown = 0;
	Integer count;
	String imagename;
	
	int[][] testdata;
	
	OCRImage tempImage;
	
	OCRImage image1;
	OCRImage image2;
	OCRImage image3;
	OCRImage image4;
	
	public GUIController(){
		
		thresholdOutInt = new TextArea();
		count = 200;
		imagename = null;
		
		image1 = new OCRImage("letters.jpg", count);
	}
	
	@FXML public void displayImage1(){
		
		image1.threshold(count += 1);
		image2 = image1;
		
		System.out.println("width " + image2.width);
		System.out.println("height " + image2.height);
		
		thresholdOutInt.setText(count.toString());
		thresholdOutVis.setProgress(((double) count) / 255.0);
		
		WritableImage wr = getImage(image1.getMonochromeImage());
		
		/* SET IT AS AN IMAGE VIEW */
		ImageView picture = new ImageView(wr);	
		pictureframe1.setContent(picture);
		
		System.out.println("width " + image2.width);
		System.out.println("height " + image2.height);
		
		tempImage  //image1;
				= new OCRImage(image2);
	}
	
	@FXML public void displayImage2(){
		
//		testdata = Segmentation.reduce(tempImage.monochrome, tempImage.width, tempImage.height, 1);
//		tempImage = new OCRImage(testdata, count);
//				
//		testdata = Segmentation.expand(tempImage.monochrome, tempImage.width, tempImage.height, 1);
//		tempImage = new OCRImage(testdata, count);
//		
//		testdata = Segmentation.reduce(tempImage.monochrome, tempImage.width, tempImage.height, 2);
//		tempImage = new OCRImage(testdata, count);
//				
//		testdata = Segmentation.expand(tempImage.monochrome, tempImage.width, tempImage.height, 2);
//		tempImage = new OCRImage(testdata, count);
		
		tempImage = Segmentation.getBlocks(image1, image1.width, image1.height, 0, 1);
		
		WritableImage wr = getImage(tempImage.source);
		
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
