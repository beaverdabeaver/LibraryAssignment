package controller;

import java.net.URL;
import java.sql.Connection;
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
			books = gateway.getBooks();
			bookList.setItems(books);

		} catch (Exception e) {	e.printStackTrace(); }
	}
	@FXML
	void onSearchButtonClicked(MouseEvent event) {
		String searchString = searchField.getText();

		//update the list view
		try {
			if(searchString.length() > 0){
				books = gateway.getBooks(searchString);
				bookList.setItems(books);
			}
			else{
				books = gateway.getBooks();
				bookList.setItems(books);
			}
		} catch (Exception e) {	e.printStackTrace(); }
	}

	@FXML
	void onBookListClicked(MouseEvent click) throws Exception {

		if (click.getClickCount() == 2) {
			Book book = bookList.getSelectionModel().getSelectedItem();
			logger.info(book.getTitle() + " double clicked");
			controller.changeView("/view/BookDetailView.fxml", new BookDetailController(book, this.rootPane), rootPane);
	    }
	}

	public void initialize(URL location, ResourceBundle resources) {
		bookList.setItems(books);
	}
}
