package servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.CounterSlave;

import java.util.*;

public class MatrixCalculator extends HttpServlet{
	
	private static final int lines = 4;
	private static final int column = 2;
	
	@Override
	public void init(ServletConfig conf) throws ServletException
	{
		super.init(conf);
		this.getServletContext().setAttribute("time", new Date());
	}
	
	public void doPost(HttpServletRequest request,
            HttpServletResponse response)
    throws ServletException, IOException
	{
		Date primoTempo = (Date)this.getServletContext().getAttribute("time");
		long nowhours = (int)new Date().getTime();
		long firsthour = (int)primoTempo.getTime();
		int requestCounter = (Integer)this.getServletContext().getAttribute("operationCounter");
		
		
		if(nowhours-firsthour >3600000)
		{
			requestCounter=0;
			this.getServletContext().setAttribute("time", new Date());
			
		}else
		{
			requestCounter++;
			
		}
		this.getServletContext().setAttribute("operationCounter", requestCounter);
		String mode = request.getParameter("mode");
		int[][] valA = new int[lines][column];
		int[][] valB = new int[lines][column];
		StringTokenizer tokenizer = new StringTokenizer(request.getParameter("aVal"),"@");
		StringTokenizer tokenizer2 = new StringTokenizer(request.getParameter("bVal"),"@");
		for(int i=0; i<lines; i++)
		{
			for(int j=0; j<column; j++)
			{
				valA[i][j] = Integer.parseInt(tokenizer.nextToken());
				valB[i][j] = Integer.parseInt(tokenizer2.nextToken());
			}
		}
		String res="";
		if(mode.compareTo("single")==0)
		{
			for(int i=0; i<lines; i++)
			{
				for(int j=0; j<column; j++)
				{
					res = res + (valA[i][j] - valB[i][j]) + "@";
				}
			}
		}
		else
		{
			CounterSlave[] threads = new CounterSlave[4];
			int[][] firstA = new int[lines/2][column/2];
			int[][] secondA = new int[lines/2][column/2];
			int[][] thirdA = new int[lines/2][column/2];
			int[][] forthA = new int[lines/2][column/2];
			
			int[][] firstB = new int[lines/2][column/2];
			int[][] secondB = new int[lines/2][column/2];
			int[][] thirdB = new int[lines/2][column/2];
			int[][] forthB = new int[lines/2][column/2];
			
			for(int i=0; i<lines/2; i++)
			{
				for(int j=0;j<column/2;j++)
				{
					firstA[i][j] = valA[i][j];
					firstB[i][j] = valB[i][j];
				}
			}
			
			for(int i=0; i<lines/2; i++)
			{
				for(int j=column/2;j<column;j++)
				{
					secondA[i][j] = valA[i][j];
					secondB[i][j] = valB[i][j];
				}
			}
			for(int i=lines/2; i<lines; i++)
			{
				for(int j=0;j<column/2;j++)
				{
					thirdA[i][j] = valA[i][j];
					thirdB[i][j] = valB[i][j];
				}
			}
			
			for(int i=lines/2; i<lines; i++)
			{
				for(int j=column/2;j<column;j++)
				{
					thirdA[i][j] = valA[i][j];
					thirdB[i][j] = valB[i][j];
				}
			}
			threads[0] = new CounterSlave();
			threads[0].setValA(firstA);
			threads[0].setValB(firstB);
			threads[0].setColumnLimit(column/2);
			threads[0].setLineLimit(lines/2);
			
			threads[1] = new CounterSlave();
			threads[1].setValA(secondA);
			threads[1].setValB(secondB);
			threads[1].setColumnLimit(column/2);
			threads[1].setLineLimit(lines/2);
			
			threads[2] = new CounterSlave();
			threads[2].setValA(thirdA);
			threads[2].setValB(thirdB);
			threads[2].setColumnLimit(column/2);
			threads[2].setLineLimit(lines/2);
			
			threads[3] = new CounterSlave();
			threads[3].setValA(forthA);
			threads[3].setValB(forthB);
			threads[3].setColumnLimit(column/2);
			threads[3].setLineLimit(lines/2);
			
			for(int i = 0;i<4; i++)
			{
				threads[i].start();
			}
			for(int i = 0;i<4; i++)
			{
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(int i=0;i<4;i++)
			{
				int[][] subRes = threads[i].getResult();
				for(int k=0; k<lines/2;k++)
				{
					for(int j=0; j<column/2;j++)
					{
						res = res + subRes[i][j] + "@";
					}
				}
			}
		}
		
		response.getWriter().println(res);
	}
	
	public void doGet(HttpServletRequest request,
            HttpServletResponse response)
    throws ServletException, IOException
	{
	}

}
