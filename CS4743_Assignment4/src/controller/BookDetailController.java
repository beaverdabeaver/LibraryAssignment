package controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import book.Book;
import book.Publisher;
import db.ConnectionFactory;
import db.GatewayDistributer;
import db.PublisherTableGateway;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.NumberStringConverter;
import misc.AlertHelper;
import model.AuditTrailEntry;

public class BookDetailController implements Initializable{
	private GatewayDistributer distributer;

	private static Logger logger;
	private Book book;
	private Book oldBook;
	private BorderPane rootPane;
	private PublisherTableGateway pubGateway;
	private ObservableList<Publisher> publishers;
	private ControllerSingleton controller = ControllerSingleton.getInstance();

	@FXML private TextField title;
	@FXML private TextField publisher;
	@FXML private TextField year_published;
	@FXML private TextField isbn;
	@FXML private DatePicker date_added;
	@FXML private TextArea summary;
	@FXML private Button saveButton;
    @FXML private ComboBox<Publisher> publishersCombo;

	public BookDetailController(Book book, BorderPane rootPane) throws Exception {
		distributer = GatewayDistributer.getInstance();

		pubGateway = distributer.getPublisherGateway();
		publishers = pubGateway.getPublishers();
		logger = LogManager.getLogger();

		this.book = book;
		oldBook = new Book();
		oldBook.setTitle(book.getTitle());
		oldBook.setSummary(book.getSummary());
		oldBook.setYearPublished(book.getYearPublished());
		oldBook.setIsbn(book.getIsbn());
		this.rootPane = rootPane;
	}

	@FXML
	void saveButtonClicked(MouseEvent event){
		logger.info("SAVE button clicked");

		//Validate each field
		//Prompt if one fails and return to original value
		if(!book.isValidTitle(book.getTitle())) {
    		logger.error("Invalid Title:\t\"" + book.getTitle() + "\"");
    		AlertHelper.showWarningMessage("SAVE ERROR", "Title is invalid", "Titles must be 1-255 characters.");
    		//firstName.textProperty().set(origFirstName);
		}
		else if(!book.isValidSummary(book.getSummary())) {
    		logger.error("Invalid Summary:\t\"" + book.getSummary() + "\"");
    		AlertHelper.showWarningMessage("SAVE ERROR", "Summary is invalid", "Book summaries must be less than 65,536 characters.");
    		//lastName.textProperty().set(origLastName);
		}
		else if(!book.isValidPublished(book.getYearPublished())) {
    		logger.error("Invalid Gender:\t\"" + book.getYearPublished() + "\"");
    		AlertHelper.showWarningMessage("SAVE ERROR", "Year published is invalid", "The year published cannot be after the current year.");
    		//gender.textProperty().set(origGender);
		}
		else if(!book.isValidIsbn(book.getIsbn())) {
    		logger.error("Invalid ISBN:\t\"" + book.getIsbn() + "\"");
    		AlertHelper.showWarningMessage("SAVE ERROR", "ISBN is invalid", "ISBN cannot be less than 13 characters.");
    		//web.textProperty().set(origWebsite);
		}
		else {
			if(!oldBook.getTitle().equals(book.getTitle())){
				logger.info("title changed!");
				try {
					distributer.getBookGateway().insertAudit("title changed from " 
							+ oldBook.getTitle() + " to " + book.getTitle(), this.book);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!oldBook.getSummary().equals(book.getSummary())){
				logger.info("summary changed!");
				try {
					distributer.getBookGateway().insertAudit("summary changed from " 
							+ oldBook.getSummary() + " to " + book.getSummary(), this.book);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!(oldBook.getYearPublished() == (book.getYearPublished()))){
				logger.info("year changed!");
				try {
					distributer.getBookGateway().insertAudit("year changed from " 
							+ oldBook.getYearPublished() + " to " + book.getYearPublished(), this.book);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!oldBook.getIsbn().equals(book.getIsbn())){
				logger.info("isbn changed!");
				try {
					distributer.getBookGateway().insertAudit("ISBN changed from " 
							+ oldBook.getIsbn() + " to " + book.getIsbn(), this.book);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			book.setPublisher(publishersCombo.getSelectionModel().getSelectedItem());
			book.save();
		}
	}

	@FXML
	void auditButtonClicked(MouseEvent event){
		logger.info("audit button clicked");
		ObservableList<AuditTrailEntry> audit = null;
		try {
			audit = distributer.getBookGateway().getAudits(book.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(book.getId() == 0){
			AlertHelper.showWarningMessage("Did you save?", "Did ya?", "Did ya?");
			return;
		}
		controller.changeView("/view/AuditTrailView.fxml", new BookAuditTrailController(audit, book, rootPane), rootPane);
	}

	public void initialize(URL arg0, ResourceBundle arg1) {
		publishersCombo.setItems(publishers);
		publishersCombo.setValue(book.getPublisher());

		title.textProperty().bindBidirectional(book.getTitleProperty());
		//publisher.textProperty().bindBidirectional(book.getPublisher().getPublisherNameProperty());
		year_published.textProperty().bindBidirectional(book.getYearPublishedProperty(), new NumberStringConverter("####"));
		isbn.textProperty().bindBidirectional(book.getIsbnProperty());
		date_added.valueProperty().bindBidirectional(book.getDateAddedProperty());
		summary.textProperty().bindBidirectional(book.getSummaryProperty());
	}
}
