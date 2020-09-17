package library.assistant.ui.addmember;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddMemberLoader extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		AnchorPane root = (AnchorPane) loadFXML("addMember");
//		AnchorPane root = (AnchorPane) loadFXML("views/AddMember");
		
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Library assistant");
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddMemberLoader.class.getResource( fxml + ".fxml"));
        return fxmlLoader.load();
    }
	
	public static void main(String[] args) {
		launch(args);
	}

}
