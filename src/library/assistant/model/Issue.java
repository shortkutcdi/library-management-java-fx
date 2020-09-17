package library.assistant.model;


import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Issue {
	
	private final StringProperty bookId;
	private final StringProperty memberId;
	
	private final ObjectProperty<Date> issueTime;
	private final ObjectProperty<Integer> renewCount;
	
	public Issue(String bookId, String memberId, Date issueTime,Integer renewCount) {
		super();
		this.bookId = new SimpleStringProperty(bookId);
		this.memberId = new SimpleStringProperty(memberId);
		this.issueTime = new SimpleObjectProperty<>(new Date());
		this.renewCount = new SimpleObjectProperty<Integer>(renewCount);
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
	

	public final StringProperty memberIdProperty() {
		return this.memberId;
	}
	

	public final String getMemberId() {
		return this.memberIdProperty().get();
	}
	

	public final void setMemberId(final String memberId) {
		this.memberIdProperty().set(memberId);
	}
	

	public final ObjectProperty<Date> issueTimeProperty() {
		return this.issueTime;
	}
	

	public final Date getIssueTime() {
		return this.issueTimeProperty().get();
	}
	

	public final void setIssueTime(final Date issueTime) {
		this.issueTimeProperty().set(issueTime);
	}
	

	public final ObjectProperty<Integer> renewCountProperty() {
		return this.renewCount;
	}
	

	public final Integer getRenewCount() {
		return this.renewCountProperty().get();
	}
	

	public final void setRenewCount(final Integer renewCount) {
		this.renewCountProperty().set(renewCount);
	}

	@Override
	public String toString() {
		return "Issue [bookId=" + getBookId() + ", memberId=" + getMemberId() + ", issueTime=" +
					getIssueTime() + ", renewCount=" + getRenewCount() + "]";
	}
	
	
	
}
