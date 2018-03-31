package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import book.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.AuditTrailEntry;
import model.Author;

public class AuthorTableGateway {
	private Connection conn;
	private static Logger logger;
	
	public AuthorTableGateway(Connection conn) {
		this.conn = conn;
		logger = LogManager.getLogger();
	}
	
	public void closeConnection() {
		logger.info("Closing connection...");
		
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ObservableList<Author> getAuthors() throws Exception{
		ObservableList<Author> authors = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT * FROM `Author` ORDER BY `Author`.`first_name` ASC");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Author author = new Author();
				author.setId(rs.getInt("id"));
				author.setFirstName(rs.getString("first_name"));
				author.setLastName(rs.getString("last_name"));
				author.setDob(rs.getDate("dob"));
				author.setGender(rs.getString("gender"));
				author.setWebsite(rs.getString("web_site"));
				author.setLastModified(rs.getTimestamp("last_modified").toLocalDateTime());
				author.setGateway(this);
				author.setId(rs.getInt("id"));
				authors.add(author);	
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
		
		return authors;
	}
	
	public LocalDateTime getAuthorLastModified(int id) throws Exception{
		PreparedStatement st = null;
		LocalDateTime time;
		
		try {
			st = conn.prepareStatement("SELECT * FROM `Author` WHERE id=?");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			rs.next();
			time = rs.getTimestamp("last_modified").toLocalDateTime();
			
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
		return time;
	}
	
	public ObservableList<AuditTrailEntry> getAudits(int author_id) throws Exception{
		PreparedStatement st = conn.prepareStatement("SELECT * FROM author_audit_trail WHERE author_id = " + author_id + " ORDER BY date_added ASC");
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
	
	public void insertAudit(String msg, Author author) throws Exception{
		PreparedStatement st = null;
		AuditTrailEntry audit = new AuditTrailEntry();
		try {
			st = conn.prepareStatement("INSERT INTO author_audit_trail(entry_msg, author_id) VALUES(?,?)");
			st.setString(1, msg);
			st.setInt(2, author.getId());

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
	
	public Boolean isModified(int id, LocalDateTime time) throws Exception{
		PreparedStatement st = null;
		LocalDateTime oldDateTime = time;
		
		LocalDateTime serverDateTime = LocalDateTime.now();
		
		Boolean isDiff;
		
		try {
			st = conn.prepareStatement("SELECT * FROM `Author` WHERE id=?");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			rs.next();
			serverDateTime = rs.getTimestamp("last_modified").toLocalDateTime();
			
			if(serverDateTime.isEqual(oldDateTime)){
				isDiff = false;
			} else{
				isDiff = true;
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
		System.out.println("has been modified is "+isDiff+" cuz servertime is "+serverDateTime.toString()+" and model time is "+ oldDateTime.toString());
		return isDiff;
	}
	
	public void updateAuthor(Author author) throws Exception {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update Author set first_name = ?, "
					+ "last_name = ?, "
					+ "dob = ?, "
					+ "gender = ?, "
					+ "web_site = ?  "
					+ "where id = ?");
			st.setString(1, author.getFirstName());
			st.setString(2, author.getLastName());
			st.setDate(3, author.getDobDate());
			st.setString(4, author.getGender());
			st.setString(5, author.getWebsite());
			st.setInt(6, author.getId());
			st.executeUpdate();
			
			logger.info(author.getFirstName() + " " + author.getLastName() + " SAVED");
			
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
	
	public void insertAuthor(Author author) throws Exception {
		PreparedStatement st = null;
		PreparedStatement stFindLast = null;
		
		try {
			st = conn.prepareStatement("INSERT INTO Author(first_name,last_name,dob,gender,web_site) VALUES(?,?,?,?,?)");
			st.setString(1, author.getFirstName());
			st.setString(2, author.getLastName());
			st.setDate(3, author.getDobDate());
			st.setString(4, author.getGender());
			st.setString(5, author.getWebsite());
			
			st.executeUpdate();
			
			stFindLast = conn.prepareStatement("SELECT * FROM `Author` WHERE 1 ORDER BY id DESC LIMIT 1 ");
			ResultSet rs = stFindLast.executeQuery();
			rs.next();
			author.setId(rs.getInt("id"));
			author.setGateway(this);

			logger.info(author.getFirstName() + " " + author.getLastName() + " INSERTED");
			
			insertAudit("Author added", author);
			
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
	
	public void deleteAuthor(Author author) throws Exception {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM Author WHERE id = ?");
			st.setInt(1, author.getId());
			st.executeUpdate();
			
			logger.info(author.getFirstName() + " " + author.getLastName() + " DELETED");
			
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
