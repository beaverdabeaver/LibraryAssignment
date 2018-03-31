package controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import book.Book;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.AuditTrailEntry;

public class AuditTrailController implements Initializable {

	private static Logger logger;
	private ControllerSingleton controller = ControllerSingleton.getInstance();
	private BorderPane rootPane;
	private Book book;
	private ObservableList<AuditTrailEntry> audit;

	@FXML private Label text;
	@FXML private ListView<AuditTrailEntry> auditList;
	@FXML private Button back;
	

	public AuditTrailController(ObservableList<AuditTrailEntry> audit, Book book, BorderPane rootPane){
		logger = LogManager.getLogger();
		this.audit = audit;
		this.book = book;
		this.rootPane = rootPane;
		//text.setText("Audit Trail for " + book.getTitle());
	}
	@FXML
	void onBackClicked(MouseEvent event){
		logger.info("Back clicked");
		try {
			controller.changeView("/view/BookDetailView.fxml", new BookDetailController(this.book, rootPane), rootPane);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initialize(URL arg0, ResourceBundle arg1){
		text.setText("Audit Trail for " + book.getTitle());
		auditList.setItems(audit);
		logger.info("Audit Trail initialized");
	}
	
}
