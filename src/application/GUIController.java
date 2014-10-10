package application;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GUIController {
	
	@FXML ScrollPane pictureframe1;
	@FXML ScrollPane pictureframe2;
	
	int count = 0;
	String imagename = null;
	
	@FXML public void displayImage1(){
		
		if(count%3 == 0) imagename = "colors.jpg";
		if(count%3 == 1) imagename = "letters.jpg";
		if(count%3 == 2) imagename = "sample.jpg";
		count++;
		
		/* LOAD THE IMAGE */
		Image image = new Image(imagename);
		
		/* SET IT AS AN IMAGE VIEW */
		ImageView picture = new ImageView();
		picture.setImage(image);
		
		pictureframe1.setContent(picture);
         
        System.out.println("What the fuck.");
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
         
        System.out.println("What the fuck.");
	}
	
	
}
