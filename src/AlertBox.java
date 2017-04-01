import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	public static void display(String title, String message){
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		
		window.setTitle(title);
		window.setHeight(150);
		window.setWidth(300);
		
		Label messageL = new Label(message);
		
		Button closeButton = new Button("Close");
		closeButton.setOnAction(e->window.close());
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(messageL, closeButton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout, 300, 150);
		window.setScene(scene);
		window.show();
	}
}
