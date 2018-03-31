package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.AuditTrailEntry;
import model.Author;
import misc.AlertHelper;

public class AuthorDetailController implements Initializable{
	
	private String origFirstName;
	private String origLastName;
	private LocalDate origDate;
	private String origGender;
	private String origWebsite;
	
	private ControllerSingleton controller = ControllerSingleton.getInstance();
	private static Logger logger;
	private Author author;
	private BorderPane rootPane;
	
	@FXML private TextField firstName;
	@FXML private TextField lastName;
	@FXML private DatePicker dob;
	@FXML private TextField gender;
	@FXML private TextField web;
	@FXML private Button saveButton;
	@FXML private Button auditButton;
	
	public AuthorDetailController(Author author, BorderPane rootPane) {
		logger = LogManager.getLogger();
		this.author = author;
		this.rootPane = rootPane;
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
		else if(author.isModified(author.getId(), author.getLastModified())){
			AlertHelper.showWarningMessage("Uh oh!!!", "Someone beat you to it! Time to go back to the Author List :(", "That sucks...");
		}
		else {
			if(!author.getFirstName().equals(origFirstName)){
				try {
					author.getGateway().insertAudit("Changed First Name from "+ origFirstName + " to " + author.getFirstName(), author);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!author.getLastName().equals(origLastName)){
				try {
					author.getGateway().insertAudit("Changed Last Name from "+ origLastName + " to " + author.getLastName(), author);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!author.getDob().equals(origDate)){
				try {
					author.getGateway().insertAudit("Changed DOB from "+ origDate.toString() + " to " + author.getDob(), author);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!author.getGender().equals(origGender)){
				try {
					author.getGateway().insertAudit("Changed Gender from "+ origGender + " to " + author.getGender(), author);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!author.getWebsite().equals(origWebsite)){
				try {
					author.getGateway().insertAudit("Changed Website from "+ origWebsite+ " to " + author.getWebsite(), author);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			author.save();
			try {
				this.author.setLastModified(author.getGateway().getAuthorLastModified(author.getId()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	void auditButtonClicked(MouseEvent event){
		logger.info("audit button clicked");
		ObservableList<AuditTrailEntry> audit = null;
		if(author.getId() == 0){
			AlertHelper.showWarningMessage("Did you save?", "Did ya?", "Did ya?");
			return;
		}
		try {
			audit = author.getGateway().getAudits(author.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		controller.changeView("/view/AuditTrailView.fxml", new AuthorAuditTrailController(audit, author, rootPane), rootPane);
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
