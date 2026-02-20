package it;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/websocket")
public class WebSocket {
    private boolean started=false;
    private Session session;

    @OnOpen
    public void open(Session session) {
        //init
        this.session = session;
        System.out.println("Connessione aperta da "+session.getId());
    }

    @OnClose
    public void close(Session session) throws IOException {
        System.out.println("Connessione chiusa da "+session.getId());
        session.close();
    }
    @OnMessage
    public void handleMessage(String message, Session session) throws EncodeException, IOException {
        System.out.println("Ricevuto :" + message + " da " + session.getId());

        switch(message){
            case "register": {
                if(!started){
                    sendBack("Registrazione avvenuta, "+session.getId());
                }else{
                    sendBack("Game running, non è possibile registrarsi, "+session.getId());
                }
            }
            break;
            case "start": {
                if(!started) {
                    sendBack("Gioco avviato da utente " + session.getId());
                    started = true;
                }
            }
            break;
            case "quit": {
                sendBack("Gioco lasciato, " + session.getId());
            }
            break;
            default:
                sendBack("Evento registrato");
        }
    }

    public void sendBack(String message) throws IOException, EncodeException {
        try{
            //this.session.getBasicRemote().sendText(message);
            //per le ws non è definito un broadcast quindi ciclo le sessioni
            for (Session session : session.getOpenSessions()) {
                session.getBasicRemote().sendText(message);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
