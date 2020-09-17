package library.assistant.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LibraryAssistant extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		AnchorPane root = (AnchorPane) loadFXML("booklist/BookList");
//		AnchorPane root = (AnchorPane) loadFXML("views/AddBook");
//		AnchorPane root = (AnchorPane) loadFXML("view/AddBook");


//		FXMLLoader loader = new FXMLLoader(LibraryAssistant.class.getResource("view/FXMLDocument.fxml"));
//		AnchorPane root = (AnchorPane) loader.load();
//		ui/booklist/view/BookList.fxml
//		ui/addbook/view/AddBook.fxml
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Library assistant");
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryAssistant.class.getResource( fxml + ".fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(LibraryAssistant.class.getResource("view/"+ fxml + ".fxml"));
        return fxmlLoader.load();
    }
	
	public static void main(String[] args) {
		launch(args);
	}

}
