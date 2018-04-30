package db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import book.Book;
import model.AuthorBook;

public class AuthorBookGateway {
	
	private Connection conn;
	private static Logger logger;
	private AuthorTableGateway authorGateway;
	
	private List<AuthorBook> authorBookList;

	public AuthorBookGateway(Connection conn) {
		this.conn = conn;
		logger = LogManager.getLogger();
		authorGateway = new AuthorTableGateway(conn);
	}

	public List<AuthorBook> getAuthorsForBook(Book book) throws Exception{
		authorBookList = new ArrayList<AuthorBook>();
		PreparedStatement st = conn.prepareStatement("SELECT * FROM author_book WHERE book_id = ?");
		st.setInt(1, book.getId());
		try {
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				AuthorBook authorBook = new AuthorBook();
				authorBook.setAuthor(authorGateway.getAuthor(rs.getInt("author_id")));
				authorBook.setBook(book);
				authorBook.setNewRecord(false);
				
				Double decimalRoyalty = rs.getBigDecimal("royalty").doubleValue();
				
				authorBook.setRoyalty((int)(decimalRoyalty * 1000000));
				
				authorBookList.add(authorBook);
				
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
		return authorBookList;
	}
	
	
	public void deleteAuthorRelationship(AuthorBook authorBook) throws Exception {
		int authorID = authorBook.getAuthor().getId();
		int bookID = authorBook.getBook().getId();
		
		String authorName = authorBook.getAuthor().getFirstName() + " " + authorBook.getAuthor().getLastName();
		String bookTitle = authorBook.getBook().getTitle();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM `author_book` WHERE author_id = ? AND book_id = ?");
			st.setInt(1, authorID);
			st.setInt(2, bookID);
			st.executeUpdate();

			logger.info("Relationship between " + authorName + " and " + bookTitle + " has been DELETED");

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
	
	public void updateAuthorRoyalty(AuthorBook authorBook, int newRoyalty) throws Exception {
		int authorID = authorBook.getAuthor().getId();
		int bookID = authorBook.getBook().getId();
		
		String authorName = authorBook.getAuthor().getFirstName() + " " + authorBook.getAuthor().getLastName();
		String bookTitle = authorBook.getBook().getTitle();
		
		double dRoyalty = (double)newRoyalty;
		dRoyalty /= 1000000;
		BigDecimal bRoyalty = new BigDecimal(dRoyalty);
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update author_book set royalty = ? WHERE author_id = ? AND book_id = ?");
			st.setBigDecimal(1, bRoyalty);
			st.setInt(2, authorID);
			st.setInt(3, bookID);
			
			st.executeUpdate();

			logger.info(authorName + "'s royalty for " + bookTitle + " updated to " + bRoyalty);

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

	public void addAuthor(AuthorBook authorBook) throws Exception {
		int authorID = authorBook.getAuthor().getId();
		int bookID = authorBook.getBook().getId();
		
		String authorName = authorBook.getAuthor().getFirstName() + " " + authorBook.getAuthor().getLastName();
		String bookTitle = authorBook.getBook().getTitle();
		
		double dRoyalty = (double)authorBook.getIntRoyalty() / 100000;
		BigDecimal bRoyalty = new BigDecimal(dRoyalty);
		
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO author_book(author_id,book_id,royalty) VALUES(?,?,?)");
			st.setInt(1, authorID);
			st.setInt(2, bookID);
			st.setBigDecimal(3, bRoyalty);
	
			st.executeUpdate();

			logger.info(authorName + " " + bookTitle + " relationship INSERTED");

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
}
