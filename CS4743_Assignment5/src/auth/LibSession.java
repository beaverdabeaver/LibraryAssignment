package auth;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LibSession implements Serializable{

	private LocalDateTime expirationDate;
	
	private String authType;
	
	private int sessionNum;
	
	public LibSession(String loginAuth, int sessionCount){
		authType = loginAuth;
		sessionNum = sessionCount;
		expirationDate = LocalDateTime.now().plusMinutes(60);
	}
	
	public LocalDateTime getExpirationDate(){
		return expirationDate;
	}
	
	public String authType(){
		return authType;
	}
	
	public int getSessionNum(){
		return sessionNum;
	}
	
}
