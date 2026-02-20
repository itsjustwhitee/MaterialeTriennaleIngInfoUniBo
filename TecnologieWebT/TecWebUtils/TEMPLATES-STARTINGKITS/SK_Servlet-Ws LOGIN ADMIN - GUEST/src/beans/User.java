package beans;

import java.io.IOException;
import java.io.Serializable;

// É un bean, quindi:
// 	1. ha costruttore senza argomenti,
// 	2. implementa Serializable
// 	3. ha getters e setters (pubblici)

public class User implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String username, password, sessionId;
	private boolean expired, admin;
	private int group;
	
	public User() throws IOException {
		super();
		if(group < -1)
			throw new IOException("Il gruppo deve essere positivo o -1 se non posseduto");
		this.username = "";
		this.password = "";
		this.expired = true;
		this.admin = false;
		this.sessionId = "";
		this.group = -1;
	}
	
	public User(String id, String username, String password, int group, boolean expired, boolean admin) throws IOException {
		super();
		if(group < -1)
			throw new IOException("Il gruppo deve essere positivo o -1 se non posseduto");
		this.username = username;
		this.password = password;
		this.expired = expired;
		this.admin = admin;
		this.sessionId = id;
		this.group = group;
	}
	
	public User(String username, String password, int group, boolean admin) throws IOException {
		super();
		
		if(group < -1)
			throw new IOException("Il gruppo deve essere positivo o -1 se non posseduto");
		this.username = username;
		this.password = password;
		this.expired = true;
		this.admin = admin;
		this.sessionId = "";
		this.group = group;
	}
	
	public User(String username, String password, boolean admin) throws IOException {
		super();
		
		if(group < -1)
			throw new IOException("Il gruppo deve essere positivo o -1 se non posseduto");
		this.username = username;
		this.password = password;
		this.expired = true;
		this.admin = admin;
		this.sessionId = "";
		this.group = -1;
	}
	
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		this.expired = true;
		this.admin = false;
		this.sessionId = "";
		this.group = -1;
	}
	
	public User(String username, String password, int group) {
		super();
		this.username = username;
		this.password = password;
		this.expired = true;
		this.admin = false;
		this.sessionId = "";
		this.group = group;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isExpired() {
		return expired;
	}
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}
	
	@Override
	public String toString()
	{
		return "username=" + username + (admin ? " admin" : "") + " [" + group + "] " 
				+ "expired=" + expired + " sessionId=" + sessionId;
	}
}
