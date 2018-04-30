package book;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import db.BookTableGateway;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import misc.AlertHelper;
import model.Author;
import model.AuthorBook;

public class Book {	
	private BookDelegate helper;
	
	public int id;
	public SimpleStringProperty title;
	public SimpleStringProperty summary;
	public SimpleIntegerProperty yearPublished;
	public SimpleObjectProperty<Publisher> publisher;
	public SimpleStringProperty isbn;
	public SimpleObjectProperty<LocalDate> dateAdded;
	
	public SimpleIntegerProperty publisherID;
	
	private BookTableGateway gateway;
	
	public Book() {
		helper = new BookDelegate();
		
		id = 0;
		title = new SimpleStringProperty();
		summary = new SimpleStringProperty();
		yearPublished = new SimpleIntegerProperty();
		publisher = new SimpleObjectProperty<Publisher>();
		isbn = new SimpleStringProperty();
		dateAdded = new SimpleObjectProperty<LocalDate>();
		publisherID = new SimpleIntegerProperty();
		
		title.set("");
		summary.set("");
		yearPublished.set(Calendar.getInstance().get(Calendar.YEAR));
		publisher.set(new Publisher());
		isbn.set("");
		dateAdded.set(LocalDate.now());
		publisherID.set(1);
	}
	
	public String toString() {
		return title.get();
	}
	
	public void save(){
		
		if(id == 0) {
			helper.insertBook(this);
		}
		else
			helper.saveBook(this);
	}
	
	public void delete(){
		helper.deleteBook(this);
	}
	
	public void deleteAuthorRelationship(AuthorBook authorBook) throws Exception {
		gateway.deleteAuthorRelationship(authorBook);
	}
	
	public void updateAuthorRoyalty(AuthorBook authorBook, String oldRoyalty, int newRoyalty) throws Exception {
		gateway.updateAuthorRoyalty(authorBook, oldRoyalty ,newRoyalty);
	}
	
	public void addAuthor(AuthorBook authorBook) throws Exception {
		gateway.addAuthor(authorBook);
	}
	
	public boolean isValidRelationship(Author author) throws Exception {
		List<AuthorBook> list = new ArrayList<AuthorBook>();
		try {
			list = getAuthors();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		Iterator it = list.iterator();
		
		while(it.hasNext()) {
			AuthorBook author2 = (AuthorBook)it.next();
			if(author2.getAuthor().getId() == author.getId()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isValidRoyalty(String newRoyalty) {
    	double num = 0;
    	newRoyalty = newRoyalty.replace("%", "");
    	try {
    		num = Double.parseDouble(newRoyalty);
    	}
    	catch(NumberFormatException e) {
    		AlertHelper.showWarningMessage("CHANGE ERROR", "Incorrect Syntax", "Please enter a new percent value in the form of \"X.X%\"  OR  \"X.X\"");
    		return false;
    	}
    	if(num > 100.00 || num < 0.0) {
    		AlertHelper.showWarningMessage("CHANGE ERROR", "Incorrect Value", "Please enter a new percent value between 0% - 100%");
    		return false;
    	}
    	return true;
    }
	
	//prints the firstname of all authors associated with a book (used for testing purposes)
	public void printAuthors() throws Exception {
		List<AuthorBook> list = getAuthors();
		Iterator it = list.iterator();
		
		while(it.hasNext()) {
			AuthorBook auth = (AuthorBook)it.next();
			System.out.println(auth.getAuthor().getFirstName());
		}
	}
		
	//getters
	public int getId() { return id;	}

	public String getTitle() {return title.get(); }

	public String getSummary() { return summary.get(); }

	public int getYearPublished() { return yearPublished.get(); }

	public Publisher getPublisher() { return publisher.get(); }

	public String getIsbn() { return isbn.get(); }

	public Date getDateAdded() { return java.sql.Date.valueOf(dateAdded.get()); }
	
	public BookTableGateway getBookGateway() { return gateway;}
	
	public List<AuthorBook> getAuthors() throws Exception{
		return gateway.getAuthors(this);
	}
	
	//property getters
	public SimpleStringProperty getTitleProperty() { return title; }

	public SimpleStringProperty getSummaryProperty() { return summary; }

	public SimpleIntegerProperty getYearPublishedProperty() { return yearPublished; }

	public SimpleObjectProperty<Publisher> getPublisherProperty() { return publisher; }

	public SimpleStringProperty getIsbnProperty() { return isbn; }

	public SimpleObjectProperty<LocalDate> getDateAddedProperty() { return dateAdded; }
	
	//setters
	public void setId(int id) { this.id = id; }

	public void setTitle(String title) { this.title.set(title); }

	public void setSummary(String summary) { this.summary.set(summary); }

	public void setYearPublished(int yearPublished) { this.yearPublished.set(yearPublished); }

	public void setPublisher(Publisher publisher) { this.publisher.set(publisher); }

	public void setIsbn(String isbn) { this.isbn.set(isbn); }
	
	public void setBookGateway(BookTableGateway gateway) { this.gateway = gateway;}


	//validators
	public boolean isValidTitle(String title) { return (title.length() > 0 && title.length() <=255); }
	
	public boolean isValidSummary(String summary) { return (summary.length() < 65536); }
	
	public boolean isValidPublished(int yearPublished) { return (yearPublished <= Calendar.getInstance().get(Calendar.YEAR)); }
	
	public boolean isValidIsbn(String isbn) { return (isbn.length() <= 13); }
	
}
