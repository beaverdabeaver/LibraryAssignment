package model;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

public class AuditTrailEntry {
	private int id;
	private Date dateAdded;
	private String message;
	
	public AuditTrailEntry(){
		id = 0;
		dateAdded = null;
		message = "";
	}

	//getters
	public int getId() { return id;}

	public Date getDateAdded() { return dateAdded; }

	public String getMessage() { return message; }

	//setters
	public void setId(int id) { this.id = id; }

	public void setDateAdded(Date date) { this.dateAdded = date; }
	
	public void setMessage(String message) { this.message = message; }
	
	@Override
	public String toString() {
		
		return getDateAdded()+" "+ getMessage();
	}
	
}
