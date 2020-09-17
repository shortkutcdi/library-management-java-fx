package library.assistant.ui.settings;

import java.io.Serializable;

import org.apache.commons.codec.digest.DigestUtils;


public class PreferencesPojo implements Serializable{

	private static final long serialVersionUID = 303403395850274146L;
	
	private transient String variableNonSerialisable;
	private int nbrDaysWithoutFine;
	private float finePerDay;
	private String username;
	private String password;
	
	static{
		System.out.println("PreferencesPojo - Bloc static");
	}
	
	{
		System.out.println("PreferencesPojo - Bloc d'instance");
	}
	
	public PreferencesPojo() {
		System.out.println("PreferencesPojo - Constructeur");
		nbrDaysWithoutFine = 4;
		finePerDay = 20;
		username = "admin";
		setPassword(password="admin");
	}

	public PreferencesPojo(int nbrDaysWithoutFine, float finePerDay, String username, String password) {
		super();
		this.nbrDaysWithoutFine = nbrDaysWithoutFine;
		this.finePerDay = finePerDay;
		this.username = username;
		setPassword(password);;
	}

	public int getNbrDaysWithoutFine() {
		return nbrDaysWithoutFine;
	}

	public void setNbrDaysWithoutFine(int nbrDaysWithoutFine) {
		this.nbrDaysWithoutFine = nbrDaysWithoutFine;
	}

	public float getFinePerDay() {
		return finePerDay;
	}

	public void setFinePerDay(float finePerDay) {
		this.finePerDay = finePerDay;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = DigestUtils.sha1Hex(password);
	}

	@Override
	public String toString() {
		return "PreferencesPojo [nbrDaysWithoutFine=" + nbrDaysWithoutFine + ", finePerDay=" + finePerDay
				+ ", username=" + username + ", password=" + password + "]";
	}

	
}
