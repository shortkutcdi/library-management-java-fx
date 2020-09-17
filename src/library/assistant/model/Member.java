package library.assistant.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Member {
	
	private final StringProperty name;
	private final StringProperty memberId;
	private final StringProperty mobile;
	private final StringProperty email;
	
	public Member(String name, String memberId, String mobile, String email) {
		this.name = new SimpleStringProperty(name);
		this.memberId = new SimpleStringProperty(memberId);
		this.mobile = new SimpleStringProperty(mobile);
		this.email = new SimpleStringProperty(email);
	}

	public final StringProperty nameProperty() {
		return this.name;
	}
	

	public final String getName() {
		return this.nameProperty().get();
	}
	

	public final void setName(final String name) {
		this.nameProperty().set(name);
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
	

	public final StringProperty mobileProperty() {
		return this.mobile;
	}
	

	public final String getMobile() {
		return this.mobileProperty().get();
	}
	

	public final void setMobile(final String mobile) {
		this.mobileProperty().set(mobile);
	}
	

	public final StringProperty emailProperty() {
		return this.email;
	}
	

	public final String getEmail() {
		return this.emailProperty().get();
	}
	

	public final void setEmail(final String email) {
		this.emailProperty().set(email);
	}

	@Override
	public String toString() {
		return "Member [name=" + getName() + ", memberId=" + getMemberId() + ", mobile=" 
						+ getMobile() + ", email=" + getEmail() + "]";
	}
	
	
}
