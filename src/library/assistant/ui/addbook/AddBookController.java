package library.assistant.ui.addbook;


import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import library.assistant.database.DatabaseHandler;
import library.assistant.model.Book;
import library.assistant.utils.LibraryUtils;

public class AddBookController implements Initializable{
	
    @FXML    private AnchorPane rootPane;

    @FXML    private JFXTextField bookTitle;
    @FXML    private JFXTextField bookId;
    @FXML    private JFXTextField bookAuthor;
    @FXML    private JFXTextField publisher;

    @FXML    private JFXButton saveButton;
    @FXML    private JFXButton cancelButton;
    
    private Book checkBookId, bookToUpdate;
    private boolean isInEditMode = false;
    private Stage stage;


	private DatabaseHandler dbHandler;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	dbHandler = DatabaseHandler.getInstance();
    	dbHandler.checkData();
    }


    @FXML    void cancel(ActionEvent event) {
    	stage = (Stage) rootPane.getScene().getWindow();
    	stage.close();
    }

    @FXML
    void saveBook(ActionEvent event) {
    	stage = (Stage) rootPane.getScene().getWindow();
    	
    	String _bookID = bookId.getText();
    	String _bookTitle = bookTitle.getText();
    	String _bookAuthor = bookAuthor.getText();
    	String _publisher = publisher.getText();
    	
    	if (_bookID.isEmpty() || _bookTitle.isEmpty() 
    			|| _bookAuthor.isEmpty() || _publisher.isEmpty()) {
    		LibraryUtils.simpleAlertBox(AlertType.ERROR, "Empty field(s)", "Please enter in all fields");
    		return;
    	} 

    	checkBookId = dbHandler.getBookByBookID(_bookID);
    	
    	if (!isInEditMode) {
			handleSaveBookOperation(_bookID, _bookTitle, _bookAuthor, _publisher);

		} else {
			handelEditOperation(_bookID, _bookTitle, _bookAuthor, _publisher);
			return;
		}
    	
    	stage.close();
    }



	private void handleSaveBookOperation(String _bookID, String _bookTitle, String _bookAuthor, String _publisher) {
		if(checkBookId!= null) {
			
			LibraryUtils.simpleAlertBox(AlertType.INFORMATION, "Change your book ID", 
					"The book ID already exist.\n Change to another.");
			return;
			
		} else if(dbHandler.saveBook(_bookID, _bookTitle, _bookAuthor, _publisher)){
            Platform.runLater(() -> {
                Notifications.create()
                        .title("Registered")
            			 .position(Pos.TOP_CENTER)
                        .text("Success adding book")
                        .hideAfter(Duration.seconds(4))
                        .show();
               // errorLabel.setText(ex.getMessage());
            });
			
			// LibraryUtils.simpleAlertBox(AlertType.INFORMATION, "Registered", "Success");
		} else {
			LibraryUtils.simpleAlertBox(AlertType.ERROR, "Registration failed", "Failed to register data");
		}
	}



	private void handelEditOperation(String _bookID, String _bookTitle, String _bookAuthor,
			String _publisher) {
		if(dbHandler.updateBook(_bookID, _bookTitle, _bookAuthor, _publisher)){
			
             Platform.runLater(() -> {
                 Notifications.create()
                         .title("Registered")
             			 .position(Pos.TOP_CENTER)
                         .text("Success updating book")
                         .hideAfter(Duration.seconds(4))
                         .show();
                // errorLabel.setText(ex.getMessage());
             });
			 
			// LibraryUtils.simpleAlertBox(AlertType.INFORMATION, "Registered", "Success updating book");

		} else {
			LibraryUtils.simpleAlertBox(AlertType.ERROR, "Registration failed", "Failed to register data");
		}
		// déclencher un évènement à la fermeture
		stage.fireEvent(new WindowEvent((Window) rootPane.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
	}


	public void initDataInputFields(ActionEvent event, Book book) {
		
		System.out.println(event.getSource().toString());
		if (book != null) {
			isInEditMode= true;
			
			System.out.println("InitDataInout AddBook \n " + book);

			bookToUpdate = book;
			bookId.setText(book.getBookId());
			bookTitle.setText(book.getTitle());
			bookAuthor.setText(book.getAuthor());
			publisher.setText(book.getPublisher());
			
			bookId.setEditable(false);
		} else {
			bookId.setText("");
			bookTitle.setText("");
			bookAuthor.setText("");
			publisher.setText("");
		}
	}
    

}
