package beans;

public class Risposta {
	private boolean close;
	private boolean valid;
	private String message;
	private String user;
	
	public Risposta(boolean close, boolean valid, String message,String user) {
		super();
		this.close = close;
		this.valid = valid;
		this.message = message;
		this.user = user;
	}
	public boolean isClose() {
		return close;
	}
	public void setClose(boolean close) {
		this.close = close;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	
	
}
