package controller;

import java.io.IOException;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import db.ConnectionFactory;

public class ControllerSingleton {
	
	public static Connection conn;
	private static ControllerSingleton singleton = null;
	
	private static Logger logger = LogManager.getLogger();
		
	public ControllerSingleton() {}

	public static ControllerSingleton getInstance() {
		if(singleton == null) {
			singleton = new ControllerSingleton();
			establishConnection();
		}
		return singleton;
	}
	
	public void changeView(String FXMLpath, Object controller, BorderPane rootPane){
		try{
			URL fxmlFile = this.getClass().getResource(FXMLpath);
			FXMLLoader loader = new FXMLLoader(fxmlFile);
			
			loader.setController(controller);
	
			Parent contentView = loader.load();
			rootPane.setCenter(null);
			rootPane.setCenter(contentView);
		}catch (IOException e) {
			logger.error("IO exception on view changer");
			e.printStackTrace();
		}
	}
	
	private static void establishConnection() {
		logger.info("Creating connection...");
		
		try {
			conn = ConnectionFactory.createConnection();
		} catch(Exception e) {
			logger.fatal("Cannot connect to db");
			Platform.exit();
		}
	}
	
	public Connection getConnection() {
		return conn;
	}
}