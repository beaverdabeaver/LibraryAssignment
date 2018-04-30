package db;

import java.sql.Connection;

import controller.ControllerSingleton;

public class GatewayDistributer {
	private static GatewayDistributer distributer = null;
	
	private AuthorTableGateway authorGateway;
	private BookTableGateway bookGateway;
	private PublisherTableGateway publisherGateway;
	
	private ControllerSingleton controller;
	private Connection conn;
	
	public GatewayDistributer(){
		controller = ControllerSingleton.getInstance();
		conn = controller.getConnection();
		
		authorGateway = new AuthorTableGateway(conn);
		bookGateway = new BookTableGateway(conn);
		publisherGateway = new PublisherTableGateway(conn);
	}
	
	public static GatewayDistributer getInstance(){
		if(distributer == null){
			distributer = new GatewayDistributer();
		}
		return distributer;
	}
	
	//getters
	public AuthorTableGateway getAuthorGateway(){ return authorGateway; }
	
	public BookTableGateway getBookGateway(){ return bookGateway; }
	
	public PublisherTableGateway getPublisherGateway() { return publisherGateway; }
	

}
