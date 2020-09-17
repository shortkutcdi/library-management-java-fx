package library.assistant.ui.main;


import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;
import library.assistant.model.Book;
import library.assistant.model.Issue;
import library.assistant.model.Member;
import library.assistant.utils.LibraryUtils;

public class MainController implements Initializable {

    @FXML    private Label infoLabel;
    
    @FXML    private MenuItem fullScreenMenuItem;
	
    @FXML    private Text bookNameTxt;
    @FXML    private Text bookAuthorTxt;
    @FXML    private Text bookStatusTxt;
    @FXML    private JFXTextField bookIdInput;

    @FXML    private Text memberNameTxt;
    @FXML    private Text memberContactTxt;
    @FXML    private JFXTextField memberIdInput;

    @FXML    private HBox memberInfoHbox;
    @FXML    private HBox bookInfoHBox;
    
    @FXML    private StackPane rootPane;
    @FXML    private TabPane mainTabPane;
    @FXML    private Tab bookIssueTab;
    @FXML    private Tab renewTab;
    @FXML    private JFXButton renewBtn;
    @FXML    private JFXButton submissionBtn;

    @FXML    private JFXTextField bookIDTxtField;
    @FXML    private ListView<String> listView;


    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm");
	private DatabaseHandler dbHandler;
	private ObservableList<String> listIssueData = FXCollections.observableArrayList();
	private boolean isReadyForSubmission = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// definir un style de profondeur (ombre portée)
		JFXDepthManager.setDepth(bookInfoHBox, 1);
		JFXDepthManager.setDepth(memberInfoHbox, 1);
		
		desactivateIssueButtons();
		fullScreenMenuItem.setText("Fullsreen");

		dbHandler = DatabaseHandler.getInstance(); 
	}
	
	public MainController() {
		
	}

	private void activateIssueButtons() {
		renewBtn.setDisable(false);
		submissionBtn.setDisable(false);
	}
	
	private void desactivateIssueButtons() {
		renewBtn.setDisable(true);
		submissionBtn.setDisable(true);
	}

	public void clearBookCache() {
    	bookNameTxt.setText("");
    	bookAuthorTxt.setText("");
    	bookStatusTxt.setText("");
    }
    
    public void clearMemberCache() {
		memberNameTxt.setText("");
		memberContactTxt.setText("");
    }
	
    @FXML
    void loadMemberInfo(ActionEvent event) {
    	// effacer les données entrées précédement
    	clearMemberCache();
		String memberId = memberIdInput.getText();
    	
		if (!memberId.isEmpty()) {
			
			Member member = dbHandler.getMemberByMemberID(memberId);

			if (member != null) {
				memberNameTxt.setText(member.getName());
				memberContactTxt.setText(member.getMobile());
				System.out.println(member);
			} else {
				memberNameTxt.setText("No Such Member Available");
			}
		}
    }
	
	@FXML
	void loadBookInfo(ActionEvent event) {
		// effacer les données entrées précédement
		clearBookCache();
		String bookId = bookIdInput.getText();

		
		if (!bookId.isEmpty()) {
			
			Book book = dbHandler.getBookByBookID(bookId);

			if (book != null) {
				bookNameTxt.setText(book.getTitle());
				bookAuthorTxt.setText(book.getAuthor());
				bookStatusTxt.setText(book.getAvailablity()==1? "Available": "No available");
				System.out.println(book);
			} else {
				bookNameTxt.setText("No Such Book Available");
			}
		}
	}

	/*****************************************************
	 * 
	 *	Open windows 
	 *
	*****************************************************/
	
	@FXML
    void loadAddBook(ActionEvent event) {
    	loadWindowAddBook();
    }

    @FXML
    void loadAddMember(ActionEvent event) {
    	loadWindowAddMember();
    }

    @FXML
    void loadBookTable(ActionEvent event) {
    	
    	loadWindowBookTable();
    }

    @FXML
    void loadMemberTable(ActionEvent event) {
    	loadWindowMemberTable();
    }

    @FXML
    void loadSettings(ActionEvent event) {
    	loadWindowSettings();
    }

	private void loadWindowAddBook() {
		LibraryUtils.loadWindow("/library/assistant/ui/addbook/addBook.fxml", "Saving a book");
    }
    
	private void loadWindowAddMember() {
		LibraryUtils.loadWindow("/library/assistant/ui/addmember/addMember.fxml", "Saving a member");
	}
	
	private void loadWindowBookTable() {
		LibraryUtils.loadWindow("/library/assistant/ui/booklist/bookList.fxml", "List of books");
	}


	private void loadWindowMemberTable() {
		LibraryUtils.loadWindow("/library/assistant/ui/memberlist/memberList.fxml", "List of members");
	}

    private void loadWindowSettings() {
    	LibraryUtils.loadWindow("/library/assistant/ui/settings/settings.fxml", "Settings");
	}

    /*****************************************************
     * 
     *	Renew/Submission Tab 
     *
     *****************************************************/
    
	@FXML
    void loadIssueOperation(ActionEvent event) {
    	
    	String member_id = memberIdInput.getText();
    	String book_id = bookIdInput.getText();
    	
    	int resultat = -2;
    	
    	// aucun champs vide
    	if (!member_id.isEmpty() && !book_id.isEmpty() ) {
    		
    		// disponibilité d'un livre présent
    		if ( dbHandler.getBookByBookID(book_id)!=null && !dbHandler.checkBookAvailability(book_id) ) {
				LibraryUtils.simpleAlertBox(AlertType.WARNING, "Book is not available", 
						"Please choose a book available.");

			} 
    		// book ou membre non présents dans bdd
    		else if(dbHandler.getBookByBookID(book_id)==null || dbHandler.getMemberByMemberID(member_id)==null) {
    			
				LibraryUtils.simpleAlertBox(AlertType.WARNING, "Requesting data not present in database", 
						"Wrong value for book ID and/or member ID\n"
						+ "Data are not in database\n"
						+ "Please give a new pair of book_id and member_id \n"
						+ "to complete the operation.");

    		} else {
    			
    			Optional<ButtonType> response = LibraryUtils.alertWithBtnResponse(
    					"Confirm the issue operation",
    					"Are you sure to issue the book " + bookNameTxt.getText() + " to " + memberNameTxt.getText() + " ?");
    			
    			if (response.get() == ButtonType.OK) {
    				resultat = dbHandler.saveIssue(book_id, member_id);

    				switch (resultat) {
    				case 1:
    					LibraryUtils.simpleAlertBox(AlertType.INFORMATION, "Registered", "Book Issue complete");
    					break;
    				case 0:
    					LibraryUtils.simpleAlertBox(AlertType.WARNING, "An Book Issue allready exists in database", 
    							"Data all ready exist\n"
    							+ "Please give a new pair of book ID and member ID \n"
    							+ "to complete the operation.");
    					break;
    					
    				default:
    					LibraryUtils.simpleAlertBox(AlertType.ERROR, "Registration error", 
    							"Issue OPeration failled.");
    					break;
    				}
    			} 
    		}
    		
    	// champs vides	
    	} else {
    		LibraryUtils.simpleAlertBox(AlertType.ERROR, "Empty fields", 
    				"Please fill the fields book_id and member_id \n"
    				+ "to complete the operation.");
		}
    }



	public void clearListView() {
		if (!listIssueData.isEmpty()) {
			listIssueData.clear();
		}
		listView.setItems(listIssueData);
	}

	@FXML
	void loadBookInfoRenewTab(ActionEvent event) {
		// effacer les données entrées précédement
		clearListView();
		
		String bookId = bookIDTxtField.getText();
		// champ non vide
		if (!bookId.isEmpty()) {
			
			Issue issue = dbHandler.getIssueByBookId(bookId);
			System.out.println(issue);
			
			if (issue != null) {
				isReadyForSubmission = true;
				activateIssueButtons();
				
				//listIssueData = FXCollections.observableArrayList();
				Book book = dbHandler.getBookByBookID(issue.getBookId());
				Member member = dbHandler.getMemberByMemberID(issue.getMemberId());
				
				Date date = issue.getIssueTime();
				listIssueData.add("Issue Date :\t\t" + dateFormat.format(date));
				listIssueData.add("Renew Count :\t\t"+ issue.getRenewCount());
				
				listIssueData.add("\n");
				
				listIssueData.add("Book\n");
				listIssueData.add("Title:\t\t\t\t"+ book.getTitle());
				listIssueData.add("Book Id:\t\t\t"+ book.getBookId());
				listIssueData.add("Author:\t\t\t"+ book.getAuthor());
				listIssueData.add("Publisher:\t\t\t"+ book.getPublisher());
				listIssueData.add("Availability:\t\t" + (book.getAvailablity()==1? "Available": "No available") );
				
				listIssueData.add("\n");
	
				listIssueData.add("Member\n");
				listIssueData.add("Name:\t\t\t" + member.getName());
				listIssueData.add("member ID:\t\t" + member.getMemberId());
				listIssueData.add("Mobile:\t\t\t" + member.getMobile());
				listIssueData.add("Email:\t\t\t" + member.getEmail());
				
				//observableList.setAll(listIssueData);
				//listView.getItems().setAll(listIssueData);
				listView.setItems(listIssueData);
				
				System.out.println("\n\n" +issue);
				System.out.println(book);
				System.out.println(member);
			} else {
				isReadyForSubmission = false;
				desactivateIssueButtons();
				clearListView();
				listView.getItems().add("No Book Issue Found");
			}
		}
	}

	
    @FXML
	void loadSubmissionOperation(ActionEvent event) {
		
		String bookId = bookIDTxtField.getText();
		
		if (isReadyForSubmission) {
			
			// alert de confirmation
			Optional<ButtonType> response = LibraryUtils.alertWithBtnResponse(
					"Confirm the submission operation", "Are you sure to return the book  ?");
			
			if(response.get() == ButtonType.OK) {
				if(dbHandler.deletedIssue(bookId)) {
					LibraryUtils.simpleAlertBox(AlertType.CONFIRMATION, "Success", "Book has been submitted");
				clearListView();
					
				} else {
					LibraryUtils.simpleAlertBox(AlertType.ERROR, "Failed", "Failed to proced the operation");
				}
			}
			
		} 
// plus besoin car gestion avec l'actvation/desactivation des boutons
//		else {
//			// alert no issue...
//			LibraryUtils.simpleAlertBox(AlertType.WARNING, "No issue found", "Please select a book issue to submit");
//		}
	}


	@FXML
    void loadRenewOp(ActionEvent event) {
		
		String bookId = bookIDTxtField.getText();

		// si isReadyForSubmission=true - champ non vide et issue existant
		if (isReadyForSubmission) {
			Optional< ButtonType> response = LibraryUtils.alertWithBtnResponse(
					"Renew this issue", "Do you want renew ths issue?");
			
			if(response.get() == ButtonType.OK) {
				if(dbHandler.renewIssue(bookId)) {
					LibraryUtils.simpleAlertBox(AlertType.INFORMATION, "Book issue updated", "Book issue has been renewed");
					// recharge le listView -> afficher les modif renew count et issue_time
					loadBookInfoRenewTab(event);
				} else {
					LibraryUtils.simpleAlertBox(AlertType.ERROR, "Failed", "Failed to proced the operation");
				}
			}
			
		} 
// plus besoin car gestion avec l'actvation/desactivation des boutons		
//		else {
//			LibraryUtils.simpleAlertBox(AlertType.WARNING, "No issue found", "Please select a book issue to submit");
//		}
		
    }
	
	/*****************************************************
	 * 
	 *	Menu 
	 *
	*****************************************************/

    @FXML
    void handleMenuClose(ActionEvent event) {
    	((Stage) rootPane.getScene().getWindow()).close();
    	System.exit(0);
    }
    

    @FXML
    void handleMenuAddBook(ActionEvent event) {
    	loadWindowAddBook();
    }

    @FXML
    void handleMenuAddMember(ActionEvent event) {
    	loadWindowAddMember();
    }
    
    @FXML
    void handleMenuViewBooks(ActionEvent event) {
    	loadWindowBookTable();
    }

    @FXML
    void handleMenuViewMembers(ActionEvent event) {
    	loadWindowMemberTable();
    }
    
    @FXML
    void handleMenuFullScreen(ActionEvent event) {
    	Stage stage = ((Stage) rootPane.getScene().getWindow());
    	stage.setFullScreen(!stage.isFullScreen());
    	
    	String fullscrenMenuText= stage.isFullScreen() ?	"Exit fullscreen": "Fullscreen";
    	fullScreenMenuItem.setText(fullscrenMenuText);
    }


}
