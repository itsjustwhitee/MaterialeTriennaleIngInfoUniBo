package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import beans.*;

public class Counter extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
    public void init(ServletConfig conf) throws ServletException
	{
        super.init(conf);
        this.resetStats();
        this.getServletContext().setAttribute("month", this.getMonthOfNow());
        
        System.out.println("Init Counter eseguito");
	}
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
	{
		this.checkAndUpdateStats();
		Gson gson = new Gson();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
		
		Request req = gson.fromJson(jsonBuilder.toString(), Request.class);
		
		System.out.println("Ricevuto json e parsato.\nFile1:\n" + req.getFile1() + "\n\nFile2:\n" + req.getFile2());
		
		int totUpper1 = 0, totUpper2 = 0, totLower1 = 0, totLower2 = 0;
		
		for(char c : req.getFile1().toCharArray())
		{
			if(Character.isLowerCase(c))
				totLower1++;
			else if(Character.isUpperCase(c))
				totUpper1++;
		}
		
		for(char c : req.getFile2().toCharArray())
		{
			if(Character.isLowerCase(c))
				totLower2++;
			else if(Character.isUpperCase(c))
				totUpper2++;
		}
		
		synchronized(this.getServletContext())
		{
			this.getServletContext().setAttribute("totUpper", (int) this.getServletContext().getAttribute("totUpper") + totUpper1 + totUpper2);
			this.getServletContext().setAttribute("totLower", (int) this.getServletContext().getAttribute("totLower") + totLower1 + totLower2);
			this.getServletContext().setAttribute("totProcessed", (int) this.getServletContext().getAttribute("totUpper") 
																	+ (int) this.getServletContext().getAttribute("totLower"));
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		/*
		String json = "{\"totUpper1\":" + totUpper1 + ",\"totLower1\":" + totLower1 
				+ ",\"totUpper2\":" + totUpper2 + ",\"totLower2\":" + totLower2  + "}";
		*/
		String json = gson.toJson(new Response(totUpper1, totUpper2, totLower1, totLower2));
		System.out.println("Terminato conteggio e pronto json: " + json);
		
		response.getWriter().write(json);
		response.getWriter().flush();
	}
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
	{
		System.out.println("DoGet Counter");
	}
	
	public void checkAndUpdateStats()
	{
		synchronized(this.getServletContext())
		{
			if((int) this.getServletContext().getAttribute("month") != this.getMonthOfNow())
				this.resetStats();
		}
	}
	
	private int getMonthOfNow()
	{
		return LocalDateTime.now().getMonth().ordinal() + 1;
	}
	
	private void resetStats()
	{
		synchronized(this.getServletContext())
		{
			this.getServletContext().setAttribute("totUpper", 0);
	        this.getServletContext().setAttribute("totLower", 0);
	        this.getServletContext().setAttribute("totProcessed", 0);
		}
	}

}
