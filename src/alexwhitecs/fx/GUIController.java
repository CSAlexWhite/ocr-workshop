package alexwhitecs.fx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class GUIController {
	
	@FXML ScrollPane pictureframe1;
	@FXML ScrollPane pictureframe2;
	
	int count = 0;
	String imagename = null;
	
	@FXML public void displayImage1(){
		
//		if(count%3 == 0) imagename = "colors.jpg";
//		if(count%3 == 1) imagename = "letters.jpg";
//		if(count%3 == 2) imagename = "sample.jpg";
//		count++;
		
		/* LOAD THE IMAGE */
		//Image image = new Image(imagename);
		
		BufferedImage bf = null;
		try {
			bf = PreProcess.Execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		WritableImage wr = null;
		    if (bf != null) {
		        wr = new WritableImage(bf.getWidth(), bf.getHeight());
		        PixelWriter pw = wr.getPixelWriter();
		        for (int x = 0; x < bf.getWidth(); x++) {
		            for (int y = 0; y < bf.getHeight(); y++) {
		                pw.setArgb(x, y, bf.getRGB(x, y));
		            }
		        }
		    }
			
		
		
		
		
		/* SET IT AS AN IMAGE VIEW */
		ImageView picture = new ImageView(wr);
		//picture.setImage(image);
		
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
}
