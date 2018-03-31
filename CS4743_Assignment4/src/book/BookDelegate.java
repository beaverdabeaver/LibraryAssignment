package book;

import java.util.Calendar;
import db.BookTableGateway;
import db.GatewayDistributer;

public class BookDelegate {
	private GatewayDistributer distributer;
	private BookTableGateway gateway;
	
	public BookDelegate() {
		distributer = GatewayDistributer.getInstance();
		gateway = distributer.getBookGateway();
	}
	
	public void saveBook(Book book) {
		try {
			gateway.updateBook(book);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void insertBook(Book book) {		 
		try {
			gateway.insertBook(book);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void deleteBook(Book book) {
		try {
			gateway.deleteBook(book);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public boolean isValidTitle(String title) {
		return (title.length() > 0 && title.length() <=255);
	}
	
	public boolean isValidSummary(String summary) {
		return (summary.length() < 65536);
	}
	
	public boolean isValidPublished(int yearPublished) {
		return (yearPublished <= Calendar.getInstance().get(Calendar.YEAR));
	}
	
	public boolean isValidIsbn(String isbn) {
		return (isbn.length() <= 13);
	}
	
	/**
	public boolean isValidDateAdded(Date dateAdded) {
		
	}
	**/

}
