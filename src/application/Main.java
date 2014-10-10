package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	static HBox root;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			root = (HBox)FXMLLoader
				.load(getClass().getResource("GUI.fxml"));
			
			
			Scene scene = new Scene(root);
			scene.getStylesheets()
				.add(getClass().getResource("application.css").toExternalForm());
			
			/* LOAD THE IMAGE */
			Image image = new Image("colors.jpg");
			
			/* SET IT AS AN IMAGE VIEW */
//			ImageView iv1 = new ImageView();
//	         iv1.setImage(image);

	        // DISPLAY THE GUI 
			primaryStage.setScene(scene);
			primaryStage.show();
			
			 /* ADDS IT TO THE ROOT */ 
//			root.getChildren().add(iv1);
			
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
