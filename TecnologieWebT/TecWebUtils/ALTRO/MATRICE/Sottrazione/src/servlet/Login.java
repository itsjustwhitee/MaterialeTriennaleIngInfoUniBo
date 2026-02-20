package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

public class Login extends HttpServlet{
	
	@Override
	public void init(ServletConfig conf) throws ServletException
	{
		super.init(conf);
		List<HttpSession> sessioniAttive = new ArrayList<HttpSession>();
		this.getServletContext().setAttribute("sessioni", sessioniAttive);
		this.getServletContext().setAttribute("operationCounter", 0);
		
	}
	
	@Override
	public void doPost(HttpServletRequest request,
            HttpServletResponse response)
    throws ServletException, IOException
	{
		
		String user = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		HttpSession session = request.getSession();
		List<HttpSession> sessioniAttive = (List<HttpSession>) this.getServletContext().getAttribute("sessioni");
		if(session.isNew())
		{
			sessioniAttive.add(session);
		}
		
		if(user.compareTo("admin")==0 && pwd.compareTo("admin")==0)
		{
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/admin.jsp");	
			rd.forward(request, response);
			return; 
		
		}
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");	
		rd.forward(request, response);
		return; 
	}

}
