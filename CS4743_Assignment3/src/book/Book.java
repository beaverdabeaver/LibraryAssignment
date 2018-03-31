package book;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import db.BookTableGateway;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

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
	

	//getters
	public int getId() { return id;	}

	public String getTitle() {return title.get(); }

	public String getSummary() { return summary.get(); }

	public int getYearPublished() { return yearPublished.get(); }

	public Publisher getPublisher() { return publisher.get(); }

	public String getIsbn() { return isbn.get(); }

	public Date getDateAdded() { return java.sql.Date.valueOf(dateAdded.get()); }
	
	public BookTableGateway getBookGateway() { return gateway;}
	
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

	/**
	public void setDateAdded(Date dateAdded) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.dateAdded.set(LocalDate.parse(dateAdded.toString(), formatter));
	}
	**/
	
	//validators
	public boolean isValidTitle(String title) { return (title.length() > 0 && title.length() <=255); }
	
	public boolean isValidSummary(String summary) { return (summary.length() < 65536); }
	
	public boolean isValidPublished(int yearPublished) { return (yearPublished <= Calendar.getInstance().get(Calendar.YEAR)); }
	
	public boolean isValidIsbn(String isbn) { return (isbn.length() <= 13); }
	
}
