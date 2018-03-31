package launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**CS 4743 Assignment 2 by Brian Biever & Jonathan Fisher**/

public class Launcher extends Application {
	private static Logger logger = LogManager.getLogger();
	
	public void start(Stage stage) throws Exception {
		logger.info("start() called");
		
		Parent rootPane = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
		Scene scene = new Scene(rootPane, 600, 400);
		stage.setTitle("Assignment 3");
		stage.setScene(scene);
		stage.show();		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
