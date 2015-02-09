package alexwhitecs.fx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import alexwhitecs.ocr.OCRImage;
import alexwhitecs.ocr.Scanning;
import alexwhitecs.ocr.Segmentation;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GUIController {
	
	Stage primaryStage;
	
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
		count = 100;
		imagename = null;	
	}
	
	/*************************** GUI ELEMENT METHODS  *************************/
	
	@FXML public void loadImage(){
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.setInitialDirectory(new File("D:\\Dropbox\\{Java Workspace}\\OCR-TestBed"));
		File file = fileChooser.showOpenDialog(primaryStage);
		
		image1 = new OCRImage(file, count);
		display(image1, 1);
	}
	
	@FXML public void incrementThreshold(){
		
		image1.changeThreshold(count += 5);
		thresholdOutInt.setText(count.toString());
		thresholdOutVis.setProgress(((double) count) / 255.0);
		display(image1, 2);
	}
	
	@FXML public void decrementThreshold(){
		
		image1.changeThreshold(count -= 5);
		thresholdOutInt.setText(count.toString());
		thresholdOutVis.setProgress(((double) count) / 255.0);
		display(image1, 2);
	}
	
	int i=0; boolean init = false;
	Vector<OCRImage> images	= null;	
	@FXML public void segment(){
		
		System.out.println(i);
		if(!init) images = Segmentation.chop(image1);
		init = true;
		image1 = images.elementAt(i);
		display(image1, 2);
		i = (i+1)%(images.size());
	}
	
	@FXML public void showHorizontals(){
						
		display(image1, 2);		
		horizontals();
	}
	
	@FXML public void showVerticals(){
		
		display(image1, 2);
		verticals();
	}
			
	@FXML public void expand(){
		
		int[][] outdata = Segmentation.expand(tempImage.monochrome, tempImage.width, tempImage.height, 1);
		OCRImage outImage = new OCRImage(outdata, count);
		display(outImage, 2);
	}
	
	@FXML public void contract(){
		
		int[][] outdata = Segmentation.reduce(tempImage.monochrome, tempImage.width, tempImage.height, 1);
		OCRImage outImage = new OCRImage(outdata, count);
		display(outImage, 2);
	}
	
	@FXML public void denoise(){
		
		int[][] outdata = Scanning.denoise(tempImage.monochrome, tempImage.width, tempImage.height);
		OCRImage outImage = new OCRImage(outdata, count);
		display(outImage, 2);
	}	
	
	/*************************** PRIVATE METHODS ******************************/
	
	public void horizontals(){
		
		OCRImage outImage = Segmentation.horizontals(tempImage);		
		display(outImage, 2);
	}
	
	public void verticals(){
		
		OCRImage outImage = Segmentation.verticals(tempImage);
		display(outImage, 2);
	}
	
	private void display(OCRImage image, int frame){
		
		WritableImage wr = getImage(image.source);
		
		/* SET IT AS AN IMAGE VIEW */
		ImageView picture = new ImageView(wr);
		
		if(frame == 1) pictureframe1.setContent(picture);
		if(frame == 2) pictureframe2.setContent(picture);
		
		tempImage = new OCRImage(image,count);
	}
	
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
