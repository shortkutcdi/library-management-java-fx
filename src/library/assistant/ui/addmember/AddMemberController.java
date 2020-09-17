package library.assistant.ui.addmember;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;
import library.assistant.utils.LibraryUtils;

public class AddMemberController implements Initializable{

    @FXML    private AnchorPane rootPane;

    @FXML    private JFXTextField name;

    @FXML    private JFXTextField id;

    @FXML    private JFXTextField mobile;

    @FXML    private JFXTextField email;

    @FXML    private JFXButton saveButton;

    @FXML    private JFXButton cancelButton;
    
    
	DatabaseHandler dbHandler;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	dbHandler = DatabaseHandler.getInstance();
    	dbHandler.checkData();
    }
    

    @FXML    
    void addMember(ActionEvent event) {
    	
    	String _name = name.getText();
    	String _id = id.getText();
    	String _mobile = mobile.getText();
    	String _email = email.getText();
    	
    	boolean flag = _name.isEmpty() || _id.isEmpty() || _mobile.isEmpty() || _email.isEmpty();
    	
    	if (flag) {
    		LibraryUtils.simpleAlertBox(AlertType.ERROR, "Empty field(s)", "Please enter in all fields");
    		return;
		} else if (dbHandler.saveMember(_id, _name, _mobile, _email)) {
			LibraryUtils.simpleAlertBox(AlertType.INFORMATION, "Registered", "Success saving member");
		} else {
			LibraryUtils.simpleAlertBox(AlertType.ERROR, "Registration failed", "Failed to register data");
		}
    }

    @FXML
    void cancel(ActionEvent event) {
    	Stage stage  = (Stage) rootPane.getScene().getWindow();
    	stage.close();
    }


}
