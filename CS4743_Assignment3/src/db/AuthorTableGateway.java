package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
