package controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import book.Book;
import db.BookTableGateway;
import db.GatewayDistributer;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class BookListViewController implements Initializable{
	
	private GatewayDistributer distributer;
	private BookTableGateway gateway;
	private static Logger logger;
	private ObservableList<Book> books;
	private BorderPane rootPane;
	private ControllerSingleton controller = ControllerSingleton.getInstance();

	@FXML private ListView<Book> bookList;
	@FXML private Button deleteButton;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    
    @FXML private Button firstButton;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Button lastButton;
    
    @FXML private Label pageLabel;

	public BookListViewController(ObservableList<Book> books, BorderPane rootPane) {
		distributer = GatewayDistributer.getInstance();
		gateway = distributer.getBookGateway();

		logger = LogManager.getLogger();
		this.books = books;
		this.rootPane = rootPane;
	}

	@FXML void deleteButtonClicked(MouseEvent event) {
		Book selectedBook = bookList.getSelectionModel().getSelectedItem();
		selectedBook.delete();

		//update the list view
		try {
			books = gateway.setFirstPage(false);
			bookList.setItems(books);

		} catch (Exception e) {	e.printStackTrace(); }
	}
	@FXML
	void onSearchButtonClicked(MouseEvent event) throws Exception {
		String searchString = searchField.getText();
		gateway.setSearchString(searchString);

		//update the list view
		books = gateway.setFirstPage(false);
		bookList.setItems(books);
		pageLabel.setText("Fetched records " + gateway.getStart() + " to " + gateway.getEnd() + " out of " + gateway.getTotalReturnedRecords());
	}

	@FXML
	void onBookListClicked(MouseEvent click) throws Exception {

		if (click.getClickCount() == 2) {
			Book book = bookList.getSelectionModel().getSelectedItem();
			logger.info(book.getTitle() + " double clicked");
			controller.changeView("/view/BookDetailView.fxml", new BookDetailController(book, this.rootPane), rootPane);
	    }
	}
	
	@FXML
	void firstButtonClicked(MouseEvent event) throws Exception {
		books = gateway.getFirstPage();

		if(books != null) {
			bookList.setItems(books);
			pageLabel.setText("Fetched records " + gateway.getStart() + " to " + gateway.getEnd() + " out of " + gateway.getTotalReturnedRecords());
		}
	}

	@FXML
	void lastButtonClicked(MouseEvent event) throws Exception {
		books = gateway.getLastPage();

		if(books != null) {
			bookList.setItems(books);
			pageLabel.setText("Fetched records " + gateway.getStart() + " to " + gateway.getEnd() + " out of " + gateway.getTotalReturnedRecords());
		}
	}

	@FXML
	void nextButtonClicked(MouseEvent event) throws Exception {
		books = gateway.getNextPage();
		
		if(books != null) {
			bookList.setItems(books);
			pageLabel.setText("Fetched records " + gateway.getStart() + " to " + gateway.getEnd() + " out of " + gateway.getTotalReturnedRecords());
		}
	}

	@FXML
	void prevButtonClicked(MouseEvent event) throws Exception {
		books = gateway.getPrevPage();
		
		if(books != null) {
			bookList.setItems(books);
			pageLabel.setText("Fetched records " + gateway.getStart() + " to " + gateway.getEnd() + " out of " + gateway.getTotalReturnedRecords());
		}
	}

	public void initialize(URL location, ResourceBundle resources) {
		bookList.setItems(books);
		pageLabel.setText("Fetched records " + gateway.getStart() + " to " + gateway.getEnd() + " out of " + gateway.getTotalReturnedRecords());
	}
}
