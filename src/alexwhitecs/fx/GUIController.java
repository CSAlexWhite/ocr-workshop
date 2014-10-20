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
		count = 180;
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
		
		tempImage = new OCRImage(image2, count); //copy constructor absolutely not working...:(
	}
	
	@FXML public void displayImage2(){
		
		OCRImage outImage = Segmentation.getBlocks(tempImage, 1, 2);
		
		WritableImage wr = getImage(outImage.source);
		
		/* SET IT AS AN IMAGE VIEW */
		ImageView picture = new ImageView(wr);	
		pictureframe2.setContent(picture);
	}
	
	@FXML public void expand(){
		
		int[][] outdata = Segmentation.expand(tempImage.monochrome, tempImage.width, tempImage.height, 1);
		OCRImage outImage = new OCRImage(outdata, count);
		
		WritableImage wr = getImage(outImage.source);
		
		/* SET IT AS AN IMAGE VIEW */
		ImageView picture = new ImageView(wr);	
		pictureframe2.setContent(picture);
		
		tempImage = outImage;
	}
	
	@FXML public void contract(){
		
		int[][] outdata = Segmentation.reduce(tempImage.monochrome, tempImage.width, tempImage.height, 1);
		OCRImage outImage = new OCRImage(outdata, count);
		
		WritableImage wr = getImage(outImage.source);
		
		/* SET IT AS AN IMAGE VIEW */
		ImageView picture = new ImageView(wr);	
		pictureframe2.setContent(picture);
		
		tempImage = outImage;
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
