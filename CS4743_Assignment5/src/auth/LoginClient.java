package auth;

import java.util.Properties;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import auth.AuthBeanRemote;
import javafx.application.Platform;

public class LoginClient {

	private AuthBeanRemote bean;
	//NOTE: InitialContext MUST have same scope as the bean variable
	private InitialContext context;
	
	private static LoginClient loginSingleton;
	
	private LibSession currentSession;
		
	public LoginClient(){
		Properties props = new Properties();
		//use the jboss factory for context to lookup the EJB remote methods 
		props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		//URL is the jboss server; port 8080 is jboss default for remote corba access 
		props.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
		//below statement triggers the creation of a EJBClientContext containing a EJBReceiver capable of handling the EJB invocations 		
		props.put("jboss.naming.client.ejb.context", "true");
		try {
			//create and save context as instance var
			context = new InitialContext(props);
			//grab ref to bean’s remote interface
			try{
			bean = (AuthBeanRemote) context.lookup("CS4743_Assignment5_AuthEJB/AuthBean!auth.AuthBeanRemote");
			}catch(CommunicationException e){
				System.out.println("server is offline!");
				Platform.exit();
			}
			//System.out.println(bean);
		} catch (NamingException e) {
			e.printStackTrace();
			Platform.exit();
		}
	}
	
	public static LoginClient getInstance(){
		if(loginSingleton == null){
			loginSingleton = new LoginClient();
		}
		return loginSingleton;
	}
	
	public LibSession getSession(){
		currentSession = bean.getSession();
		return currentSession;
	}
	
	public void login(String username, String hash){
		bean.login(username, hash);
	}
	public void logout(LibSession session){
		bean.logout(session);
	}
	
}
