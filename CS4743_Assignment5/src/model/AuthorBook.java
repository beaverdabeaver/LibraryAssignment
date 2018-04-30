package model;

import book.Book;

public class AuthorBook {
	
	private Author author;
	private Book book;
	private int royalty;
	private boolean newRecord;
	
	public AuthorBook() {
		royalty = 0;
		newRecord = true;
	}
	
	//setters
	public void setAuthor(Author author) {
		this.author = author;
	} 
	public void setBook(Book book) {
		this.book = book;
	} 
	
	public void setRoyalty(int royalty) {
		this.royalty = royalty;
	}
	
	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}
	
	//getters
	public Author getAuthor() {
		return author;
	}
	
	public Book getBook() {
		return book;
	}

	public String getRoyalty() {
		return getRoyaltyPercent();
	}
	
	public int getIntRoyalty() {
		return royalty;
	}
	
	public String getRoyaltyPercent() {
		double dRoyalty = (double) royalty;
		dRoyalty /= 10000;
		String s = Double.toString(dRoyalty);
		s = s.indexOf(".") < 0 ? s : s.replaceAll("0*$", "").replaceAll("\\.$", "");
		
		return s+"%";
	}

	public boolean isNewRecord() {
		return newRecord;
	}
}
