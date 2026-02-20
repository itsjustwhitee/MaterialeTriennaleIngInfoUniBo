package servlets;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServlet;
import javax.websocket.*;
import javax.websocket.server.*;
import java.time.temporal.ChronoUnit;

import com.google.gson.Gson;

import beans.Risposta;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/actions")
public class WebSocket {
	private static LocalDateTime data;
	private Session sessione;
	private Gson g;
	private Set<String> users = new HashSet<String>();
	private static Set<Risposta> rispostePrecedenti = new HashSet <Risposta>();
	private static final Set<WebSocket> calcEndpoints = new CopyOnWriteArraySet<>();
	
	@OnOpen
	public void open(Session session)
	{
		this.sessione = session;
		g = new Gson();
		System.out.println("Connessione Aperta ");
		calcEndpoints.add(this);
		users.add(session.getId());
		if(users.size() == 1) {
			WebSocket.data = LocalDateTime.now();
		}
		if(rispostePrecedenti.size() != 0) {
			rispostePrecedenti.forEach( risposta -> {
				try {
					sendback(g.toJson(risposta));
				} catch (IOException | EncodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		System.out.print("ho finito la open");
	}
	
	@OnClose
	public void close(Session session)
	{
		System.out.println("Connessione Chiusa ");
	}
	
	@OnMessage
	public void handleMessage(String message, Session session)throws IOException, EncodeException {
		String mess = g.fromJson(message,String.class);
		if(ChronoUnit.MINUTES.between(data, LocalDateTime.now()) < 30) {
			if(mess.startsWith("A") || mess.startsWith("S")) {
				Risposta r = new Risposta(false,false,"","");
				sendback(g.toJson(r));
			}
			else {
				Risposta r = new Risposta(false,true,mess,this.sessione.getId());
				broadcast(g.toJson(r));
				r.setUser("Messaggio precedente ~ "+r.getUser());
				WebSocket.rispostePrecedenti.add(r);
			}
		}
		else {
			Risposta r=new Risposta(true,true,"","");
			sendback(g.toJson(r));
		}
		
	}

	private void sendback(String message) throws IOException, EncodeException {
		// TODO Auto-generated method stub
		try {
			System.out.println("Sto inviando: "+message);
			this.sessione.getBasicRemote().sendText(message);
			
		} catch (IOException  e) {
            e.printStackTrace();
        }
	}

	@OnError
	public void onError(Session session, Throwable throwable)
	{
		System.out.println("Errore ");
		throwable.printStackTrace();
	}
	
	private static void broadcast(String message) throws IOException, EncodeException {
		
        calcEndpoints.forEach(endpoint -> {
            try {
                endpoint.sessione.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
