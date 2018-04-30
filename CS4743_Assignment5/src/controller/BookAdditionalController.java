package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import book.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import misc.AlertHelper;
import model.AuthorBook;

public class BookAdditionalController implements Initializable{
	
	@FXML private TableView<AuthorBook> table;
	@FXML private TableColumn<AuthorBook, String> authorColumn;
	@FXML private TableColumn<AuthorBook, String> royaltyColumn;
	    
    @FXML private Label titleLabel;
    @FXML private Button addAuthorButtonID;
    @FXML private Button deleteAuthorButtonID;
    @FXML private Button backButtonID;
    
   
    private ControllerSingleton controller = ControllerSingleton.getInstance();
    private BorderPane rootPane;
    private Book book;
    private ObservableList<AuthorBook> aList;
    
    public BookAdditionalController(Book book, BorderPane rootPane) throws Exception {
    	table = new TableView<>();
    	
    	this.book = book;
    	this.rootPane = rootPane;
    	List<AuthorBook> placeholder =  new ArrayList<AuthorBook>();
    	
    	try{
    		placeholder = book.getAuthors();
    	}catch(Exception e){
    	
    	}
    	
    	aList = FXCollections.observableArrayList(placeholder);
    
    }

    @FXML
    void addAuthorClicked(MouseEvent event) throws Exception {
    	controller.changeView("/view/AuthorSaveView.fxml", new AuthorSaveController(book, rootPane), rootPane);
    }

    @FXML
    void deleteAuthorClicked(MouseEvent event) throws Exception {
    	try {
    		if(aList.size() < 2) {
    			AlertHelper.showWarningMessage("DELETE ERROR", "Book must have author!", "All books must have at least one author.");
    		}
    		else {
    			AuthorBook selectedBook = table.getSelectionModel().getSelectedItem();
    			book.deleteAuthorRelationship(selectedBook);
    		
    			//update the table
    			table.getItems().clear();
    			aList = FXCollections.observableArrayList(book.getAuthors());
    			table.getItems().addAll(aList);
    		}
    	} catch(Exception e) {}
    }
    
    @FXML
    void editComplete(CellEditEvent event) throws Exception {
    	String newValue = event.getNewValue().toString();
    	double num = 0;
    	if(book.isValidRoyalty(newValue)) {
    		newValue = newValue.replace("%", "");
    		num = Double.parseDouble(newValue);
    		int iRoyalty = (int)(num * 10000);
    		
    		AuthorBook selectedBook = table.getSelectionModel().getSelectedItem();
    		String oldRoyalty = selectedBook.getRoyalty();
    		selectedBook.setRoyalty(iRoyalty);
    		book.updateAuthorRoyalty(selectedBook, oldRoyalty, iRoyalty);
    	}
    }
    
    @FXML
    void backButtonClicked(MouseEvent event) throws Exception {
    	controller.changeView("/view/BookDetailView.fxml", new BookDetailController(book, rootPane), rootPane);
    }
       
	public void initialize(URL arg0, ResourceBundle arg1) {
		titleLabel.setText(book.getTitle());
		
		authorColumn.setCellValueFactory(new PropertyValueFactory<AuthorBook, String>("Author"));
		royaltyColumn.setCellValueFactory(new PropertyValueFactory<AuthorBook, String>("Royalty"));
		
		table.setEditable(true);
		royaltyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		table.getItems().addAll(aList);
	}
}