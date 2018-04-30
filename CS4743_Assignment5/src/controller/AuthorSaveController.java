package controller;

import java.net.URL;
import java.util.ResourceBundle;
import book.Book;
import db.AuthorTableGateway;
import db.GatewayDistributer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import misc.AlertHelper;
import model.Author;
import model.AuthorBook;

public class AuthorSaveController implements Initializable{

    @FXML private TextField royaltyTextFieldID;
    @FXML private ComboBox<Author> authorComboID;
    @FXML private Button saveButtonID;
    @FXML private Button cancelButtonID;
    @FXML private Label titleID;
    private BorderPane rootPane;
    
    private GatewayDistributer distributer;
    private ControllerSingleton controller = ControllerSingleton.getInstance();
	private AuthorTableGateway authorGateway;
	
	private Book book;
	private ObservableList<Author> authors;
    
    public AuthorSaveController(Book book, BorderPane rootPane) throws Exception {
    	this.rootPane = rootPane;
    	
    	distributer = GatewayDistributer.getInstance();
		authorGateway = distributer.getAuthorGateway();
		authors = authorGateway.getAuthors(); 
		
		this.book = book;
    }

    @FXML
    void cancelButtonClicked(MouseEvent event) throws Exception {
    	controller.changeView("/view/BookAdditionalView.fxml", new BookAdditionalController(book, rootPane), rootPane);
    }

    @FXML
    void saveButtonClicked(MouseEvent event) throws Exception {
    	if(book.getId() == 0){
    		AlertHelper.showWarningMessage("BOOK ERROR", "Book not saved", "Please save the current book before adding an author.");
    		return;
    	}
    	Author selectedAuthor = authorComboID.getSelectionModel().getSelectedItem();
    	String royalty = royaltyTextFieldID.getText();
    	
    	if(selectedAuthor == null) {
    		AlertHelper.showWarningMessage("AUTHOR ERROR", "Null Author selected", "Please select an author from the dropdown menu.");
    		return;
    	}
    	if(!book.isValidRelationship(selectedAuthor)) {
    		AlertHelper.showWarningMessage("AUTHOR ERROR", "Relationship already established", "This book already has this author listed.");
    		return;
    	}
    	if(book.isValidRoyalty(royalty)) {
    		AuthorBook authorBook = new AuthorBook();
    		royalty = royalty.replace("%", "");
    		double num = Double.parseDouble(royalty);
    		int iRoyalty = (int)num * 1000;
    		
    		authorBook.setRoyalty(iRoyalty);
    		authorBook.setAuthor(selectedAuthor);
    		authorBook.setBook(book);
    		authorBook.setNewRecord(false);
    		
    		book.addAuthor(authorBook);
    		controller.changeView("/view/BookAdditionalView.fxml", new BookAdditionalController(book, rootPane), rootPane);
    	}
    }
    
    public void initialize(URL arg0, ResourceBundle arg1) {
    	authorComboID.setItems(authors);
    	titleID.setText(book.getTitle());
    	//authorComboID.setValue(book.getPublisher());
	}
}