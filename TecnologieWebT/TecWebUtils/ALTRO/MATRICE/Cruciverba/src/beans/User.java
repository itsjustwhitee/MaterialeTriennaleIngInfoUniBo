package beans;

import javax.servlet.http.HttpSession;

public class User {
    private String username;
    private String password;
    private HttpSession session = null;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public HttpSession getSession() {
    	return session;
    }

    public boolean login(HttpSession session) {
    	if(this.session == null) { //utente appena inizializzato o non ancora entrato
    		this.session = session;
    		return true;
    	}else {
    		this.session.invalidate();
    		this.session = session;
    	}	
    	return false; //significa che c'e stato un errore in fase di login
    }
    
    public void logout() { //per invalidare la sessione di questo utente
    	this.session.invalidate();
    	this.session = null;
    	return;
    }
    
}