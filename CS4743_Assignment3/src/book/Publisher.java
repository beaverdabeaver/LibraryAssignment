package book;

import javafx.beans.property.SimpleStringProperty;

public class Publisher {
	public int id;
	public SimpleStringProperty publisherName;
	
	public Publisher() {
		publisherName = new SimpleStringProperty();
		id = 1;
		publisherName.set("Unknown");
	}

	public String toString() {
		return publisherName.get();
	}
	
	//getters
	public int getId() { return id;	}

	public String getPublisherName() { return publisherName.get(); }
	
	//property getters
	public SimpleStringProperty getPublisherNameProperty() { return publisherName; }

	//setters
	public void setId(int id) {	this.id = id; }

	public void setPublisherName(String publisherName) { this.publisherName.set(publisherName); }

}
