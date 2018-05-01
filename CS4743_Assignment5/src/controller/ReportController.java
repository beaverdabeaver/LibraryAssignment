package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ListIterator;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import misc.AlertHelper;
import model.AuditTrailEntry;
import model.AuthorBook;

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
	
	private File file;

	public ReportController(BorderPane rootPane) throws Exception{
		distributer = GatewayDistributer.getInstance();

		pubGateway = distributer.getPublisherGateway();
		publishers = pubGateway.getPublishers();
		
		logger = LogManager.getLogger();
		this.rootPane = rootPane;
		
	}
	
	void fileWriter(File file, Publisher publisher) throws Exception{
		StringBuilder sb = new StringBuilder();
		
		sb.append("Royalty	Report\n");
		
		sb.append("Publisher: " + publisher.getPublisherName() + "\n");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd yyy HH:mm");
		LocalDateTime now = LocalDateTime.now();
		//System.out.println(dtf.format(now)); //2016/11/16 12:08:43
		
		List<Book> book = distributer.getBookGateway().getBooksByPublisherID(publisher.getId());
		
		sb.append("Report Generated on "+ dtf.format(now) + "\n");
		
		sb.append("\n");
		
		//add column headers
		sb.append("Book Title\tISBN\tAuthor\tRoyalty\n");
		
		for(ListIterator<Book> iter = book.listIterator(); iter.hasNext();){
			Book currentBook = iter.next();
			sb.append(currentBook.getTitle() + "\t" + currentBook.getIsbn() + "\t");
			List<AuthorBook> authors = distributer.getBookGateway().getAuthors(currentBook);
			int runningTotal = 0;
			for(ListIterator<AuthorBook> authIter = authors.listIterator(); authIter.hasNext();){
				AuthorBook currentAuthor = authIter.next();
				if (runningTotal == 0){
					sb.append(currentAuthor.getAuthor().getFirstName() +" "+ currentAuthor.getAuthor().getLastName()+"\t" + currentAuthor.getRoyalty()+"");
					//logger.info(currentAuthor.getIntRoyalty());
					runningTotal += currentAuthor.getIntRoyalty();
				} else{
					sb.append("\t\t"+currentAuthor.getAuthor().getFirstName() +" "+ currentAuthor.getAuthor().getLastName()+"\t" + currentAuthor.getRoyalty()+"");
					runningTotal += currentAuthor.getIntRoyalty();
				}
				sb.append("\n");
			}
			sb.append("\t\tTotal Royalty\t"+runningTotal/10000.0+"%\n\n");
		}
		
		//write to a file
		try (FileOutputStream f = new FileOutputStream(file)) {
			f.write(sb.toString().getBytes());
			logger.info("file saved!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
    void saveButtonClicked(MouseEvent event) throws Exception {
		logger.info("Save button clicked!");
		if(publisherComboID.getSelectionModel().getSelectedItem() == null){
			AlertHelper.showWarningMessage("Uhh","Did you select a publisher?", "Cuz I'm getting a null vaule lol");
			return;
		}
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Report");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XLS", "*.xls"));
		file = fileChooser.showSaveDialog(new Stage());
		fileWriter(file, publisherComboID.getSelectionModel().getSelectedItem());
		
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
