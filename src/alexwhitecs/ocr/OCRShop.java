package alexwhitecs.ocr;
	
import alexwhitecs.fx.GUIController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;

public class OCRShop extends Application {
	
	static HBox root;
	static GUIController controller;
	
	public static void main(String[] args) {
		
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader fxmlloader = new FXMLLoader();
			fxmlloader.setLocation(getClass().getResource("GUI.fxml"));
			
			root = (HBox)FXMLLoader
				.load(getClass().getResource("GUI.fxml"));
			
			controller = (GUIController) fxmlloader.getController();
			//controller.setStage(primaryStage);
						
			Scene scene = new Scene(root);
			scene.getStylesheets()
				.add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {e.printStackTrace();}
	}
}
