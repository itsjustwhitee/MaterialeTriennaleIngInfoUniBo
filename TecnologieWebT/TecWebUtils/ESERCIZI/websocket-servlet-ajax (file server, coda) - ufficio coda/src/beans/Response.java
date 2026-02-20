package beans;

import java.io.Serializable;

public class Response implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String status, message;
	private int timeToWait;
	
	public Response() {
		super();
		status = "error";
		message = "generic error";
		timeToWait = 0;
	}

	public Response(String status, String message, int timeToWait) {
		super();
		this.status = status;
		this.message = message;
		this.timeToWait = timeToWait;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTimeToWait() {
		return timeToWait;
	}

	public void setTimeToWait(int timeToWait) {
		this.timeToWait = timeToWait;
	}
	
	@Override
	public String toString()
	{
		return "status: " + status + "\nmessage: " + message + "\nAttesa: " + timeToWait;
	}
}
