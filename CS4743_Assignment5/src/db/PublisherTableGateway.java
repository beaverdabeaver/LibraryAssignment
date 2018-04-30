package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import book.Publisher;

public class PublisherTableGateway {
	private Connection conn;
	private static Logger logger;
	
	public PublisherTableGateway(Connection conn) {
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
	
	public ObservableList<Publisher> getPublishers() throws Exception{
		ObservableList<Publisher> publishers = FXCollections.observableArrayList();
		PreparedStatement st = null;
	
		try {
			st = conn.prepareStatement("SELECT * FROM `Publisher` ORDER BY `Publisher`.`publisher_name` ASC");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Publisher publisher = new Publisher();
				publisher.setId(rs.getInt("id"));
				publisher.setPublisherName(rs.getString("publisher_name"));
				
				publishers.add(publisher);	
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
		
		return publishers;
	}
	
	public Publisher getPublisherById(int id) throws Exception{
		PreparedStatement st = null;
		Publisher publisher = new Publisher();
		
		try {
			st = conn.prepareStatement("SELECT * FROM `Publisher` WHERE `id` = ?");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			rs.next();
			publisher.setId(rs.getInt("id"));
			publisher.setPublisherName(rs.getString("publisher_name"));
			
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
		
		return publisher;
		
	}

}
