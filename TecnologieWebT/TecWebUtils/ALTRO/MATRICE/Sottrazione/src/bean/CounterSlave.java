package bean;

public class CounterSlave extends Thread{
	
	private int[][] valA;
	private int[][] valB;
	private int lineLimit;
	private int columnLimit;
	private int[][] res;
	
	
	public int getLineLimit() {
		return lineLimit;
	}
	public void setLineLimit(int lineLimit) {
		this.lineLimit = lineLimit;
	}
	public int getColumnLimit() {
		return columnLimit;
	}
	public void setColumnLimit(int columnLimit) {
		this.columnLimit = columnLimit;
	}
	public int[][] getValA() {
		return valA;
	}
	public void setValA(int[][] valA) {
		this.valA = valA;
	}
	public int[][] getValB() {
		return valB;
	}
	public void setValB(int[][] valB) {
		this.valB = valB;
	}
	
	public int[][] getResult()
	{
		return this.res;
	}
	@Override
	public void run() {
		this.res = new int[lineLimit][columnLimit];
		for(int i=0; i<lineLimit; i++)
		{
			for(int j=0; j<columnLimit; j++)
			{
				this.res[i][j] = valA[i][j] - valB[i][j];
			}
		}
		
	}
	
	

}
