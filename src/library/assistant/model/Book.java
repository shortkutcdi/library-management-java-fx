package library.assistant.model;


import javafx.beans.property.*;

public class Book {
	
	private final StringProperty title;
	private final StringProperty bookId;
	private final StringProperty author;
	private final StringProperty publisher;
	private final IntegerProperty availablity;
	
	public Book(String title, String bookId, String author, String publisher,
			Integer availablity) {
		this.title = new SimpleStringProperty(title);
		this.bookId = new SimpleStringProperty(bookId);
		this.author = new SimpleStringProperty(author);
		this.publisher = new SimpleStringProperty(publisher);
		this.availablity = new SimpleIntegerProperty(availablity);
	}
	
	public final StringProperty titleProperty() {
		return this.title;
	}
	
	public final String getTitle() {
		return this.titleProperty().get();
	}
	
	public final void setTitle(final String title) {
		this.titleProperty().set(title);
	}
	
	public final StringProperty bookIdProperty() {
		return this.bookId;
	}
	
	public final String getBookId() {
		return this.bookIdProperty().get();
	}
	
	public final void setBookId(final String bookId) {
		this.bookIdProperty().set(bookId);
	}
	
	public final StringProperty authorProperty() {
		return this.author;
	}
	
	public final String getAuthor() {
		return this.authorProperty().get();
	}
	
	public final void setAuthor(final String author) {
		this.authorProperty().set(author);
	}
	
	public final StringProperty publisherProperty() {
		return this.publisher;
	}
	
	public final String getPublisher() {
		return this.publisherProperty().get();
	}
	
	public final void setPublisher(final String publisher) {
		this.publisherProperty().set(publisher);
	}
	
	public final IntegerProperty availablityProperty() {
		return this.availablity;
	}
	
	public final int getAvailablity() {
		return this.availablityProperty().get();
	}
	
	public final void setAvailablity(final int availablity) {
		this.availablityProperty().set(availablity);
	}

	@Override
	public String toString() {
		return "Book [title=" + getTitle() + ", bookId=" + getBookId() + ", author=" + getAuthor() + ", publisher=" + getPublisher()
				+ ", availablity=" + getAvailablity() + "]";
	}

	
}
