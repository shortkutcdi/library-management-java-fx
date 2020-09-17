package library.assistant.ui.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.codec.digest.DigestUtils;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.ui.settings.PreferencesPojo;
import library.assistant.utils.PreferencesPojoUtils;

public class LoginController implements Initializable {
	
    @FXML    private AnchorPane rootPane;
    @FXML    private JFXTextField username;
    @FXML    private JFXPasswordField password;
    

    @FXML    private VBox titleVbox;
    @FXML    private Label titleLabel;

    private PreferencesPojo pojo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			pojo = PreferencesPojoUtils.deserializePreferencesPojo();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
    @FXML
    void handleCancelAction(ActionEvent event) {
    	closeStage();
    	System.exit(0);
    }
    
    @FXML
    void handleLoginAction(ActionEvent event) {
    	String _username = username.getText();
    	String _password = DigestUtils.sha1Hex(password.getText());
    	System.out.println("Username "+ _username);
    	titleVbox.setStyle("-fx-background-color: #b71c1c");
    	titleLabel.setText("Library Assistant Login");

		try {
			if (pojo != null && pojo.getUsername().equals(_username) && pojo.getPassword().equals(_password)) {
				closeStage();
				loadMain();
			} else {
				titleVbox.setStyle("-fx-background-color: #b71c1c");
				titleLabel.setText("Invalid credentials");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
			
    }

	private void closeStage() {
		 ((Stage) username.getScene().getWindow()).close();
	}

	private void loadMain() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/ui/main/main.fxml"));
		
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setTitle("");
		stage.setScene(new Scene(root));
		stage.show();
	}

}
