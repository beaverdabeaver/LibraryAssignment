package model;

import java.time.LocalDate;
import db.AuthorTableGateway;
import db.GatewayDistributer;

public class AuthorDelegate {
	private GatewayDistributer distributer;
	private AuthorTableGateway gateway;
	
	public AuthorDelegate() {
		distributer = GatewayDistributer.getInstance();
		gateway = distributer.getAuthorGateway();
	}
	
	public void saveAuthor(Author author) {
		try {
			gateway.updateAuthor(author);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void insertAuthor(Author author) {
		try {
			gateway.insertAuthor(author);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void deleteAuthor(Author author) {
		try {
			gateway.deleteAuthor(author);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	//Validators
	public boolean isValidID(int id) {
		return id >=0;
	}
	
	public boolean isValidFirstName(String fName) {
		if (fName.length() > 0 && fName.length() <= 100)
			return true;
		return false;
	}

	public boolean isValidLastName(String lName) {
		if (lName.length() > 0 && lName.length() <= 100)
			return true;
		return false;
	}
	
	public boolean isValidDOB(LocalDate date) {
		LocalDate current = LocalDate.now();
		return date.isBefore(current);
	}
	
	public boolean isValidGender(String gender) {
		gender = gender.toLowerCase();
		if(gender.equals("male") || gender.equals("female") || gender.equals("unknown"))
			return true;
		return false;
	}
	
	public boolean isValidLastWebsite(String web) {
		if (web.length() <= 100)
			return true;
		return false;
	}
}