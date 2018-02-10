

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String fName;
	private String lName;
	private String emailId;
	private String password;
	private String userId;
	private ArrayList<String> tables = new ArrayList<String>();
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String emailId, String firstName, String lastName, String userId, String password) {
		super();
		this.fName = firstName;
		this.lName = lastName;
		this.emailId = emailId;
		this.password = password;
		this.userId = userId;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String firstName) {
		this.fName = firstName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lastName) {
		this.lName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public ArrayList<String> getTables() {
		return tables;
	}

	public void setTables(ArrayList<String> Tables) {
		this.tables = Tables;
	}
	
}
