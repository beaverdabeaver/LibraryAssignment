package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Author;
import misc.AlertHelper;

public class AuthorDetailController implements Initializable{
	
	private String origFirstName;
	private String origLastName;
	private LocalDate origDate;
	private String origGender;
	private String origWebsite;
	
	private static Logger logger;
	private Author author;
	
	@FXML private TextField firstName;
	@FXML private TextField lastName;
	@FXML private DatePicker dob;
	@FXML private TextField gender;
	@FXML private TextField web;
	@FXML private Button saveButton;

	public AuthorDetailController(Author author) {
		logger = LogManager.getLogger();
		this.author = author;
	}
	 
	@FXML
	void saveButtonClicked(MouseEvent event){
		logger.info("SAVE button clicked");
		
		//Validate each field
		//Prompt if one fails and return to original value
		if(!author.isValidFirstName(author.getFirstName())) {
    		logger.error("Invalid Firstname:\t\"" + author.getFirstName() + "\"");
    		AlertHelper.showWarningMessage("SAVE ERROR", "Firstname is invalid", "Firstnames must be 1-100 characters.");
    		firstName.textProperty().set(origFirstName);
		}
		else if(!author.isValidLastName(author.getLastName())) {
    		logger.error("Invalid Lastname:\t\"" + author.getLastName() + "\"");
    		AlertHelper.showWarningMessage("SAVE ERROR", "Lastname is invalid", "Lastnames must be 1-100 characters.");
    		lastName.textProperty().set(origLastName);
		}
		else if(!author.isValidDOB(author.getDob())) {
    		logger.error("Invalid Date:\t\"" + author.getDob().toString() + "\"");
    		AlertHelper.showWarningMessage("SAVE ERROR", "Date is invalid", "Date must be before the current date.");
    		dob.valueProperty().set(origDate);
		}
		else if(!author.isValidGender(author.getGender())) {
    		logger.error("Invalid Gender:\t\"" + author.getGender() + "\"");
    		AlertHelper.showWarningMessage("SAVE ERROR", "Gender is invalid", "While we can not assume your gender, we can reject it.");
    		gender.textProperty().set(origGender);
		}
		else if(!author.isValidWebsite(author.getWebsite())) {
    		logger.error("Invalid Weabsite:\t\"" + author.getWebsite() + "\"");
    		AlertHelper.showWarningMessage("SAVE ERROR", "Website is invalid", "Website must be no more than 100 characters.");
    		web.textProperty().set(origWebsite);
		}
		else {
			author.save();
		}
	}

	public void initialize(URL arg0, ResourceBundle arg1) {
		//Save the initial values
		origFirstName = author.getFirstName();
		origLastName = author.getLastName();
		origDate = author.getDob();
		origGender = author.getGender();
		origWebsite = author.getWebsite();
		
		//bind the values to the author object
		firstName.textProperty().bindBidirectional(author.firstNameProperty());
		lastName.textProperty().bindBidirectional(author.lastNameProperty());
		dob.valueProperty().bindBidirectional(author.dobProperty());
		gender.textProperty().bindBidirectional(author.genderProperty());
		web.textProperty().bindBidirectional(author.websiteProperty());
	}
}
