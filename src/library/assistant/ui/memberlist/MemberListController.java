package library.assistant.ui.memberlist;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import library.assistant.database.DatabaseHandler;
import library.assistant.model.Member;

public class MemberListController implements Initializable {
	@FXML	private AnchorPane rootPane;
	
	@FXML	private TableView<Member> tableView;
	@FXML	private TableColumn<Member, String> nameCol;
	@FXML	private TableColumn<Member, String> memberIdCol;
	@FXML	private TableColumn<Member, String> mobileCol;
	@FXML	private TableColumn<Member, String> emailCol;
	
	private DatabaseHandler dbHandler;
	private ObservableList<Member> list = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initColumns();
		dbHandler = DatabaseHandler.getInstance();
		
		loadDataToTableView();
	}

	private void loadDataToTableView() {
		list= dbHandler.getAllMembers();
		
		tableView.getItems().addAll(list);
		list.forEach(System.out::println);
	}

	
	private void initColumns() {
//		titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		memberIdCol.setCellValueFactory(cellData -> cellData.getValue().memberIdProperty());
		mobileCol.setCellValueFactory(cellData -> cellData.getValue().mobileProperty());
		emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
	}
	
	@FXML
	public void handleEditMemberOption(ActionEvent event) {
		System.out.println("edit");
	}

	@FXML
	public void handleDeleteMemberOption(ActionEvent event) {
		System.out.println("delete");
	}
	

}
