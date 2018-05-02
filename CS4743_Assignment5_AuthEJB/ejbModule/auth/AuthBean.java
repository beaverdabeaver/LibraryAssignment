package auth;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.ejb.Stateful;

@Stateful
public class AuthBean implements AuthBeanRemote{

	private LibSession session;
	
	private List<LibSession> sessions;
	
	private int logins;
		
	public AuthBean(){
		session = new LibSession("Guest", 0);
		sessions = new ArrayList<>();
		logins = 0;
	}
	
	public LibSession getSession() {
		System.out.println("server side get session selected!!!!!!!!!!!!");
		return session;
	}

	
	public void login(String username, String hash) {
		System.out.println("server side login selected!"+ " login got "+ username + " "+ hash);
		if(username.equals("wilma") && hash.equals("arugula")){
			logins++;
			session = new LibSession("Admin", logins);
			sessions.add(session);
		} else if(username.equals("leroy") && hash.equals("wipeout")){
			logins++;
			session = new LibSession("Data Entry", logins);
			sessions.add(session);
		} else if(username.equals("sasquatch") && hash.equals("jerky")){
			logins++;
			session = new LibSession("Intern", logins);
			sessions.add(session);
		} else {
			logins++;
			session = new LibSession("Guest", logins);
			sessions.add(session);
		}
	}
	
	public void logout(LibSession session){
		System.out.println("server side logout selected! " + session.getSessionNum());
		for(ListIterator<LibSession> iter = sessions.listIterator(); iter.hasNext();){
			LibSession currentSession = iter.next();
			if(currentSession.getSessionNum() == session.getSessionNum()){
				sessions.remove(currentSession);
				return;
			}
		}
	}
	
}
