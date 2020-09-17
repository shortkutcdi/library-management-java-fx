package library.assistant.ui.settings;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.utils.LibraryUtils;
import library.assistant.utils.PreferencesPojoUtils;

public class SettingsController implements Initializable {
	
    @FXML    private AnchorPane rootPane;

	@FXML    private JFXTextField numDaysWithoutFineInput;
    @FXML    private JFXTextField finePerDayInput;
    @FXML    private JFXTextField userNameInput;
    @FXML    private JFXPasswordField passwordInput;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	System.out.println("Dans initalize StettingsController");
    	initDefaultValues();
    }

    private void initDefaultValues() {
		try {
			// check if config file exist, if exist  -> déserialise 
			PreferencesPojo pojo = PreferencesPojoUtils.deserializePreferencesPojo();
			if (pojo != null) {
				numDaysWithoutFineInput.setText(String.valueOf(pojo.getNbrDaysWithoutFine()));
				finePerDayInput.setText(String.valueOf(pojo.getFinePerDay()));
				userNameInput.setText(pojo.getUsername());
				passwordInput.setText(pojo.getPassword());
				
			} else {
				// if no file or pojo==null create a new config file with default data
				PreferencesPojoUtils.initConfig2();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML 
    void handleCancelButtonAction(ActionEvent event) {
    	System.out.println("handleCancelButtonAction called");
    	closeStage();
    }

	private void closeStage() {
		Stage stage = (Stage) rootPane.getScene().getWindow();
    	stage.close();
	}

    @FXML
    void handleSaveButtonAction(ActionEvent event) {
    	System.out.println("handleSaveButtonAction called");
		String  _numDaysWithoutFine = numDaysWithoutFineInput.getText();
		String _finePerDay = finePerDayInput.getText();
		String _userName = userNameInput.getText();
		String _password = passwordInput.getText();
		
		boolean emptyFields = _numDaysWithoutFine.isEmpty() || _finePerDay.isEmpty()
											|| _userName.isEmpty() || _password.isEmpty();
		
		if (!emptyFields) {
			PreferencesPojo pojo = new PreferencesPojo( Integer.parseInt(_numDaysWithoutFine), 
					Float.parseFloat(_finePerDay), _userName, _password);
			
			if (pojo!= null) {
				try {
					PreferencesPojoUtils.serializePojo(pojo);

					Optional< ButtonType> response = LibraryUtils
							.alertWithBtnResponse("Save Settings", "Do you confirm new settings?");
					
					if(response.get() == ButtonType.OK) {
						closeStage();
					}
					
				} catch (IOException e) {
					System.out.println("Dans le catch de saveHandle");
					LibraryUtils.simpleAlertBox(AlertType.ERROR, "Registration failed", "Failed to register data");
					e.printStackTrace();
				}
			}
		} else {
			LibraryUtils.simpleAlertBox(AlertType.ERROR, "Empty field(s)", "Please enter in all fields");
		}
		//closeStage();
    }


}