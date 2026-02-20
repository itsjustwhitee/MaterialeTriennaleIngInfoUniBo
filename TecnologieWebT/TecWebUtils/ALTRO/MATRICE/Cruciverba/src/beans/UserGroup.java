package beans;

import java.util.ArrayList;

public class UserGroup {
	private ArrayList<User> users;
	
	
    public UserGroup() {
    	this.users = new ArrayList<User>();
    }
    
    public ArrayList<User> getLista(){
    	return this.users;
    }
    
    public boolean checkUser(User u) {  //per registrare un utente si usa il suo Username come identificativo
    	for(User checker : this.getLista())
    		if(checker.getUsername().equals(u.getUsername()))
    			return true;
    	return false;
    }
    
    public User getUser(String username) {
    	for(User u : this.getLista()) 
    		if(u.getUsername().equals(username))
    			return u;
    	return null;
    }
    
    public void addUser(User u) {
    	this.users.add(u);
    }
    
    public void invalidateSession(String username) {  //per invalidare la sessione di un utente dato l'username
    	for (User u : this.getLista())
    		if(u.getUsername().equals(username)) {
    			u.logout();
    			break;
    		}
    	return;
    }
    
}