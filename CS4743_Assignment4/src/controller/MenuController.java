package controller;

import java.net.URL;
import java.util.ResourceBundle;
import book.Book;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import db.AuthorTableGateway;
import db.BookTableGateway;
import db.GatewayDistributer;
import model.Author;

public class MenuController implements Initializable {
	private GatewayDistributer distributer;

	private AuthorTableGateway authorGateway;
	private BookTableGateway bookGateway;
	private ObservableList<Author> authors;
	private ObservableList<Book> books;
	private ControllerSingleton controller;

	@FXML private MenuBar menuBar;
	@FXML private MenuItem menuItemAddAuthors;
	@FXML private MenuItem menuItemAuthors;
	@FXML private MenuItem menuItemBooks;
	@FXML private MenuItem menuItemAddBook;
	@FXML private MenuItem menuItemQuit;
	@FXML private BorderPane rootPane;

	public MenuController() {
		distributer = GatewayDistributer.getInstance();
		controller = ControllerSingleton.getInstance();

		bookGateway = distributer.getBookGateway();
		authorGateway = distributer.getAuthorGateway();
	}

	@FXML private void handleMenuAction(ActionEvent event) throws Exception {

		//Author list view
		if(event.getSource() == menuItemAuthors) {
			authors = authorGateway.getAuthors();
			controller.changeView("/view/AuthorListView.fxml", new AuthorListViewController(authors, rootPane), rootPane);
		}
		//Add Author
		else if(event.getSource() == menuItemAddAuthors) {
			controller.changeView("/view/AuthorDetailView.fxml", new AuthorDetailController(new Author(), rootPane), rootPane);
		}
		//Book list view
		else if(event.getSource() == menuItemBooks) {
			books = bookGateway.getBooks();
			controller.changeView("/view/BookListView.fxml", new BookListViewController(books, rootPane), rootPane);
		}
		//Add Book
		else if(event.getSource() == menuItemAddBook){
			controller.changeView("/view/BookDetailView.fxml", new BookDetailController(new Book(), rootPane), rootPane);
		}
		//Quit
		else if(event.getSource() == menuItemQuit) {
			authorGateway.closeConnection();
			System.exit(0);
		}
	}

	public void initialize(URL location, ResourceBundle resources) {
		menuBar.setFocusTraversable(true);
	}
}
