package library.assistant.utils;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.ui.main.MainController;

import library.assistant.ui.main.Main;

public class LibraryUtils {
	
	/**
	 * <p>Define an alert with its AlertType, title and content<br>
	 * 
	 * @param typeAlert
	 * @param title
	 * @param content
	 */
	public static void simpleAlertBox(AlertType typeAlert, String title, String content) {
		Alert alert = new Alert(typeAlert);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	/**
	 * <p>Define an alert with its AlertType, title and content<br>
	 * Use the return value to test for example if Button OK has been clicked</p>
	 * 
	 * <b>example</b>:<br>
	 * 
	 *<pre>Optional< ButtonType> response = 
	 * 	LibraryUtils.alertWithBtnResponse( 
	 * 		AlertType.CONFIRMATION, "Title", "Content here...");
	 *		
	 *if(response.get() == ButtonType.OK) {
	 *	//your code here
	 *}</pre>
	 * 
	 * @param alertType
	 * @param title
	 * @param content
	 * @return Optional<ButtonType>
	 */
	public static Optional<ButtonType> alertWithBtnResponse(String title, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setContentText(content);
		
		Optional<ButtonType> response = alert.showAndWait();
		return response;
	}
	
	public static void loadWindow(String loc, String title) {
//		public static <T extends Object> void loadWindow(Class<T> thisClass,String loc, String title) {
		try {
			
			Parent parent = FXMLLoader.load(Main.class.getResource(loc));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle(title);
			
			stage.setScene(new Scene(parent));
			stage.show();
		} catch (IOException e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
		}
	}



}
