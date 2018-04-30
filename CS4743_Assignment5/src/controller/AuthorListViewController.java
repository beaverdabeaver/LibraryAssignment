package controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.AuthorTableGateway;
import db.GatewayDistributer;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import misc.AlertHelper;
import javafx.scene.control.Button;
import model.Author;

public class AuthorListViewController implements Initializable{
	private GatewayDistributer distributer;
	
	@FXML private ListView<Author> authorList;
	@FXML private Button deleteButton;
   
	private static Logger logger;
	private ObservableList<Author> authors;
	private BorderPane rootPane;
	private ControllerSingleton controller = ControllerSingleton.getInstance();
	private AuthorTableGateway gateway;

	public AuthorListViewController(ObservableList<Author> authors, BorderPane rootPane) {
		distributer = GatewayDistributer.getInstance();
		gateway = distributer.getAuthorGateway();
		
		logger = LogManager.getLogger();
		this.authors = authors;
		this.rootPane = rootPane;
	}
	
	@FXML void deleteButtonClicked(MouseEvent event) throws Exception { 
		
		Author selectedAuthor = authorList.getSelectionModel().getSelectedItem();
		selectedAuthor.delete();
		
		//update the list view
		try {
			authors = gateway.getAuthors();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		authorList.setItems(authors);
	}
	
	@FXML
	void onAuthorListClicked(MouseEvent click) {
		//Go to detail view on double click
		if (click.getClickCount() == 2) {
			Author author = authorList.getSelectionModel().getSelectedItem();
			logger.info(author.getFirstName() + " " + author.getLastName() + " double clicked");
			controller.changeView("/view/AuthorDetailView.fxml", new AuthorDetailController(author, rootPane), rootPane);
	    }
	}

	public void initialize(URL location, ResourceBundle resources) {
		authorList.setItems(authors);
	}
}
