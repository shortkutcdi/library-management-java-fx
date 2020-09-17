package library.assistant.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.sql.DataSource;
import javax.swing.JOptionPane;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library.assistant.model.Book;
import library.assistant.model.Issue;
import library.assistant.model.Member;
import library.assistant.ui.main.MainController;

public class DatabaseHandler {
	
    private final static org.apache.logging.log4j.Logger LOGGER =  LogManager.getLogger(DatabaseHandler.class.getName());

    private static DatabaseHandler handler = null;

    private static Connection conn = null;
    private static Statement stmt = null;
    private static PreparedStatement prepstmt = null;
    
	
    static {
        createConnection();
        // verifie si les tables existent sinon le génère
        setUpBookTable();
        setUpMemberTable();
        setUpIssueTable();
    }
    
    private DatabaseHandler() {
	}
    
    public static DatabaseHandler getInstance() {
    	if (handler == null) {
			handler = new DatabaseHandler();
		}
    	return handler;
    }
    
    private static void createConnection() {
        try {
        	DataSource dataSource = DataSourceProvider.getSingleDataSourceInstance();
			conn = dataSource.getConnection();
			((BasicDataSource) dataSource).setInitialSize(5);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.out.println( " " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    public Book getBookByBookID(String book_id) {
    	String sql = "SELECT * FROM book WHERE book_id=?";
    	ResultSet rs;
    	Book book = null;
		try {
			prepstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prepstmt.setString(1, book_id);
			
			rs = prepstmt.executeQuery();
			while(rs.next()) {
				String bookId = rs.getString("book_id");
				String title = rs.getString("title");
				String author = rs.getString("author");
				String publisher = rs.getString("publisher");
				Integer availablity = rs.getInt("is_avail");
				book = new Book(title, bookId, author, publisher, availablity);
			}
			
		} catch (SQLException e) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
			System.out.println("getBookByBookID error");
			e.printStackTrace();
		}
		return book;
    }
    
	public boolean saveBook(String bookId, String bookTitle, String bookAuthor, String publisher) {
    	
    	String sql = "INSERT INTO book VALUES (?,?,?,?, '', 1)";
    	Map<String, String> message = new HashMap<>();
    		
    	try {
            // transaction manuelle
            conn.setAutoCommit(false);
            
            if (checkBookId(bookId)!= null && checkBookId(bookId).length() > 0 ) {
            	message.put("errorId", "Book ID Already exist.\n Please change to a new one.");
            	System.out.println("Pas bon");
            	conn.commit(); 	//<<<<<<<<<<<<<<<<
				return false;
			} else {
					System.out.println("bon");
					
					prepstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					prepstmt.setString(1, bookId);
					prepstmt.setString(2, bookTitle);
					prepstmt.setString(3, bookAuthor);
					prepstmt.setString(4, publisher);
					
					prepstmt.executeUpdate();
					
					conn.commit(); 	//<<<<<<<<<<<<<<<<
					message.put("success", "Book saved to Database");
			}
    	} catch (SQLException e) {
    		System.out.println("erreur saveBook ");
    		e.printStackTrace();
    		try {
    			conn.rollback(); 	//<<<<<<<<<<<<<<<<
    			System.out.println("rollbak saveBook ");
    			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "rollbak saveBook ", e);
    		} catch (SQLException e1) {
    			e1.printStackTrace();
    		}	
    		Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Fail to save Book.", e);
    		message.put("errorFail", "Fail to save.");
    		return false;
    	} finally {
    		
    	}
    	return true;
    }
	
	
	public boolean deleteBook(String bookId) {
		String sqlDelete = "DELETE FROM book WHERE book_id=?";
		long resultDel = 0L;
		
		try {
			conn.setAutoCommit(false);
			prepstmt = conn.prepareStatement(sqlDelete, Statement.RETURN_GENERATED_KEYS);
			prepstmt.setString(1, bookId);
	
			// renvoi 0 si pas d'effacement
			resultDel = prepstmt.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback(); 	//<<<<<<<<<<<<<<<<
				System.out.println("rollbak deletedIssue ");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	
			return false;
		}
		return true;
	}

	public ObservableList<Book> getAllBooks() {
		ObservableList<Book> bookList =  FXCollections.observableArrayList();
		try {
			String sql = "SELECT * FROM book";
			ResultSet rs = executeQuery(sql);
			while (rs.next()) {
				String title = rs.getString("title");
				String bookId = rs.getString("book_id");
				String author = rs.getString("author");
				String publisher = rs.getString("publisher");
				int availablity = rs.getInt("is_avail");
				Book book = new Book(title, bookId, author, publisher, availablity);
				bookList.add(book);
				System.out.println(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookList;
	}

	public String checkBookId(String bookId) {
		String sql = "SELECT * FROM book WHERE book_id=?";
		ResultSet rs;
		String book_id = null;
		try {
			prepstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prepstmt.setString(1, bookId);
			
			rs = prepstmt.executeQuery();
			while(rs.next())
				book_id = rs.getString("book_id");
			
		} catch (SQLException e) {
			System.out.println("checkBookIdExists error");
			e.printStackTrace();
		
		}
		return book_id;
	}

	public boolean checkBookAvailability(String book_id) {
		String sql = "SELECT count(*) FROM book WHERE book_id=? and is_avail=1";
		ResultSet rs;
		int count = 0;
		
		try {
			prepstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prepstmt.setString(1, book_id);
			
			rs = prepstmt.executeQuery();
			rs.next();
			count = rs.getInt(1);
			
		} catch (SQLException e) {
			
			System.out.println("checkBookIdExists error");
			e.printStackTrace();
		}
		return count==1? true: false;
	}

	public boolean saveMember(String memberId, String name, String mobile, String email) {
		
		String sql = "INSERT INTO `membre` (`member_id`, `name`, `mobile`, `email`) VALUES (?, ?, ?, ?);";
		Map<String, String> message = new HashMap<>();
		System.out.println(sql);
		try {
			// transaction manuelle
			conn.setAutoCommit(false);

			prepstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prepstmt.setString(1, memberId);
			prepstmt.setString(2, name);
			prepstmt.setString(3, mobile);
			prepstmt.setString(4, email);
			
			prepstmt.executeUpdate();
			
			conn.commit(); 	//<<<<<<<<<<<<<<<<
			message.put("success", "Member saved to Database");
		} catch (SQLException e) {
			System.out.println("erreur saveMember ");
			e.printStackTrace();
			try {
				conn.rollback(); 	//<<<<<<<<<<<<<<<<
				System.out.println("rollbak saveMember ");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	
			message.put("errorFail", "Fail to save.");
			return false;
		} finally {
			
		}
		return true;
	}
	
	public Member getMemberByMemberID(String member_id) {
		String sql = "SELECT * FROM membre WHERE member_id=?";
		ResultSet rs;
		Member member = null;
		try {
			prepstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prepstmt.setString(1, member_id);
			
			rs = prepstmt.executeQuery();
			while(rs.next()) {
				String name = rs.getString("name");
				String memberId = rs.getString("member_id");
				String mobile = rs.getString("mobile");
				String email = rs.getString("email");
				
				member = new Member(name, memberId, mobile, email);
			}
			
		} catch (SQLException e) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
			System.out.println("getMemberByMemberID error");
			e.printStackTrace();
		}
		return member;
	}

	public ObservableList<Member> getAllMembers() {
    	ObservableList<Member> memberList =  FXCollections.observableArrayList();
    	try {
    		String sql = "SELECT * FROM membre";
//    		String sql = "SELECT * FROM member";
    		ResultSet rs = executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("name");
				String memberId = rs.getString("member_id");
				String mobile = rs.getString("mobile");
				String email = rs.getString("email");
				Member member = new Member(name, memberId, mobile, email);
				memberList.add(member);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return memberList;
	}
    
    public Issue getIssueByBookId(String book_id) {
		String sql = "SELECT * FROM issue WHERE book_id=?";
		ResultSet rs;
		Issue issue = null;
		try {
			prepstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prepstmt.setString(1, book_id);
			
			rs = prepstmt.executeQuery();
			while(rs.next()) {
				String bookId = rs.getString("book_id");
				String memberId = rs.getString("member_id");
				Date issueTime = rs.getDate("issue_time");
				Integer renewCount = rs.getInt("renew_count");
				issue = new Issue(bookId, memberId, issueTime, renewCount);
			}
			
		} catch (SQLException e) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
			System.out.println("getBookByBookID error");
			e.printStackTrace();
		}
		return issue;
	}

	/**
     * @param book_id
     * @param member_id
     * @return
     * 		<ul>
     * 		<li>1 if data saved</li>
     * 		<li>0  if a row exists in issue table <br>with the values of parameters book_id and member_id</li>
     * 		<li>-1 if book is not available</li>
     * 		<li>-3 if there is an error in database</li>
     * 		<li>-2 data book_id and/or member_id are not present in respective table book, member </li>
     * 		</ul> 
     */
	public int saveIssue(String book_id, String member_id) {
		
		if (!checkBookAvailability(book_id)) return -1;
		
		try {
			conn.setAutoCommit(false);

			String sqlissue = "select count(*) from issue WHERE book_id=? AND member_id=?";
			prepstmt = conn.prepareStatement(sqlissue, Statement.RETURN_GENERATED_KEYS);
			prepstmt.setString(1, book_id);
			prepstmt.setString(2, member_id);
			
			ResultSet rs = prepstmt.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			
			if (count>0) return 0;
						
			if(getBookByBookID(book_id)==null || getMemberByMemberID(member_id)==null)
			return -2;
			
			
			String sql = "INSERT INTO `issue` (`book_id`, `member_id`, renew_count) VALUES (?, ?, 0);";
			prepstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prepstmt.setString(1, book_id);
			prepstmt.setString(2, member_id);
			int resultat = prepstmt.executeUpdate();
			System.out.println("résultat du saveIssue : " + resultat);
			
			String sql2 = " UPDATE `book` SET `is_avail` = 0 WHERE `book_id`=?;";
			prepstmt = conn.prepareStatement(sql2);
			prepstmt.setString(1, book_id);
			
			resultat = prepstmt.executeUpdate();
			System.out.println("résultat du saveIssue : " + resultat);
				
			conn.commit();
				
		} catch (SQLException e) {
			System.out.println("Erreur saveIssue ");
			e.printStackTrace();
			try {
				conn.rollback(); 	//<<<<<<<<<<<<<<<<
				System.out.println("rollbak saveIssue ");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	
			return -3;
		} finally {
			
		}
		return 1;
	}


	public boolean deletedIssue(String bookId) {
		
		String sqlDelete = "DELETE FROM issue WHERE book_id=?";
		String sqlUpdateBookIsAvail = "UPDATE book SET is_avail=1 WHERE book_id=?";
		
		long resultUp = 0L, resultDel = 0L;
		
		try {
			conn.setAutoCommit(false);
			prepstmt = conn.prepareStatement(sqlDelete, Statement.RETURN_GENERATED_KEYS);
			prepstmt.setString(1, bookId);

			// renvoi 0 si pas d'effacement
			resultDel = prepstmt.executeUpdate();
			
			if (resultDel != 0L) {
				prepstmt = conn.prepareStatement(sqlUpdateBookIsAvail, Statement.RETURN_GENERATED_KEYS);
				prepstmt.setString(1, bookId);
				// renvoi 0 si pas d'update
				resultUp = prepstmt.executeUpdate();
			}
			
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback(); 	//<<<<<<<<<<<<<<<<
				System.out.println("rollbak deletedIssue ");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	
			return false;
		}
		return true;
	}
    
	
    public boolean renewIssue(String bookId) {
		String sqlRenewIssue = "UPDATE issue SET issue_time = NOW(), renew_count = (renew_count+1) "
							 + " WHERE book_id = ?;";
		
		long resultUp = 0L;
		
		try {
			conn.setAutoCommit(false);
	
			prepstmt = conn.prepareStatement(sqlRenewIssue, Statement.RETURN_GENERATED_KEYS);
			prepstmt.setString(1, bookId);
			// renvoi 0 si pas d'update
			resultUp = prepstmt.executeUpdate();
			
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback(); 	//<<<<<<<<<<<<<<<<
				System.out.println("rollbak deletedIssue ");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	
			return false;
		}
		return resultUp!=0;
	}

	public static void setUpMemberTable() {
    	String TABLE_NAME = "membre";
    	
    	try {
			stmt = conn.createStatement();
			
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
			
			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + " Allready exists. Ready for go!");
			} else {
				String sql = "CREATE TABLE IF NOT EXISTS library_assistant.member (\r\n" + 
						"  member_id varchar(200) DEFAULT NULL,\r\n" + 
						"  name varchar(200) DEFAULT NULL,\r\n" + 
						"  mobile varchar(20) DEFAULT NULL,\r\n" + 
						"  email varchar(100) DEFAULT NULL,\r\n" + 
						"  PRIMARY KEY (id,`member_id`)\r\n" + 
						") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
				
				stmt.execute(sql);				
				System.out.println("create table " + TABLE_NAME);
			}
		} catch (SQLException e) {
			System.out.println("erreur setUpMemberTable ");
			System.err.println(e.getMessage() + " ... setupDatase");
		} finally {

		}
		
	}
    
    public static void setUpBookTable() {
    	String TABLE_NAME = "book";
    	
    	try {
			stmt = conn.createStatement();
			
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
			
			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + " Allready exists. Ready for go!");
			} else {
				String sql = "CREATE TABLE `library_assistant`.`" + TABLE_NAME +"` (\r\n" + 
						"  `book_id` varchar(200) DEFAULT NULL,\r\n" + 
						"  `title` VARCHAR(200) NULL,\r\n" + 
						"  `author` VARCHAR(200) NULL,\r\n" + 
						"  `publisher` VARCHAR(100) NULL,\r\n" + 
						"  `intcode` VARCHAR(100) NULL,\r\n" + 
						"  `isAvail` TINYINT(1) NULL DEFAULT 1,\r\n" + 
						"  PRIMARY KEY (`id`,`book_id`))\r\n" + 
						"ENGINE = InnoDB\r\n" + 
						"DEFAULT CHARACTER SET = utf8mb4;";
				stmt.execute(sql);				
				System.out.println("create table " + TABLE_NAME);
			}
		} catch (SQLException e) {
			System.out.println("erreur setUpBookTable ");
			System.err.println(e.getMessage() + " ... setupDatase");
		} finally {

		}
    }
    
    
    public static void setUpIssueTable() {
    	String TABLE_NAME = "issue";
    	
    	try {
    		stmt = conn.createStatement();
    		
    		DatabaseMetaData dbm = conn.getMetaData();
    		ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
    		
    		if (tables.next()) {
    			System.out.println("Table " + TABLE_NAME + " Allready exists. Ready for go!");
    		} else {
    			String sql = "CREATE TABLE `library_assistant`.`" + TABLE_NAME +"` (\r\n" + 
    					"   `book_id` VARCHAR(200) NULL,\r\n" + 
    					"  `member_id` VARCHAR(200) NULL,\r\n" + 
    					"  `issue_time` DATETIME NULL DEFAULT NOW(),\r\n" + 
    					"  `renew_count` INT NULL,\r\n" + 
    					"  INDEX `book_id_idx` (`book_id` ASC) VISIBLE,\r\n" + 
    					"  INDEX `member_id_idx` (`member_id` ASC) VISIBLE,\r\n" + 
    					"  CONSTRAINT `book_id`\r\n" + 
    					"    FOREIGN KEY (`book_id`)\r\n" + 
    					"    REFERENCES `mydb`.`book` (`book_id`)\r\n" + 
    					"    ON DELETE NO ACTION\r\n" + 
    					"    ON UPDATE NO ACTION,\r\n" + 
    					"  CONSTRAINT `member_id`\r\n" + 
    					"    FOREIGN KEY (`member_id`)\r\n" + 
    					"    REFERENCES `mydb`.`membre` (`member_id`)\r\n" + 
    					"    ON DELETE NO ACTION\r\n" + 
    					"    ON UPDATE NO ACTION)\r\n" + 
    					" ENGINE = InnoDB\r\n" + 
    					" DEFAULT CHARACTER SET = utf8mb4;";
    			stmt.execute(sql);				
    			System.out.println("create table " + TABLE_NAME);
    		}
    	} catch (SQLException e) {
    		System.out.println("erreur setUpBookTable ");
    		System.err.println(e.getMessage() + " ... setupDatase");
    	} finally {
    		
    	}
    }
    
    public void checkData() {
    	try {
    		String sql = "SELECT title,author, book_id FROM book";
    		ResultSet rs = executeQuery(sql);
			while (rs.next()) {
				String title = rs.getString("title");
				String book_id = rs.getString("book_id");
				String author = rs.getString("author");
				System.out.printf("%s from %s, book ID: %s%n", title,author,book_id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * requête sans paramètres retourn le resultSet
     * @param query
     * @return 
     */
    public ResultSet executeQuery(String query) {
    	ResultSet result;
    	try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
		} catch (SQLException ex) {
			System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
			return null;
		} finally {
			
		}
    	return result;
    }
    
    /**
     * requête simple sans paramètres 
     * @param query
     * @return
     */
    public boolean execAction(String query) {
        try {
            stmt = conn.createStatement();
            stmt.execute(query);
            return true;
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        }
        finally {
        }
    }

	public boolean updateBook(String _bookID, String _bookTitle, String _bookAuthor, String _publisher) {
		String sqlUpdate = "UPDATE book SET title=?, author=?, publisher=? WHERE book_id=?";
		System.out.printf("UPDATE book SET title=%s, author=%s, publisher=%s WHERE book_id=%s%n",
				_bookTitle, _bookAuthor, _publisher, _bookID);
		long resultUp;
		
		try {
			conn.setAutoCommit(false);

			prepstmt = conn.prepareStatement(sqlUpdate, Statement.RETURN_GENERATED_KEYS);
			prepstmt.setString(1, _bookTitle);
			prepstmt.setString(2, _bookAuthor);
			prepstmt.setString(3, _publisher);
			prepstmt.setString(4, _bookID);
			
			// renvoi 0 si pas d'update
			resultUp = prepstmt.executeUpdate();
			
			conn.commit();
			 
			return resultUp > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback(); 	//<<<<<<<<<<<<<<<<
				System.out.println("rollbak deletedIssue ");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	
			return false;
		}
		
	}



}
