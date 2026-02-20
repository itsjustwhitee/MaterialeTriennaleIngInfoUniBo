package beans;

import java.io.Serializable;

public class Request implements Serializable{

	private static final long serialVersionUID = 1L;
	private String file1, file2;

	public Request() {
		super();
		file1 = "";
		file2 = "";
	}

	public String getFile1() {
		return file1;
	}

	public void setFile1(String file1) {
		this.file1 = file1;
	}

	public String getFile2() {
		return file2;
	}

	public void setFile2(String file2) {
		this.file2 = file2;
	}
}
