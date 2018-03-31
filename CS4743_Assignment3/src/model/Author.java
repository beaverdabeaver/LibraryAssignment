package model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import db.AuthorTableGateway;

public class Author {
	
	public int id;
	public SimpleStringProperty firstName;
	public SimpleStringProperty lastName;
	public SimpleObjectProperty<LocalDate> dob;
	public SimpleStringProperty gender;
	public SimpleStringProperty website;
	
	private AuthorDelegate helper = new AuthorDelegate();
	private AuthorTableGateway gateway;

	public Author() {
		id = 0;
		firstName = new SimpleStringProperty();
		lastName = new SimpleStringProperty();
		dob = new SimpleObjectProperty<LocalDate>();
		gender = new SimpleStringProperty();
		website = new SimpleStringProperty();
		
		firstName.set("");
		lastName.set("");
		dob.set(LocalDate.now());
		gender.set("");
		website.set("");
	}
	
	public Author(String fName, String lName, Date date, String gen, String web) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		firstName.set(fName);
		lastName.set(lName);
		dob.set(LocalDate.parse(date.toString(), formatter));
		gender.set(gen);
		website.set(web);
	}
	
	private String normalizeName(String name) {
		if(name.length() >= 2) {
			name.toLowerCase();
			return name.substring(0, 1).toUpperCase() + name.substring(1);
		}
		else if(name.length() == 1)
			return name.toUpperCase();
		else
			return name;
	}

	public String toString() {
		return firstName.get() + " " + lastName.get();
	}
	
	public void save() {
		if(id == 0) {
			helper.insertAuthor(this);
		}
		else
			helper.saveAuthor(this);
	}
	
	public void delete() {
		helper.deleteAuthor(this);
	}
	
	//getters
	public int getId() { return id; }
	
	public String getFirstName() { return normalizeName(firstName.get()); }

	public String getLastName() { return normalizeName(lastName.get()); }

	public LocalDate getDob() { return dob.get(); }
	
	public Date getDobDate() { 	return java.sql.Date.valueOf(dob.get()); }

	public String getGender() { return normalizeName(gender.get()); }

	public String getWebsite() { return website.get(); }

	public AuthorTableGateway getGateway() { return gateway; }
	
	//property getters
	public SimpleStringProperty firstNameProperty() { return firstName; }
	
	public SimpleStringProperty lastNameProperty() {return lastName; }
	
	public SimpleObjectProperty<LocalDate> dobProperty() { return dob; }
	
	public SimpleStringProperty genderProperty() {return gender; }
	
	public SimpleStringProperty websiteProperty() {return website; }

	//setters
	public void setId(int id) {	this.id = id; }

	public void setFirstName(String firstName) { this.firstName.set(firstName); }

	public void setLastName(String lastName) { this.lastName.set(lastName); }

	public void setDob(Date dob) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.dob.set(LocalDate.parse(dob.toString(), formatter));
	}

	public void setGender(String gender) {	this.gender.set(gender); }

	public void setWebsite(String website) { this.website.set(website); }
	
	public void setGateway(AuthorTableGateway gateway) { this.gateway = gateway; }
	
	//Validators
	public boolean isValidID(int id) {return helper.isValidID(id); }
	
	public boolean isValidFirstName(String fName) { return helper.isValidFirstName(fName); }
	
	public boolean isValidLastName(String lName) { return helper.isValidLastName(lName); }
	
	public boolean isValidDOB(LocalDate date) { return helper.isValidDOB(date); }
	
	public boolean isValidGender(String gender) { return helper.isValidGender(gender); }
	
	public boolean isValidWebsite(String web) { return helper.isValidLastWebsite(web); }
}
