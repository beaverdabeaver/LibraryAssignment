package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.AuditTrailEntry;
import model.AuthorBook;
import book.Book;

public class BookTableGateway {
	
	final int PAGE_SIZE = 50;
	
	private int offset;
	private int start;
	private int end;
	private int totalReturnedRecords;
	private String stringToFind;
	
	private Connection conn;
	private static Logger logger;
	private PublisherTableGateway pubGateway;
	private AuthorBookGateway authorBookGateway;

	public BookTableGateway(Connection conn) {
		offset = 1;
		stringToFind = "";
		totalReturnedRecords = 0;
		
		this.conn = conn;
		logger = LogManager.getLogger();
		pubGateway = new PublisherTableGateway(conn);
		authorBookGateway = new AuthorBookGateway(conn);
	}

	public void closeConnection() {
		logger.info("Closing connection...");

		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Assignment 5 additions
	
	public void setSearchString(String stringToFind) {
		this.stringToFind = stringToFind;
	}
	
	public int getSearchedRecordNumber(String stringToFind) throws Exception{
		PreparedStatement st = conn.prepareStatement(""
				+ "SELECT COUNT(*) "
				+ "AS total "
				+ "FROM Book "
				+ "WHERE title LIKE '%" + stringToFind + "%'");
		
		int numOfRecords = 0;
		try {
			ResultSet rs = st.executeQuery();
			
			
			while(rs.next()) {
				numOfRecords = rs.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new Exception(e);
			}
		}
		return numOfRecords;
	}
	
	private ObservableList<Book> getRecords() throws Exception {
		PreparedStatement st = null;
		calculateStart(offset);
		calculateEnd(offset);
		
		if(end > totalReturnedRecords) {
			end = totalReturnedRecords;
		}

		if(stringToFind.length() > 0) {
			st = conn.prepareStatement("SELECT * FROM Book WHERE title LIKE '%" + stringToFind + "%' LIMIT " + (start-1) + "," + ((start-1) + PAGE_SIZE) + "");
		}	
		else
			st = conn.prepareStatement("SELECT * FROM Book LIMIT " + (start-1) + "," + (end - (start-1)) + "");
		return searchBooks(st);
	}
	
	public ObservableList<Book> setFirstPage(boolean refresh) throws Exception{
		//Refresh means that the book list was left and returned to or being seen for the first time
		if(refresh)
			stringToFind = "";
		
		totalReturnedRecords = getSearchedRecordNumber(stringToFind);
		offset = 1;
		return getRecords();
	}
	
	public ObservableList<Book> getFirstPage() throws Exception{
		if(offset == 1)
			return null;

		offset = 1;
		return getRecords();
	}
	
	public ObservableList<Book> getPrevPage() throws Exception{
		if(offset <= 1)
			return null;
		
		offset--;
		return getRecords();
	}
	public ObservableList<Book> getNextPage() throws Exception{
		int totalRecords = getSearchedRecordNumber(stringToFind);
		if(offset >= (totalRecords/PAGE_SIZE) + 1)
			return null;
		
		offset++;
		return getRecords();
	}
	public ObservableList<Book> getLastPage() throws Exception{
		int totalRecords = getSearchedRecordNumber(stringToFind);
		if(offset >= (totalRecords/PAGE_SIZE) + 1)
			return null;
		
		offset = (totalRecords/PAGE_SIZE) + 1;
		return getRecords();
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public int getTotalReturnedRecords() {
		return totalReturnedRecords;
	}
	
	private void calculateStart(int offset) {
		start = ((PAGE_SIZE * offset) - PAGE_SIZE) + 1;
	}
	
	private void calculateEnd(int offset) {
		end = PAGE_SIZE * offset;
	}
	
	//end Assignment 5 additions
	
	public ObservableList<Book> getBooks() throws Exception{
		PreparedStatement st = conn.prepareStatement("SELECT * FROM `Book` ORDER BY `Book`.`title` ASC");
		
		return searchBooks(st);
	}

	//override the previous methods to accept a searchable string
	public ObservableList<Book> getBooks(String stringToFind) throws Exception{
		PreparedStatement st = conn.prepareStatement("SELECT * FROM Book WHERE title LIKE '%" + stringToFind + "%'");
		getSearchedRecordNumber(stringToFind);
		return searchBooks(st);
	}
	
	public List<Book> getBooksByPublisherID(int pub_id) throws Exception{
		//PreparedStatement st = conn.prepareStatement("SELECT * FROM `Book` WHERE publisher_id = ? ORDER BY `Book`.`title` ASC");
		PreparedStatement st = conn.prepareStatement("SELECT * FROM `Book` WHERE publisher_id = ?");
		st.setInt(1, pub_id);
		List<Book> book  = searchBooks(st);
		return book;
	}

	public ObservableList<AuditTrailEntry> getAudits(int book_id) throws Exception{
		PreparedStatement st = conn.prepareStatement("SELECT * FROM book_audit_trail WHERE book_id = " + book_id + " ORDER BY date_added ASC");
		ObservableList<AuditTrailEntry> audits = FXCollections.observableArrayList();

		try {
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				AuditTrailEntry audit = new AuditTrailEntry();
				audit.setId(rs.getInt("id"));
				audit.setDateAdded(rs.getTimestamp("date_added").toLocalDateTime());
				audit.setMessage(rs.getString("entry_msg"));

				audits.add(audit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new Exception(e);
			}
		}

		return audits;
	}

	public ObservableList<Book> searchBooks(PreparedStatement st) throws Exception{
		ObservableList<Book> books = FXCollections.observableArrayList();

		try {
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Book book = new Book();
				book.setId(rs.getInt("id"));
				book.setTitle(rs.getString("title"));
				book.setSummary(rs.getString("summary"));
				book.setYearPublished((rs.getInt("year_published")));
				book.setIsbn(rs.getString("isbn"));
				book.setPublisher(pubGateway.getPublisherById(rs.getInt("publisher_id")));

				book.setBookGateway(this);
				books.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new Exception(e);
			}
		}

		return books;
	}


	public void updateBook(Book book) throws Exception {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update Book set title = ?, "
					+ "summary = ?, "
					+ "year_published = ?, "
					+ "publisher_id = ?, "
					+ "isbn = ?  "
					+ "where id = ?");
			st.setString(1, book.getTitle());
			st.setString(2, book.getSummary());
			st.setInt(3, book.getYearPublished());
			st.setInt(4, book.getPublisher().getId());
			st.setString(5, book.getIsbn());
			st.setInt(6, book.getId());
			st.executeUpdate();

			logger.info(book.getTitle() + " SAVED");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new Exception(e);
			}
		}
	}

	public void insertAudit(String msg, Book book) throws Exception{
		PreparedStatement st = null;
		AuditTrailEntry audit = new AuditTrailEntry();
		try {
			st = conn.prepareStatement("INSERT INTO book_audit_trail(entry_msg, book_id) VALUES(?,?)");
			st.setString(1,  msg);
			st.setInt(2, book.getId());

			st.executeUpdate();

			logger.info("Book added");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new Exception(e);
			}
		}
	}
	//This unfortunately is where the book in the book_table gets its id assigned database side, thus not allowing anything to precede
	//Unless I force the user to go to the window instead of prompting them... TODO
	public void insertBook(Book book) throws Exception {
		PreparedStatement st = null;
		PreparedStatement stFindLast = null;

		try {
			st = conn.prepareStatement("INSERT INTO Book(title,summary,year_published,publisher_id,isbn) VALUES(?,?,?,?,?)");
			st.setString(1, book.getTitle());
			st.setString(2, book.getSummary());
			st.setInt(3, book.getYearPublished());
			st.setInt(4, book.getPublisher().getId());
			st.setString(5, book.getIsbn());

			st.executeUpdate();

			stFindLast = conn.prepareStatement("SELECT * FROM `Book` WHERE 1 ORDER BY id DESC LIMIT 1 ");
			ResultSet rs = stFindLast.executeQuery();
			rs.next();
			book.setId(rs.getInt("id"));
			book.setBookGateway(this);

			logger.info(book.getTitle() + " INSERTED");

			insertAudit(" Book added", book);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new Exception(e);
			}
		}
	}

	public void deleteBook(Book book) throws Exception {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM Book WHERE id = ?");
			st.setInt(1, book.getId());
			st.executeUpdate();

			logger.info(book.title + " DELETED");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new Exception(e);
			}
		}
	}
	
	//work sent off to AuthorBookGateway delegate
	public List<AuthorBook> getAuthors(Book book) throws Exception{
		return authorBookGateway.getAuthorsForBook(book);
	}
	
	public void deleteAuthorRelationship(AuthorBook authorBook) throws Exception {
		try {
			authorBookGateway.deleteAuthorRelationship(authorBook);
			insertAudit("Author "+ authorBook.getAuthor().getFirstName()+" "+authorBook.getAuthor().getLastName()+" removed with "+ authorBook.getRoyalty()+" royalty" , authorBook.getBook());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void updateAuthorRoyalty(AuthorBook authorBook, String oldRoyalty, int newRoyalty) throws Exception {
		try {
			authorBookGateway.updateAuthorRoyalty(authorBook, newRoyalty);
			insertAudit(authorBook.getAuthor().getFirstName() + " "+authorBook.getAuthor().getLastName()+" royalty changed from "+oldRoyalty+" to "+(newRoyalty/10000)+"%",authorBook.getBook());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addAuthor(AuthorBook authorBook) throws Exception {
		try {
			authorBookGateway.addAuthor(authorBook);
			insertAudit(authorBook.getAuthor().getFirstName() + " "+authorBook.getAuthor().getLastName()+" added at "+authorBook.getRoyalty()+" royalty",authorBook.getBook());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
