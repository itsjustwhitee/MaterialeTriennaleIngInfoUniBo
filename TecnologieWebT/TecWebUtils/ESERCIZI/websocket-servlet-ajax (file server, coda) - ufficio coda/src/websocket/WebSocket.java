package websocket;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.*;
import javax.websocket.server.*;

import com.google.gson.Gson;

import beans.Request;
import beans.Response;

//status di risposta possibili: 
//	-accepted	~> se richiesta accettata
//	-refused	~> se richiesta rifiutata (causa saturazione richieste)
//	-delayed	~> se richiesta ritardata a causa di vip arrivato
//	-serving	~> se richiesta in lavorazione
//	-closed		~> se richiesta chiusa (correttamente)

@ServerEndpoint("/actions")
public class WebSocket {
	
	List<Request> requestsQueue = new ArrayList<>();
	Map<String, Session> sessions = new HashMap<>();
	final LocalTime closure = LocalTime.of(23, 45);
	LocalTime currentOpStart = null;

	@OnOpen
	public void onOpen(Session session) {
	    System.out.println("Connessione aperta: " + session.getId());
	    updateQueue();
	    if(this.requestsQueue.isEmpty())
	    	this.currentOpStart = null;
	}
	
	@OnClose
	public void onClose(Session session) {
	    System.out.println("Connessione chiusa: " + session.getId());
	    synchronized (sessions)
	    {
	    	for(Entry<String, Session> s : sessions.entrySet())
	    	{
	    		if(s.getValue().getId().equals(session.getId()))
	    			sessions.remove(s.getKey());
	    	}
	    }
	    updateQueue();
	}
	
	@OnError
	public void onError(Session session, Throwable throwable) {
		updateQueue();
	}
	
	@OnMessage
	public void onMessage(String message, Session session)
	{
		Gson gson = new Gson();
		Request req = gson.fromJson(message, Request.class);
		System.out.println("Ricevuto messaggio di request: " + req.toString());
		
		Response resp = new Response();

		if(req.getApplicant() != null && req.getType() != null && !req.getApplicant().isBlank() && !req.getType().isBlank())
		{
			this.sessions.put(req.getApplicant(), session);
			int timeToWait = calculateAwaitingTime();
			if(req.isPriority())
			{
				System.out.println("Richiesta con prioritá");
				synchronized(requestsQueue)
				{	
					int i = 0;
					while(this.requestsQueue.get(i).isPriority()) //gestisco possibilitá di altri utenti vip giá acceduti
					{
						if(!this.requestsQueue.get(i).isPriority())
							this.requestsQueue.add(i, req);
						i++;
					}
				}
				resp.setMessage("Vip arrived");
				resp.setStatus("delayed");
				this.sendToAll(resp, true);
			}
			
			if(LocalTime.now().plusMinutes(timeToWait + 8).isAfter(closure))
			{
				//notifica ai cittadini in coda 
				System.out.println("Richiesta non prioritaria rifiutata");
				resp.setMessage("In closure");
				resp.setStatus("refused");
				resp.setTimeToWait(-1);
				this.sendToAll(resp, false);
				return;
			}
			else if(!req.isPriority())
			{
				System.out.println("Richiesta non prioritaria accettata");
				resp.setMessage(req.getType());
				resp.setStatus("accepted");
				resp.setTimeToWait(timeToWait);
				
				synchronized (requestsQueue)
				{
					this.requestsQueue.add(req);
				}
			}
		}
		
		try {
			System.out.println("Invio messaggio json\n" + gson.toJson(resp));
			session.getBasicRemote().sendText(gson.toJson(resp));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(this.requestsQueue.size() == 1)
			this.currentOpStart = LocalTime.now();
		updateQueue();
	}
	
	private int calculateAwaitingTime()
	{
		int tot = 0;
		for(Request req : this.requestsQueue)
			tot += (req.getType().equals("cambio") ? 8 : 5);
		System.out.println("Tempo di attesa calcolato (ultimo in coda): " + tot);
		return tot;
	}
	
	private int calculateAwaitingTimeFor(String username)
	{
		int tot = 0;
		for(Request req : this.requestsQueue)
		{
			tot += (req.getType().equals("cambio") ? 8 : 5);
			if(req.getApplicant().equals(username))
				break;
		}
		System.out.println("Tempo di attesa per " + username + ": " + tot);
		return tot;
	}
	
	private void sendToAll(Response response, boolean withTime)
	{	synchronized(sessions)
		{
			Gson gson = new Gson();
			try
			{
				System.out.println("Invio broadcast di " + response);
				for(Entry<String, Session> s : sessions.entrySet())
				{
					if(withTime)
					{
						response.setTimeToWait(calculateAwaitingTimeFor(s.getKey()));
					}
					s.getValue().getBasicRemote().sendText(gson.toJson(response));
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void updateQueue()
	{
		System.out.println("Aggiornamento coda...");
		synchronized (requestsQueue)
		{
			if(this.requestsQueue.isEmpty() || this.sessions.isEmpty())
				return;
			Gson gson = new Gson();
			while(LocalTime.now().getMinute() - this.currentOpStart.getMinute() >= (this.requestsQueue.get(0).getType() .equals("cambio") ? 8 : 5))
			{
				this.currentOpStart = this.currentOpStart.plusMinutes(this.requestsQueue.get(0).getType() .equals("cambio") ? 8 : 5);
				System.out.println("Terminata richiesta di " + requestsQueue.get(0).getApplicant());
				try {
					this.sessions.get(this.requestsQueue.get(0).getApplicant()).getBasicRemote().sendText(
							gson.toJson(new Response("closed", "Update confirmed", 0)));
					this.sessions.get(this.requestsQueue.get(0).getApplicant()).close();
				} catch (IOException e) {
					e.printStackTrace();
				};
				this.requestsQueue.remove(0);
				
				if(this.requestsQueue.isEmpty())
				{
					System.out.println("Richieste esaurite, coda vuota");
					return;
				}
				
				System.out.println("Servendo richiesta di " + requestsQueue.get(0).getApplicant() + " (" + 
						(this.requestsQueue.get(0).getType() .equals("cambio") ? 8 : 5) + "\')");
				try {
					this.sessions.get(this.requestsQueue.get(0).getApplicant()).getBasicRemote().sendText(
							gson.toJson(new Response("serving", this.requestsQueue.get(0).getType(), 
							(this.requestsQueue.get(0).getType() .equals("cambio") ? 8 : 5))));
				} catch (IOException e) {
					e.printStackTrace();
				};
			}
		}
	}
	
}
