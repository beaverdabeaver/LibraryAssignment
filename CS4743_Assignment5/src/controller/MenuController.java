package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import book.Book;
import book.Publisher;
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
import model.AuthorBook;

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
	@FXML private MenuItem menuItemGenerate;
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
			books = bookGateway.setFirstPage(true);
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
		//Generate Books
		/*
		else if(event.getSource() == menuItemGenerate){
			System.out.println("Generate Clicked!");
			generateBooks();
		}*/
	}

	public void initialize(URL location, ResourceBundle resources) {
		menuBar.setFocusTraversable(true);
	}
/*
	private void generateBooks() throws Exception{
		System.out.println("generateBooks method reached!");
		//System.out.println(ThreadLocalRandom.current().nextInt(1, 5 + 1));
		
		Book book;
		AuthorBook authorBook;
		int val = 0;
		while(val < 201){
		try (BufferedReader br = new BufferedReader(new FileReader("booktitles.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null && val < 201) {
		    	book = new Book();
		    	authorBook = new AuthorBook();
		    	book.setTitle(line);
		    	book.setSummary("This was a randomly generated book lol");
		    	book.setPublisher(distributer.getPublisherGateway().getPublisherById( ThreadLocalRandom.current().nextInt(1, 5 + 1)));
		    	book.setIsbn("1234567890123");
		    	authorBook.setBook(book);
		    	authorBook.setAuthor(authorGateway.getAuthor(24 + ThreadLocalRandom.current().nextInt(1, 5 + 1)));
		    	authorBook.setRoyalty(ThreadLocalRandom.current().nextInt(1, 5 + 1)*10000);
		    	bookGateway.insertBook(book);
		    	bookGateway.addAuthor(authorBook);
		    }
		}
		val++;
		}
		
	}*/
}