package library.assistant.ui.main;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;



public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		//StackPane root = (StackPane) loadFXML("Main");
//		Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/ui/login/login.fxml"));
//		primaryStage.initStyle(StageStyle.UNDECORATED);
		Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/ui/main/main.fxml"));
		Scene scene = new Scene(root);
		
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		new Thread(() -> DatabaseHandler.getInstance())
			.start();
	}
	
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource( fxml + ".fxml"));
        return fxmlLoader.load();
    }
	
	public static void main(String[] args) {
		String path;
		try {
			path = new File(".").getCanonicalPath();
			System.out.println(path);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		launch(args);
	}

}
