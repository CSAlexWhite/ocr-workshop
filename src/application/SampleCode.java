
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SampleCode extends Application {
  public static void main(String[] args) { launch(args); }

  String[] imageLocs = { 
    "http://uhallnyu.files.wordpress.com/2011/11/green-apple.jpg",
    "http://i.i.com.com/cnwk.1d/i/tim/2011/03/10/orange_iStock_000001331357X_540x405.jpg",
    "http://smoothiejuicerecipes.com/pear.jpg"
  };
  ImageView[] images = new ImageView[imageLocs.length];

  @Override public void init() {
    for (int i = 0; i < imageLocs.length; i++) {
      images[i] = new ImageView(new Image(imageLocs[i], 200, 0, true, true));
    }
  }

  @Override public void start(Stage stage) {
    stage.setTitle("Healthy Choices");
    stage.getIcons().add(new Image("http://files.softicons.com/download/application-icons/pixelophilia-icons-by-omercetin/png/32/apple-green.png"));

    final ScrollPane scroll = new ScrollPane();
    scroll.setPrefSize(100, 100);

    final IntegerProperty curImageIdx = new SimpleIntegerProperty(0);
    scroll.setContent(images[curImageIdx.get()]);

    Button next = new Button("Next Image");
    next.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent t) {
        curImageIdx.set((curImageIdx.get() + 1) % 3);
        scroll.setContent(images[curImageIdx.get()]);
      }
    });

    Button remove = new Button("Remove Image");
    remove.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent t) {
        scroll.setContent(null);
      }
    });

    VBox controls = new VBox(10);
    controls.getChildren().setAll(next, remove);

    HBox layout = new HBox(10);
    layout.getChildren().setAll(controls, scroll);
    layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 15;");
    HBox.setHgrow(scroll, Priority.ALWAYS);

    stage.setScene(new Scene(layout));
    stage.show();

    scroll.setHvalue(0.25); scroll.setVvalue(0.25);
  }
}