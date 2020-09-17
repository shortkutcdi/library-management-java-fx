package library.assistant.ui.booklist;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;
import library.assistant.model.Book;
import library.assistant.ui.addbook.AddBookController;
import library.assistant.ui.main.Main;
import library.assistant.utils.LibraryUtils;

public class BookListController implements Initializable {

	@FXML	private AnchorPane rootPane;
	@FXML	private TableView<Book> tableView;
	@FXML	private TableColumn<Book, String> titleCol;
	@FXML	private TableColumn<Book, String> idCol;
	@FXML	private TableColumn<Book, String> authorCol;
	@FXML	private TableColumn<Book, String> publisherCol;
	@FXML	private TableColumn<Book, Integer> availabilityCol;
	@FXML 	private MenuItem refreshMenuItem;
	@FXML 	private MenuItem editMenuItem;
	
	private DatabaseHandler dbHandler;
	private final ObservableList<Book> list = FXCollections.observableArrayList();


	private AddBookController addBookController;
	private Book selectedBookForUpdate;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dbHandler = DatabaseHandler.getInstance(); 
		initColumns();
		
		loadDataToTableView();
	}


    @FXML
    public void handleRefresh(ActionEvent event) {

    	refreshTable();
    }
    
    @FXML
    void handleDeleteBookOption(ActionEvent event) {
    	Book book = tableView.getSelectionModel().getSelectedItem();
    	if (book !=null) {
    		Optional< ButtonType> response = LibraryUtils.alertWithBtnResponse(
    				"Delete book", "Do you to delete " + book.getTitle() + "?");
    		
    		if (response.get() == ButtonType.OK){
				
    			if(dbHandler.deleteBook(book.getBookId())) {
    		    	tableView.getItems().remove(book);
    		    	list.remove(book);
    		    	tableView.getItems().clear();
    		    	//loadDataToTableView();
    			} else {
    				
    			}
			}
    		loadDataToTableView();
    	}
    }
    
    @FXML
    void handleEditBookOption(ActionEvent event) {
    	selectedBookForUpdate = tableView.getSelectionModel().getSelectedItem();

    	if (selectedBookForUpdate != null) {
			try {
				System.out.println("HandleEdit BookList -- selectedBookForUpdate \n "+ selectedBookForUpdate);
				Stage stage = new Stage(StageStyle.DECORATED);
				
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("/library/assistant/ui/addbook/addBook.fxml"));
				Parent rootPane =  loader.load();
//				AnchorPane rootPane = (AnchorPane) loader.load();
				
				addBookController =    loader.getController();
				addBookController.initDataInputFields(event, selectedBookForUpdate);
				
				stage.setTitle("Edit Book");
				Scene scene = new Scene(rootPane);
				
				stage.setScene(scene);
				stage.show();
				
				stage.setOnCloseRequest(event1 -> {
					System.out.println("close Request ********************************************************");
					//refreshTable();
					handleRefresh( new ActionEvent() );
				});
				
				
			} catch (IOException e) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
			} 
		} 
    }
	

    
	public void clearListView() {
		if (!list.isEmpty()) {
			list.clear();
		}
		tableView.setItems(list);
	}

	private void loadDataToTableView() {
		list.clear();
		list.setAll(dbHandler.getAllBooks());
		
		tableView.setItems(list);
	}

	private void initColumns() {
//		titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
		idCol.setCellValueFactory(cellData -> cellData.getValue().bookIdProperty());
		authorCol.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
		publisherCol.setCellValueFactory(cellData -> cellData.getValue().publisherProperty());
		availabilityCol.setCellValueFactory(cellData -> cellData.getValue().availablityProperty().asObject());
		
	}
	
	public void refreshTable() {
		list.clear();
		tableView.getItems().clear();
		
		list.addAll(dbHandler.getAllBooks());
		tableView.setItems(list);
	}


}
