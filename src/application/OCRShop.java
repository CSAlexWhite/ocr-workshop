package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;


public class OCRShop extends Application {
	
	static HBox root;
	
	public static void main(String[] args) {
		
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			root = (HBox)FXMLLoader
				.load(getClass().getResource("GUI.fxml"));
						
			Scene scene = new Scene(root);
			scene.getStylesheets()
				.add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {e.printStackTrace();}
	}
}