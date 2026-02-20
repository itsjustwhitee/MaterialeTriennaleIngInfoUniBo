package beans;

import java.io.Serializable;

public class Response implements Serializable{
	private static final long serialVersionUID = 1L;
	private int totUpper1, totUpper2, totLower1, totLower2;
	public Response() {
		super();
		totUpper1 = 0;
		totUpper2 = 0;
		totLower1 = 0;
		totLower2 = 0;
	}
	
	public Response(int totUpper1, int totUpper2, int totLower1, int totLower2) {
		super();
		this.totUpper1 = totUpper1;
		this.totUpper2 = totUpper2;
		this.totLower1 = totLower1;
		this.totLower2 = totLower2;
	}
	
	public int getTotUpper1() {
		return totUpper1;
	}
	public void setTotUpper1(int totUpper1) {
		this.totUpper1 = totUpper1;
	}
	public int getTotUpper2() {
		return totUpper2;
	}
	public void setTotUpper2(int totUpper2) {
		this.totUpper2 = totUpper2;
	}
	public int getTotLower1() {
		return totLower1;
	}
	public void setTotLower1(int totLower1) {
		this.totLower1 = totLower1;
	}
	public int getTotLower2() {
		return totLower2;
	}
	public void setTotLower2(int totLower2) {
		this.totLower2 = totLower2;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
