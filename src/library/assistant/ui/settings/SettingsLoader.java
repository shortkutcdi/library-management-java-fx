package library.assistant.ui.settings;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

public class SettingsLoader extends Application {

	public static char SEPARATOR = File.separatorChar;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
//		AnchorPane root = (AnchorPane) loadFXML("Settings");
		
		FXMLLoader loader = new FXMLLoader(SettingsLoader.class.getResource("settings.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Library assistant");
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		new Thread(() -> DatabaseHandler.getInstance())
		.start();
		
		System.out.println("Dans start de SettingsController");
		//PreferencesPojoUtils.initConfig();
	}
	
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SettingsLoader.class.getResource( fxml + ".fxml"));
        return fxmlLoader.load();
    }
	
	public static void main(String[] args) {
		launch(args);
	}

}