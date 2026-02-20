package websocket;

import javax.websocket.*;
import javax.websocket.server.*;

@ServerEndpoint("/actions")
public class WebSocket {

	@OnOpen
	public void onOpen(Session session) {
	    System.out.println("Connessione aperta: " + session.getId());

	}
	
	@OnClose
	public void onClose(Session session) {
	    System.out.println("Connessione chiusa: " + session.getId());
	}
	
	@OnError
	public void onError(Session session, Throwable throwable) {
	}
	
	@OnMessage
	public void onMessage(String message, Session session)
	{

	}
}
