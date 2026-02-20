package beans;

import java.io.Serializable;

public class Request implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String applicant;
	private boolean priority;
	private String type;
	
	public Request() {
		super();
		this.applicant = null;
		this.priority = false;
		this.type = "";
	}
	
	public Request(String applicant, boolean priority, String type) {
		super();
		this.applicant = applicant;
		this.priority = priority;
		this.type = type;
	}
	
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public boolean isPriority() {
		return priority;
	}
	public void setPriority(boolean priority) {
		this.priority = priority;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
	    return "Request{" +
	            "applicant='" + applicant + '\'' +
	            ", priority=" + priority +
	            ", type='" + type + '\'' +
	            '}';
	}
	
}
