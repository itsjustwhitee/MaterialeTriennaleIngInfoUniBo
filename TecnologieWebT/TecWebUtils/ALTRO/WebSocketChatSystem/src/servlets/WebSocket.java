package servlets;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpServlet;
import javax.websocket.*;
import javax.websocket.server.*;

import com.google.gson.Gson;

import beans.Risposta;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/actions")
public class WebSocket {

	private static LocalDate data;
	private Session session;
	private Gson gson;
	private Set<String> users = new HashSet<String>();
	private static final Set<WebSocket> endpoints = new CopyOnWriteArraySet<>();

	@OnOpen
	public void open(Session session) {
		this.session = session;
		users.add(session.getId());
		endpoints.add(this);
		gson = new Gson();
		if (users.size() == 2)
			WebSocket.data = LocalDate.now();
		System.out.println("Connessione aperta");
	}

	@OnClose
	public void close(Session session) {
		System.out.println("Connessione Chiusa ");
	}

	@OnMessage
	public void handleMessage(String message, Session session) throws IOException, EncodeException {
		Risposta risposta = new Risposta();

		if (ChronoUnit.MINUTES.between(data, LocalDate.now()) < 30) {
			String messaggio = gson.fromJson(message, String.class);
			risposta.setMessaggio(session.getId() + " " + messaggio);
			risposta.setChiusa(false);

			if (!message.startsWith("S") && !message.startsWith("A")) {
				risposta.setValid(true);
			} else
				risposta.setValid(false);
		} else {
			risposta.setChiusa(true);
			risposta.setValid(false);
			risposta.setMessaggio("chat chiusa");
		}

		broadcast(gson.toJson(risposta));

	}

	private static void broadcast(String message) throws IOException, EncodeException {

		endpoints.forEach(endopoint -> {
			try {
				endopoint.session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.out.println("Errore ");
	}

}
