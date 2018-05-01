package controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import book.Book;
import book.Publisher;
import db.GatewayDistributer;
import db.PublisherTableGateway;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.AuditTrailEntry;

public class ReportController implements Initializable {

	private static Logger logger;
	private ControllerSingleton controller = ControllerSingleton.getInstance();
	private BorderPane rootPane;

	@FXML private Label publisherText;
	@FXML private Button saveButtonID;
	@FXML private ComboBox<Publisher> publisherComboID;
	
	private GatewayDistributer distributer;
	private PublisherTableGateway pubGateway;
	private ObservableList<Publisher> publishers;
	

	public ReportController(BorderPane rootPane) throws Exception{
		distributer = GatewayDistributer.getInstance();

		pubGateway = distributer.getPublisherGateway();
		publishers = pubGateway.getPublishers();
		
		
		logger = LogManager.getLogger();
		this.rootPane = rootPane;
	}
	
	@FXML
    void saveButtonClicked(MouseEvent event) throws Exception {
		logger.info("Save button clicked!");
	}
	
	@FXML
	void publisherSelected(ActionEvent event){
		publisherText.setText("Publisher: " + publisherComboID.getSelectionModel().getSelectedItem().getPublisherName());
	}
	
	public void initialize(URL arg0, ResourceBundle arg1){
		//publisherText.setText("Audit Trail for ");
		//publishersCombo.getSelectionModel().getSelectedItem()
		publisherComboID.setItems(publishers);
		logger.info("Report screen initialized");
	}
	
}
